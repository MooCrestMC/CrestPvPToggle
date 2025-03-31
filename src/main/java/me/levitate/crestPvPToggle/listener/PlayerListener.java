package me.levitate.crestPvPToggle.listener;

import me.levitate.crestPvPToggle.manager.ConfigManager;
import me.levitate.crestPvPToggle.manager.PvPManager;
import me.levitate.crestPvPToggle.models.PlayerData;
import me.levitate.hiveChat.HiveChat;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlayerListener implements Listener {
    private final PvPManager pvpManager;
    private final ConfigManager configManager;

    public PlayerListener(Plugin plugin, PvPManager pvpManager, ConfigManager configManager) {
        this.pvpManager = pvpManager;
        this.configManager = configManager;

        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        if (!pvpManager.getStorage().getStorage().containsKey(playerUUID)) {
            pvpManager.getStorage().getStorage().put(playerUUID, new PlayerData());
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) ||
                !(event.getEntity() instanceof Player victim))
            return;

        final World world = damager.getWorld();
        if (configManager.config().disabledWorlds.contains(world.getName())) {
            return;
        }

        // Check if both players have pvp enabled, if not then cancel the event
        if (!pvpManager.isPvpEnabled(damager.getUniqueId()) || !pvpManager.isPvpEnabled(victim.getUniqueId())) {
            HiveChat.send(damager, configManager.messages().pvpIsDisabled);
            event.setCancelled(true);
            return;
        }

        // Add players to combat timer, preventing them from disabling pvp for a minute.
        pvpManager.putInCombat(damager.getUniqueId());
        pvpManager.putInCombat(victim.getUniqueId());
    }
}