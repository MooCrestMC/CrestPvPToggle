package me.levitate.crestPvPToggle.api;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.levitate.crestPvPToggle.manager.PvPManager;
import me.levitate.crestPvPToggle.models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The main API class for CrestPvPToggle
 * Provides static methods to interact with the plugin's functionality
 */
@UtilityClass
public class CrestPvPToggleAPI {
    private static PvPManager pvpManager;

    /**
     * -- GETTER --
     *  Checks if the API has been initialized
     *
     */
    @Getter
    private static boolean initialized = false;

    /**
     * Initializes the API - called internally by the plugin
     * @param manager The PvP manager instance
     */
    public static void init(PvPManager manager) {
        if (!initialized) {
            pvpManager = manager;
            initialized = true;
        }
    }

    /**
     * Checks if a player has PvP enabled
     * @param uuid The UUID of the player to check
     * @return true if the player has PvP enabled, false otherwise
     */
    public static boolean isPvPEnabled(UUID uuid) {
        checkInitialization();
        return pvpManager.isPvpEnabled(uuid);
    }

    /**
     * Checks if a player has PvP enabled
     * @param player The player to check
     * @return true if the player has PvP enabled, false otherwise
     */
    public static boolean isPvPEnabled(Player player) {
        return isPvPEnabled(player.getUniqueId());
    }

    /**
     * Sets whether a player has PvP enabled
     * @param uuid The UUID of the player
     * @param enabled Whether PvP should be enabled
     */
    public static void setPvPEnabled(UUID uuid, boolean enabled) {
        checkInitialization();

        PlayerData data = pvpManager.getStorage().getStorage().getOrDefault(uuid, new PlayerData());

        // If state is already correct, do nothing
        if (data.pvpEnabled() == enabled) {
            return;
        }

        // Call pre-event to check if this change should be allowed
        PvPTogglePreEvent preEvent = new PvPTogglePreEvent(uuid, enabled);
        Bukkit.getPluginManager().callEvent(preEvent);

        if (preEvent.isCancelled()) {
            return; // Event was cancelled, don't proceed
        }

        // Update the PvP state
        pvpManager.getStorage().update(uuid, playerData -> playerData.pvpEnabled(enabled));

        // Call post-event to notify listeners that the change has occurred
        PvPTogglePostEvent postEvent = new PvPTogglePostEvent(uuid, enabled);
        Bukkit.getPluginManager().callEvent(postEvent);
    }

    /**
     * Sets whether a player has PvP enabled
     * @param player The player
     * @param enabled Whether PvP should be enabled
     */
    public static void setPvPEnabled(Player player, boolean enabled) {
        setPvPEnabled(player.getUniqueId(), enabled);
    }

    /**
     * Toggles a player's PvP state
     * @param uuid The UUID of the player
     * @return The new PvP state (true if enabled, false if disabled)
     */
    public static boolean togglePvP(UUID uuid) {
        checkInitialization();
        boolean newState = !isPvPEnabled(uuid);
        setPvPEnabled(uuid, newState);
        return newState;
    }

    /**
     * Toggles a player's PvP state
     * @param player The player
     * @return The new PvP state (true if enabled, false if disabled)
     */
    public static boolean togglePvP(Player player) {
        return togglePvP(player.getUniqueId());
    }

    /**
     * Checks if a player is in combat (cannot toggle PvP)
     * @param uuid The UUID of the player
     * @return true if the player is in combat, false otherwise
     */
    public static boolean isInCombat(UUID uuid) {
        checkInitialization();
        return pvpManager.getInCombat().getIfPresent(uuid) != null;
    }

    /**
     * Checks if a player is in combat (cannot toggle PvP)
     * @param player The player
     * @return true if the player is in combat, false otherwise
     */
    public static boolean isInCombat(Player player) {
        return isInCombat(player.getUniqueId());
    }

    /**
     * Sets a player's combat state
     * @param uuid The UUID of the player
     * @param inCombat Whether the player is in combat
     */
    public static void setInCombat(UUID uuid, boolean inCombat) {
        checkInitialization();
        if (inCombat) {
            pvpManager.putInCombat(uuid);
        } else {
            pvpManager.getInCombat().invalidate(uuid);

            // Fire combat state change event
            CombatStateChangeEvent event = new CombatStateChangeEvent(uuid, false);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    /**
     * Sets a player's combat state
     * @param player The player
     * @param inCombat Whether the player is in combat
     */
    public static void setInCombat(Player player, boolean inCombat) {
        setInCombat(player.getUniqueId(), inCombat);
    }

    /**
     * Get the PvPManager instance
     * For advanced usage only - most operations should use the API methods
     * @return The PvPManager instance
     */
    public static PvPManager getPvPManager() {
        checkInitialization();
        return pvpManager;
    }

    /**
     * Checks if the API has been properly initialized before use
     */
    private static void checkInitialization() {
        if (!initialized) {
            throw new IllegalStateException("CrestPvPToggleAPI has not been initialized yet! " +
                    "This is likely because you're trying to use it before the plugin has fully loaded.");
        }
    }
}