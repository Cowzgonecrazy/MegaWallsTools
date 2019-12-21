package cowzgonecrazy.megawallstools.Commands;

import java.util.List;
import java.util.Random;

import cowzgonecrazy.megawallstools.Config.ConfigGui;
import cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig;
import cowzgonecrazy.megawallstools.MegaWallsTools;
import cowzgonecrazy.megawallstools.Modules.*;
import com.google.common.collect.Lists;
import cowzgonecrazy.megawallstools.hudproperty.HudPropertyApi;
import cowzgonecrazy.megawallstools.hudproperty.test.DelayedTask;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.Property;


public class CommandMWCooldowns implements ICommand {
    /**
     * converting the sound names you type in game to minecraft's sounds
     */
    static String ding = "random.successful_hit";
    static String lava = "liquid.lavapop";
    static String click = "random.click";
    static String levelup = "random.levelup";
    static String fizz = "random.fizz";
    static String anvil1 = "random.anvil_land";
    static String anvil2 = "random.anvil_use";
    public static String[] sounds = {ding, lava, click, levelup, fizz, anvil1, anvil2};

    public static boolean requestedMap = false;
    public static String mapName = "";
    Random r = new Random();

    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    private final List<String> aliases = Lists.newArrayList("MW");
    private HudPropertyApi api;
    private Property property;

    public CommandMWCooldowns(HudPropertyApi api) {
        this.api = api;
    }

