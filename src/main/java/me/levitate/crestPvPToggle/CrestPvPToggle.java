package me.levitate.crestPvPToggle;

import co.aikar.commands.PaperCommandManager;
import me.levitate.crestPvPToggle.api.CrestPvPToggleAPI;
import me.levitate.crestPvPToggle.commands.MainCommand;
import me.levitate.crestPvPToggle.hooks.PvPPlaceholders;
import me.levitate.crestPvPToggle.listener.PlayerListener;
import me.levitate.crestPvPToggle.manager.ConfigManager;
import me.levitate.crestPvPToggle.manager.PvPManager;
import me.levitate.hiveChat.HiveChat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrestPvPToggle extends JavaPlugin {
    private PvPManager pvpManager;
    private PvPPlaceholders pvpPlaceholders;

    @Override
    public void onEnable() {
        HiveChat.init(this);

        // Register managers
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        final ConfigManager configManager = new ConfigManager(this);
        this.pvpManager = new PvPManager(this);

        // Initialize API with static access
        CrestPvPToggleAPI.init(pvpManager);

        // Register listener
        new PlayerListener(this, pvpManager, configManager);

        // Register commands
        commandManager.registerCommand(new MainCommand(pvpManager, configManager));
        
        // Register PlaceholderAPI expansion if available
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.pvpPlaceholders = new PvPPlaceholders();
            this.pvpPlaceholders.register();
        }
    }

    @Override
    public void onDisable() {
        if (pvpManager != null)
            pvpManager.getStorage().save();

        if (pvpPlaceholders != null)
            pvpPlaceholders.unregister();
    }
}