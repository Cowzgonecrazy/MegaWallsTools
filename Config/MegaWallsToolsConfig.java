package cowzgonecrazy.megawallstools.Config;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiListButton;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class MegaWallsToolsConfig {
    public static Configuration config;
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_MWCOOLDOWNS = "mwcooldowns";

    public static boolean isCoinCounterEnabled;
    public static String coinCounterCoinsColor;
    public static String coinCounterDisplayColor;
    public static boolean isKillCounterEnabled;
    public static String killCounterSelectedColor;
    public static boolean isWitherWarningEnabled;
    public static int witherWarning;

    public static String[] possibleColors = {"Black", "Dark Blue", "Dark Green", "Dark Aqua", "Dark Red", "Dark Purple", "Gold", "Gray", "Dark Gray", "Blue", "Green", "Aqua", "Red", "Light Purple", "Yellow", "White"};
    private static String ding = "random.successful_hit";
    private static String lava = "liquid.lavapop";
    private static String click = "random.click";
    private static String levelup = "random.levelup";
    private static String fizz = "random.fizz";
    private static String anvil1 = "random.anvil_land";
    private static String anvil2 = "random.anvil_use";
    public static String[] possibleSounds = {"ding", "lava", "click", "levelup", "fizz", "anvil1", "anvil2"};

    public static boolean isMWCooldownsEnabled;
    public static boolean isMWCAlertsEnabled;
    public static int hunterLevel;
    public static int phoenixLevel;
    public static String mwcAlertSound;

    public static void syncConfig() {
        config.addCustomCategoryComment(CATEGORY_GENERAL, "Here you can manage general settings");
        config.addCustomCategoryComment(CATEGORY_MWCOOLDOWNS, "Here you can manage mwcooldowns settings");

        /*================================= General Configurations ==========================================*/
        List<String> orderGeneral = new ArrayList<String>();
        isCoinCounterEnabled = config.getBoolean("Coin Counter", CATEGORY_GENERAL,  true, "Holds whether coin counter is enabled");
        orderGeneral.add("Coin Counter");
        coinCounterDisplayColor = config.getString("Coins: Color", CATEGORY_GENERAL, "Green", "Holds color for 'coins:'", possibleColors);
        orderGeneral.add("Coins: Color");
        coinCounterCoinsColor = config.getString("# of Coins Color", CATEGORY_GENERAL, "White", "Holds color for # of coins", possibleColors);
        orderGeneral.add("# of Coins Color");
        isKillCounterEnabled = config.getBoolean("Kill Counter", CATEGORY_GENERAL, true, "Holds whether kill counter is enabled");
        orderGeneral.add("Kill Counter");
        killCounterSelectedColor = config.getString("Kill Counter Color", CATEGORY_GENERAL, "White", "Holds color for kill counter", possibleColors);
        orderGeneral.add("Kill Counter Color");
        isWitherWarningEnabled = config.getBoolean("Wither Warning", CATEGORY_GENERAL, true, "Holds whether wither warning is enabled");
        orderGeneral.add("Wither Warning");
        witherWarning = config.getInt("Wither Warning Value", CATEGORY_GENERAL, 50, 1, 1500, "Hold the wither health you will be warned at");
        orderGeneral.add("Wither Warning Value");
        config.setCategoryPropertyOrder(CATEGORY_GENERAL, orderGeneral);

        /*================================= MWCooldowns Configurations ==========================================*/
        List<String> orderMWCooldowns = new ArrayList<String>();
        isMWCooldownsEnabled = config.getBoolean("MWCooldowns", CATEGORY_MWCOOLDOWNS, true, "Holds whether mega walls cooldowns is enabled");
        orderMWCooldowns.add("MWCooldowns");
        isMWCAlertsEnabled = config.getBoolean("MWCooldownsAlerts", CATEGORY_MWCOOLDOWNS, true, "Holds whether mwcooldowns alerts are enabled");
        orderMWCooldowns.add("MWCooldownsAlerts");
        mwcAlertSound = config.getString("MWCAlerts Sound", CATEGORY_MWCOOLDOWNS, "ding", "Holds the sound played in MWCooldowns", possibleSounds);
        orderMWCooldowns.add("MWCAlerts Sound");

        config.setCategoryPropertyOrder(CATEGORY_MWCOOLDOWNS, orderMWCooldowns);

        if(config.hasChanged()) {
            config.save();
        }
    }
}