    @Override
    public String getCommandName() {
        return "mw";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "MWCooldowns Mod";
    }
    /**
     * For checking to make sure volume and pitch are floats
     * @param s String from chat arguments that the user types in
     * @return true if its a float, false otherwise
     */
    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        } return true;
    }
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        List<String> soundstrings = Lists.newArrayList("ding", "lava", "click", "levelup", "fizz", "anvil", "anvil2");
        List<Float> soundPitch = Lists.newArrayList(1.0f, .7f, .5f, 1.3f, 2.0f, 1.0f, 2.0f);
        if(args.length < 1 || !(args[0] instanceof String) || args[0].equals("help")){
            help();
        } else if(args[0].contains("hud")){
            CoinCounter.updateCoins();
            if (KillCounter.compiledKA.equals("")) {
                KillCounter.updateCounter();
                new DelayedTask(() -> api.openConfigScreen(), 1);
                KillCounter.compiledKA = "";
                KillCounter.compiledFKA = "";
            } else {
                new DelayedTask(() -> api.openConfigScreen(), 1);
            }
        } else if(args[0].contains("toggle")){
            property = MegaWallsToolsConfig.config.get("mwcooldowns", "MWCooldowns", true);
            if (MegaWallsToolsConfig.isMWCooldownsEnabled) {
                property.set(false);
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Disabled"));
            } else {
                property.set(true);
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Enabled"));
                new Thread(() -> MegaWallsTools.UpdateClass()).start();
            }
            MegaWallsToolsConfig.syncConfig();
            MegaWallsToolsConfig.config.save();
            MegaWallsTools.forcedOnConfigChanged();
        } else if(args[0].contains("update")){
            new Thread(() -> MegaWallsTools.UpdateClass()).start();
        } else if(args[0].contains("clear")){
            MWCooldowns.cooldown = "";
            MWCooldowns.cooldown2 = "";
            MegaWallsTools.MWClass = "";
            CoinCounter.compiledCoins = "";
            KillCounter.compiledKA = "";
            KillCounter.compiledFKA = "";
        } else if(args[0].contains("reset")){
            MWCooldowns.cooldown = "";
            MWCooldowns.cooldown2 = "";
            MegaWallsTools.MWClass = "";
            KillCounter.Kills = 0;
            KillCounter.Assists = 0;
            KillCounter.FinalKills = 0;
            KillCounter.FinalAssists = 0;
            KillCounter.compiledKA = "";
            KillCounter.compiledFKA = "";
            CoinCounter.totalCoins = 0;
            CoinCounter.updateCoins();
        } else if(args[0].contains("alerts")){
            property = MegaWallsToolsConfig.config.get("mwcooldowns", "MWCooldownsAlerts", true);
            if (MegaWallsToolsConfig.isMWCAlertsEnabled) {
                property.set(false);
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Sound Notifications Disbled"));
            } else {
                property.set(true);
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Sound Notifications Enabled"));
            }
            MegaWallsToolsConfig.syncConfig();
            MegaWallsToolsConfig.config.save();
            MegaWallsTools.forcedOnConfigChanged();
        } else if(args[0].contains("volume")){
            if(args.length < 2){
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage: /mw volume <0.0-1.0>"));
            }else if(isFloat(args[1])){
                if(Float.valueOf(args[1])<0 || Float.valueOf(args[1]) > 1){
                    mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cError: You must enter a \u00A7cnumber \u00A7cbetween \u00A7c0 \u00A7cand \u00A7c1!"));
                }else{
                    MWCooldowns.volume = Float.valueOf(args[1]);
                    mc.thePlayer.playSound(MWCooldowns.soundeffect, MWCooldowns.volume, MWCooldowns.pitch);
                    mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Volume updated to " + args[1]));
                }
            }else{
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage: /mw volume <0.0-1.0>"));
            }
        } else if(args[0].contains("pitch")){
            if(isFloat(args[1])){
                if(args.length < 2){
                    mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage: /mw pitch <0.0-1.0>"));
                }else if(Float.valueOf(args[1])<0 || Float.valueOf(args[1]) > 2){
                    mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cError: You must enter a \u00A7cnumber \u00A7cbetween \u00A7c0 \u00A7cand \u00A7c2!"));
                }else{
                    MWCooldowns.pitch = Float.valueOf(args[1]);
                    mc.thePlayer.playSound(MWCooldowns.soundeffect, MWCooldowns.volume, MWCooldowns.pitch);
                    mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Pitch updated to " + args[1]));
                }
            }else{
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage: /mw pitch <0.0-2.0>"));
            }
        } else if(args[0].contains("hunter")){
            if(args.length > 1 && isFloat(args[1]) && Integer.valueOf(args[1]) < 10 && Integer.valueOf(args[1]) > 0){
                property = MegaWallsToolsConfig.config.get("mwcooldowns", "Hunter Level", 9);
                property.set(args[1]);
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Hunter level updated to " + args[1]));
                MegaWallsToolsConfig.syncConfig();
                MegaWallsToolsConfig.config.save();
                MegaWallsTools.forcedOnConfigChanged();
            }else{
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage: /mw hunter <1-9>"));
            }
        } else if(args[0].contains("warning")){
            if(args.length > 1 && isFloat(args[1]) && Integer.valueOf(args[1]) < 1501 && (Integer.valueOf(args[1]) > 0 || Integer.valueOf(args[1]) == -1)) {
                property = MegaWallsToolsConfig.config.get("general", "Wither Warning Value", 50);
                property.set(args[1]);
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Warning updated to " + args[1]));
                MegaWallsToolsConfig.syncConfig();
                MegaWallsToolsConfig.config.save();
                MegaWallsTools.forcedOnConfigChanged();
            }else{
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage: /mw warning <1-1500>"));
            }
        } else if(args[0].contains("sound")) {
            if (args.length < 2) {
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage /mw sound <ding, \u00A7clava, \u00A7cclick, \u00A7clevelup, \u00A7cfizz, \u00A7canvil, \u00A7canvil2>"));
            } else if (args.length == 2 && soundstrings.contains(args[1])) {
                property = MegaWallsToolsConfig.config.get("mwcooldowns", "MWCAlerts Sound", ding);
                property.set(args[1]);
                MegaWallsToolsConfig.syncConfig();
                MegaWallsToolsConfig.config.save();
                MegaWallsTools.forcedOnConfigChanged();
                MWCooldowns.playSounds();
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Sound updated to " + args[1]));
            } else {
                mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage /mw sound <ding, \u00A7clava, \u00A7cclick, \u00A7clevelup, \u00A7cfizz, \u00A7canvil, \u00A7canvil2>"));
            }
        } else if(args[0].contains("phoenix")){
                if(args.length > 1 && isFloat(args[1]) && Integer.valueOf(args[1]) < 10 && Integer.valueOf(args[1]) > 0){
                    property = MegaWallsToolsConfig.config.get("mwcooldowns", "Phoenix Level", 9);
                    property.set(args[1]);
                    mc.thePlayer.addChatMessage(new ChatComponentText("\u00A77[\u00A7bMWCooldowns\u00A77]\u00A7a Phoenix level updated to " + args[1]));
                    MegaWallsToolsConfig.syncConfig();
                    MegaWallsToolsConfig.config.save();
                    MegaWallsTools.forcedOnConfigChanged();
                }else{
                    mc.thePlayer.addChatMessage(new ChatComponentText("\u00A7cUsage: /mw phoenix <1-9>"));
                }
        } else if(args[0].contains("map")) {
            requestedMap = true;
            Utils.sendMessageToServer("/map");
        } else if (args[0].contains("config")) {
            Utils.sendMessage("Opening Config!");
            new DelayedTask(() -> Minecraft.getMinecraft().displayGuiScreen(new ConfigGui()), 1);
        } else {
            help();
        }
    }

    public void help(){
        Utils.sendRawMessage(C.BLUE + "------------------------------------");
        Utils.sendRawMessage(C.AQUA + C.BOLD + "MegaWallsTools v" + MegaWallsTools.VERSION + " by Cowzgonecrazy");
        Utils.sendRawMessage(C.AQUA+ "(Cooldowns originally by Disregard)");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "config" + C.GRAY + "- " + C.GREEN + "Configure Mega Walls Tools");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "hud" + C.GRAY + "- " + C.GREEN + "Configure GUI location");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "clear" + C.GRAY + "- " + C.GREEN + "Clears GUI text.");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "reset" + C.GRAY + "- " + C.GREEN + "Resets counters");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "sound" + C.GRAY + "- " + C.GREEN + "Change alert sound.");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "alerts" + C.GRAY + "- " + C.GREEN + "Toggles alert sounds.");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "volume" + C.GRAY + "- " + C.GREEN + "Change volume (0-1)");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "pitch" + C.GRAY + "- " + C.GREEN + "Change pitch (0-2)");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "toggle" + C.GRAY + "- " + C.GREEN + "Toggles MWCooldowns");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "hunter" + C.GRAY + "- " + C.GREEN + "Set Hunter FoN level.");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "phoenix" + C.GRAY + "- " + C.GREEN + "Set Phoenix Resurrect level.");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "warning" + C.GRAY + "- " + C.GREEN + "Set wither warning health.");
        Utils.sendRawMessage(C.GRAY + "/mw " + C.AQUA + "map" + C.GRAY + "- " + C.GREEN + "Returns what map you are on and recommended classes");
        Utils.sendRawMessage(C.AQUA + "/randomclass " + C.GRAY + "- " + C.GREEN + "Returns a random class in chat!");
        Utils.sendRawMessage(C.BLUE + "------------------------------------");
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    public static void mapCheck(String mapName) {
        if (mapName.equals("Dragonkeep")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Zombie, " + C.AQUA + "Skeleton, " + C.AQUA + "Spider, " + C.AQUA + "Enderman, " + C.AQUA + "Pigman, " + C.AQUA + "Creeper, " + C.AQUA + "Blaze, " + C.AQUA + "Dreadlord, " + C.AQUA + "Hunter, " + C.AQUA + "Moleman");
            Utils.sendMessage(C.AQUA + "8 diamonds: " + "4 top mid, 4 bot mid");
        } else if (mapName.equals("Egypt")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Enderman, " + C.AQUA + "Squid, " + C.AQUA + "Creeper, " + C.AQUA + "Pigman, " + C.AQUA + "Arcanist, " + C.AQUA + "Zombie, " + C.AQUA + "Dreadlord, " + C.AQUA + "Werewolf");
            Utils.sendMessage(C.AQUA + "6 diamonds and 4 swords: " + C.WHITE + "Diamonds top mid tower, 1 sword in each tunnel");
        } else if (mapName.equals("Forsaken")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Creeper, " + C.AQUA + "Golem, " + C.AQUA + "Herobrine, " + C.AQUA + "Zombie, " + C.AQUA + "Dreadlord, " + C.AQUA + "Spider, " + C.AQUA + "Enderman, " + C.AQUA + "Pirate, " + C.AQUA + "Pigman, " + C.AQUA + "Arcanist, " + C.AQUA + "Werewolf, " + C.AQUA + "Skeleton, " + C.AQUA + "Hunter");
            Utils.sendMessage(C.AQUA + "16 diamonds: " + C.WHITE + "All top mid");
        } else if (mapName.equals("Solace")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Skeleton, " + C.AQUA + "Blaze, " + C.AQUA + "Werewolf, " + C.AQUA + "Phoenix, " + C.AQUA + "Shaman, " + C.AQUA + "Spider, " + C.AQUA + "Zombie, " + C.AQUA + "Enderman, " + C.AQUA + "Pirate, " + C.AQUA + "Hunter, " + C.AQUA + "Dreadlord");
            Utils.sendMessage(C.AQUA + "19 diamonds and 1 sword: " + C.WHITE + "6 diamonds top floor mid, 5 dangling down top mid, 8 bottom mid and 1 sword");
        } else if (mapName.equals("Serpents")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Zombie, " + C.AQUA + "Golem, " + C.AQUA + "Enderman, " + C.AQUA + "Shaman, " + C.AQUA + "Creeper, " + C.AQUA + "Squid, " + C.AQUA + "Dreadlord, " + C.AQUA + "Herobrine, " + C.AQUA + "Pigman, " + C.AQUA + "Pirate");
            Utils.sendMessage(C.AQUA + "11 diamonds: " + C.WHITE + "3 very top mid, 2 in top floor of each mid building");
        } else if (mapName.equals("Kingdom")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Spider, " + C.AQUA + "Herobrine, " + C.AQUA + "Enderman, " + C.AQUA + "Dreadlord");
            Utils.sendMessage(C.AQUA + "7 diamonds: " + C.WHITE + "All top mid");
        } else if (mapName.equals("Mad Pixel")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Enderman, " + C.AQUA + "Hunter, " + C.AQUA + "Shaman, " + C.AQUA + "Werewolf, " + C.AQUA + "Herobrine, " + C.AQUA + "Golem");
            Utils.sendMessage(C.AQUA + "12 diamonds: " + C.WHITE + "All bottom mid");
        } else if (mapName.equals("Dynasty")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Moleman, " + C.AQUA + "Blaze, " + C.AQUA + "Enderman, " + C.AQUA + "Spider, " + C.AQUA + "Werewolf, " + C.AQUA + "Skeleton");
            Utils.sendMessage(C.AQUA + "13 diamonds and 1 sword: " + C.WHITE + "4 diamonds upper mid, 5 groud floor mid, 4 bottom mid");
        } else if (mapName.equals("Steppes")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Squid, " + C.AQUA + "Spider, " + C.AQUA + "Werewolf, " + C.AQUA + "Dreadlord, " + C.AQUA + "Pigman");
            Utils.sendMessage(C.AQUA + "12 diamonds: " + C.WHITE + "3 diamonds per side top mid.");
        } else if (mapName.equals("City")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Enderman, " + C.AQUA + "Spider, " + C.AQUA + "Shaman, " + C.AQUA + "Dreadlord, " + C.AQUA + "Blaze");
            Utils.sendMessage(C.AQUA + "13 diamonds: " + C.WHITE + "All in mid tower");
        } else if (mapName.equals("Barrage")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Zombie, " + C.AQUA + "Herobrine, " + C.AQUA + "Enderman, " + C.AQUA + "Squid, " + C.AQUA + "Spider, " + C.AQUA + "Creeper, " + C.AQUA + "Dreadlord");
            Utils.sendMessage(C.AQUA + "12 diamonds: " + C.WHITE + "2 in mid heli, 4 top mid building, 4 underground mid");
        } else if (mapName.equals("Kirobiro")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Moleman, " + C.AQUA + "Skeleton, " + C.AQUA + "Spider, " + C.AQUA + "Enderman");
            Utils.sendMessage(C.AQUA + "16 diamonds: " + C.WHITE + "8 ground floor mid, 4 mid tower, 4 top tower");
        } else if (mapName.equals("Launchsite")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Skeleton, " + C.AQUA + "Enderman, " + C.AQUA + "Spider, " + C.AQUA + "Zombie, " + C.AQUA + "Dreadlord, " + C.AQUA + "Werewolf, " + C.AQUA + "Blaze, " + C.AQUA + "Shaman, " + C.AQUA + "Phoenix, " + C.AQUA + "Pigman");
            Utils.sendMessage(C.AQUA + "20 diamonds: " + C.WHITE + "12 in the rocket on surface, 2 in the cargo of each teams tunnel");
        } else if (mapName.equals("Serenity")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Moleman, " + C.AQUA + "Skeleton, " + C.AQUA + "Spider, " + C.AQUA + "Enderman");
            Utils.sendMessage(C.AQUA + "20 diamonds and 1 sword: " + C.WHITE + "8 diamonds top mid building, 12 diamonds and sword underground mid");
        } else if (mapName.equals("Oasis")) {
            Utils.sendMessage(C.GREEN + "You are playing on " + mapName + "!");
            Utils.sendMessage(C.GREEN + "Recommended classes for this map: " + C.AQUA + "Creeper, " + C.AQUA + "Spider, " + C.AQUA + "Golem, " + C.AQUA + "Zombie, " + C.AQUA + "Phoenix, " + C.AQUA + "Dreadlord, " + C.AQUA + "Herobrine, " + C.AQUA + "Arcanist, " + C.AQUA + "Pigman");
            Utils.sendMessage(C.AQUA + "17 diamonds and 3 swords: " + C.WHITE + "17 bottom mid, 1 dsword upper floor of mid tower, 2 dswords in opposite corners of bottom mid");
        } else {
            Utils.sendMessage(C.RED + "You are not in a Mega Walls game right now!");
        }
    }
    @Override
    public List getCommandAliases() {
        aliases.add("Mw");
        aliases.add("mW");
        aliases.add("MW");
        return aliases;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}