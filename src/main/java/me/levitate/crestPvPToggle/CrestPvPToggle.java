package me.levitate.crestPvPToggle;

import co.aikar.commands.PaperCommandManager;
import me.levitate.crestPvPToggle.commands.MainCommand;
import me.levitate.crestPvPToggle.listener.PlayerListener;
import me.levitate.crestPvPToggle.manager.ConfigManager;
import me.levitate.crestPvPToggle.manager.PvPManager;
import me.levitate.hiveChat.HiveChat;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrestPvPToggle extends JavaPlugin {

    private PvPManager pvpManager;

    @Override
    public void onEnable() {
        HiveChat.init(this);

        // Register managers
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        final ConfigManager configManager = new ConfigManager(this);
        this.pvpManager = new PvPManager(this);

        // Register listener
        new PlayerListener(this, pvpManager, configManager);

        // Register commands
        commandManager.registerCommand(new MainCommand(pvpManager, configManager));
    }

    @Override
    public void onDisable() {
        if (pvpManager != null)
            pvpManager.getStorage().save();
    }

}
