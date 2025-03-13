package me.levitate.crestPvPToggle.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import me.levitate.crestPvPToggle.manager.ConfigManager;
import me.levitate.crestPvPToggle.manager.PvPManager;
import me.levitate.crestPvPToggle.models.PlayerData;
import me.levitate.hiveChat.HiveChat;
import me.levitate.hiveChat.placeholder.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("pvptoggle|togglepvp")
@CommandPermission("crestpvptoggle.use")
public class MainCommand extends BaseCommand {
    private final PvPManager pvpManager;
    private final ConfigManager configManager;

    public MainCommand(PvPManager pvpManager, ConfigManager configManager) {
        this.pvpManager = pvpManager;
        this.configManager = configManager;
    }

    @Default
    public void toggleStatus(Player player) {
        HiveChat.send(player, configManager.messages().toggleStatus,
                Placeholder.of("status", pvpManager.getStorage().getStorage().getOrDefault(player.getUniqueId(), new PlayerData()).pvpEnabled() ? "enabled" : "disabled"));
    }

    @Subcommand("toggle")
    public void toggle(Player player) {
        if (configManager.config().disabledWorlds.contains(player.getWorld().getName())) {
            HiveChat.send(player, configManager.messages().cannotEnableWorld);
            return;
        }

        if (pvpManager.getInCombat().getIfPresent(player.getUniqueId()) != null) {
            HiveChat.send(player, configManager.messages().cannotToggle);
            return;
        }

        pvpManager.togglePvp(player.getUniqueId());

        if (!pvpManager.isPvpEnabled(player.getUniqueId())) {
            HiveChat.send(player, configManager.messages().disabledPvP);
        } else {
            HiveChat.send(player, configManager.messages().enabledPvP);
        }
    }

    @Subcommand("reload")
    @CommandPermission("crestpvptoggle.reload")
    public void onReload(CommandSender sender) {
        configManager.reload();
        HiveChat.send(sender, configManager.messages().reload);
    }

}
