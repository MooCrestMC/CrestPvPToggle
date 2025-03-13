package me.levitate.crestPvPToggle.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import me.levitate.crestPvPToggle.models.PlayerData;
import me.levitate.hive.JSONStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PvPManager {
    private final Plugin plugin;

    // If true pvp is enabled, else pvp is disabled.
    @Getter
    private final JSONStorage<UUID, PlayerData> storage;

    // Clear every 60 seconds.
    @Getter
    private final Cache<UUID, Boolean> inCombat = CacheBuilder.newBuilder()
            .expireAfterWrite(60L, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    public PvPManager(Plugin plugin) {
        this.plugin = plugin;

        this.storage = new JSONStorage<UUID, PlayerData>()
                .dataFolder(plugin.getDataFolder())
                .fileName("players.json")
                .keyClass(UUID.class)
                .valueClass(PlayerData.class)
                .build();

        storage.load();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, storage::save, 6000L, 6000L);
    }

    public boolean isPvpEnabled(UUID uuid) {
        return storage.getStorage().getOrDefault(uuid, new PlayerData()).pvpEnabled();
    }

    public void togglePvp(UUID uuid) {
        storage.update(uuid, data -> data.pvpEnabled(!data.pvpEnabled()));
    }
}
