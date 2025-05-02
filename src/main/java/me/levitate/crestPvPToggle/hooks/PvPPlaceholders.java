package me.levitate.crestPvPToggle.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.levitate.crestPvPToggle.api.CrestPvPToggleAPI;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PvPPlaceholders extends PlaceholderExpansion {
    
    @Override
    public @NotNull String getIdentifier() {
        return "crestpvp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Levitate";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        
        if (params.equalsIgnoreCase("status")) {
            return CrestPvPToggleAPI.isPvPEnabled(player.getUniqueId()) ? "Enabled" : "Disabled";
        }
        
        return null;
    }
} 