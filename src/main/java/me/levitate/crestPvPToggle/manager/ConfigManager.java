package me.levitate.crestPvPToggle.manager;

import de.exlll.configlib.YamlConfigurations;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.levitate.crestPvPToggle.config.MainConfig;
import me.levitate.crestPvPToggle.config.Messages;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public class ConfigManager {
    private final Path configDir;
    private Messages messages;
    private MainConfig config;

    public ConfigManager(Plugin plugin) {
        configDir = plugin.getDataFolder().toPath();

        final File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().severe("Failed to create plugin directory!");
            return;
        }

        messages = YamlConfigurations.update(
                configDir.resolve("messages.yml"),
                Messages.class
        );

        config = YamlConfigurations.update(
                configDir.resolve("config.yml"),
                MainConfig.class
        );
    }

    public void reload() {
        messages = YamlConfigurations.load(
                configDir.resolve("messages.yml"),
                Messages.class
        );

        config = YamlConfigurations.load(
                configDir.resolve("config.yml"),
                MainConfig.class
        );
    }
}
