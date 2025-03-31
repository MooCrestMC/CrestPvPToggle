package me.levitate.crestPvPToggle.api;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Event called after a player's PvP state has been changed
 */
@Getter
public class PvPTogglePostEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final UUID playerUUID;

    private final boolean newPvPState;

    /**
     * Creates a new PvP Toggle Post Event
     * @param playerUUID The UUID of the player
     * @param newPvPState The new PvP state
     */
    public PvPTogglePostEvent(UUID playerUUID, boolean newPvPState) {
        this.playerUUID = playerUUID;
        this.newPvPState = newPvPState;
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