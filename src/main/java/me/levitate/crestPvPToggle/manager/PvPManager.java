package me.levitate.crestPvPToggle.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.Getter;
import me.levitate.crestPvPToggle.api.CombatStateChangeEvent;
import me.levitate.crestPvPToggle.models.PlayerData;
import me.levitate.hive.JSONStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PvPManager {
    private final Plugin plugin;

    // If true pvp is enabled, else pvp is disabled.
    @Getter
    private final JSONStorage<UUID, PlayerData> storage;

    // Clear every 60 seconds.
    @Getter
    private final Cache<UUID, Boolean> inCombat;

    public PvPManager(Plugin plugin) {
        this.plugin = plugin;

        this.storage = new JSONStorage<UUID, PlayerData>()
                .dataFolder(plugin.getDataFolder())
                .fileName("players.json")
                .keyClass(UUID.class)
                .valueClass(PlayerData.class)
                .build();

        storage.load();

        // Set up cache with removal listener to fire events when combat state changes
        this.inCombat = CacheBuilder.newBuilder()
                .expireAfterWrite(60L, TimeUnit.SECONDS)
                .removalListener((RemovalListener<UUID, Boolean>) notification -> {
                    // Fire event when a player leaves combat
                    CombatStateChangeEvent event = new CombatStateChangeEvent(notification.getKey(), false);
                    Bukkit.getPluginManager().callEvent(event);
                })
                .build();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, storage::save, 6000L, 6000L);
    }

    public boolean isPvpEnabled(UUID uuid) {
        return storage.getStorage().getOrDefault(uuid, new PlayerData()).pvpEnabled();
    }

    public void togglePvp(UUID uuid) {
        storage.update(uuid, data -> data.pvpEnabled(!data.pvpEnabled()));
    }

    /**
     * Puts a player in combat state and fires the appropriate event
     * @param uuid The UUID of the player
     */
    public void putInCombat(UUID uuid) {
        boolean wasInCombat = inCombat.getIfPresent(uuid) != null;
        inCombat.put(uuid, true);

        if (!wasInCombat) {
            // Only fire event if combat state actually changed
            CombatStateChangeEvent event = new CombatStateChangeEvent(uuid, true);
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(event));
        }
    }
}