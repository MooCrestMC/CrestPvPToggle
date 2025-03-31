package me.levitate.crestPvPToggle.api;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Event called when a player's combat state changes
 */
@Getter
public class CombatStateChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final UUID playerUUID;

    private final boolean inCombat;

    /**
     * Creates a new Combat State Change Event
     * @param playerUUID The UUID of the player
     * @param inCombat Whether the player is now in combat
     */
    public CombatStateChangeEvent(UUID playerUUID, boolean inCombat) {
        this.playerUUID = playerUUID;
        this.inCombat = inCombat;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the player associated with this event, if they are online
     * @return The player, or null if they are offline
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}