package me.levitate.crestPvPToggle.config;

import de.exlll.configlib.Configuration;

@Configuration
@SuppressWarnings("FieldMayBeFinal")
public class Messages {

    public String reload = "<#D643FF><bold>CrestReferrals</bold> <dark_gray>» <gray>Plugin reloaded.";
    public String pvpIsDisabled = "<#D643FF><bold>CrestReferrals</bold> <dark_gray>» <gray>Either you or the target has pvp disabled. <gray>(/pvptoggle)";
    public String toggleStatus = "<#D643FF><bold>CrestReferrals</bold> <dark_gray>» <gray>You currently have pvp {status}.";
    public String enabledPvP = "<#D643FF><bold>CrestReferrals</bold> <dark_gray>» <gray>You have enabled pvp.";
    public String disabledPvP = "<#D643FF><bold>CrestReferrals</bold> <dark_gray>» <gray>You have disabled pvp.";
    public String cannotEnableWorld = "<#D643FF><bold>CrestReferrals</bold> <dark_gray>» <gray>You cannot toggle pvp in this world.";
    public String cannotToggle = "<#D643FF><bold>CrestReferrals</bold> <dark_gray>» <gray>You cannot toggle pvp while in combat.";

}
