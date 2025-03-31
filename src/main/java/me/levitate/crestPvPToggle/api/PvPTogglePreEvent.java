package me.levitate.crestPvPToggle.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Event called before a player's PvP state is changed
 * This event is cancellable
 */
@Getter
public class PvPTogglePreEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final UUID playerUUID;

    private final boolean newPvPState;

    @Setter
    private boolean cancelled;

    /**
     * Creates a new PvP Toggle Pre Event
     * @param playerUUID The UUID of the player
     * @param newPvPState The new PvP state
     */
    public PvPTogglePreEvent(UUID playerUUID, boolean newPvPState) {
        this.playerUUID = playerUUID;
        this.newPvPState = newPvPState;
        this.cancelled = false;
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