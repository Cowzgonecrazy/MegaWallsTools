package cowzgonecrazy.megawallstools;

import cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig;
import cowzgonecrazy.megawallstools.Modules.*;
import cowzgonecrazy.megawallstools.Commands.CommandMWCooldowns;
import cowzgonecrazy.megawallstools.Commands.CommandRandomClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.event.ClickEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.net.URL;
import java.util.*;

import cowzgonecrazy.megawallstools.hudproperty.HudPropertyApi;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import static cowzgonecrazy.megawallstools.Modules.KillCounter.*;
import static cowzgonecrazy.megawallstools.Modules.MWCooldowns.updateDisplayOne;
import static cowzgonecrazy.megawallstools.Modules.MWCooldowns.updateDisplayTwo;

@Mod(modid = MegaWallsTools.MODID, name = MegaWallsTools.NAME, version = MegaWallsTools.VERSION, clientSideOnly = true, guiFactory = "cowzgonecrazy.megawallstools.Config.MegaWallsToolsGuiFactory")
public class MegaWallsTools  {
    public static final String MODID = "mwtools";
    public static final String NAME = "Mega Walls Tools";
    public static final String VERSION = "1.1.0";
    public static final String GUIFACTORY = "cowzgonecrazy.megawallstools.Config.MegaWallsToolsGuiFactory";

    @Mod.Instance("mwtools")
    public static MegaWallsTools instance;

    public static ArrayList<PotionEffect> cache_potions = new ArrayList<>();
    static Minecraft mc = Minecraft.getMinecraft();
    public static String MWClass = "";
    public static boolean regularMWGame;
    public static String debugString = "";
    public static ArrayList<String> UUIDs;



    @SideOnly(Side.CLIENT)
    @EventHandler
    public void preinit(FMLPreInitializationEvent e)
    {
        MegaWallsToolsConfig.config = new Configuration(e.getSuggestedConfigurationFile());
        MegaWallsToolsConfig.syncConfig();
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void onInit(FMLInitializationEvent event){
        MWCooldowns.LoadCoords();
        KillCounter.LoadCoords();
        CoinCounter.LoadCoords();
        HudPropertyApi api = HudPropertyApi.newInstance();
        api.register(new MWCooldowns());
        api.register(new KillCounter());
        api.register(new CoinCounter());

        ClientCommandHandler.instance.registerCommand(new CommandMWCooldowns(api));
        ClientCommandHandler.instance.registerCommand(new CommandRandomClass());
    }

    @SideOnly(Side.CLIENT)
    @EventHandler
    public void init(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new WitherWarning());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTick(ClientTickEvent event){
        if(MWCooldowns.skip){
            MWCooldowns.skip = false;
        }else if(MWCooldowns.enabled && MWClass != ""){
            CheckPotions();
        }
    }

    /**
     * Chat triggers to tell the mod to check your class when you join and leave games so you dont have to do it manually with /mw update
     * @param event Chat event, when chat is about to be received
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event){
        String me = event.message.getUnformattedText();
        if(MWCooldowns.enabled){
            if(me.contains("Prepare your defenses!") || me.contains("You logged back in!")){
                regularMWGame = true;
                new Thread(() -> UpdateClass()).start();
                resetCounters();
            } else if(me.contains("Reward Summary") || (me.contains("Best MegaWalls") && me.contains("Streak")) || me.contains("Sending you to mini") || (me.contains(" has joined ") && me.endsWith("!"))){
                resetCounters();
            } else if(me.contains("This Duels mode will not count towards MegaWalls stats")) {
                regularMWGame = false;
                resetCounters();
                new Thread(() -> UpdateClass()).start();
            } else if(me.contains("Your Force of Nature is going") && MWCooldowns.ready && MWClass.equals("hunter")){
                String[] splitMessage = me.split("second");
                me = splitMessage[0].replace("Your Force of Nature is going to give you a ", "");
                me = me.replace(" ", "");
                MWCooldowns.hunterEffect = Integer.parseInt(me);
                if (regularMWGame) {
                    new Thread(() -> MWCooldowns.runHunter()).start();
                } else {
                    regularMWGame = true;
                    new Thread(() -> MWCooldowns.runHunterDuels()).start();
                }

            } else if((me.contains("Your Inferno healed") || me.contains("You were healed by")) && MWCooldowns.ready && MWClass == "phoenix"){
                MWCooldowns.ready = false;
                new Thread(() -> MWCooldowns.runPhoenix()).start();
            } else if (me.contains("Your wither has died. You can no longer respawn!") && MWClass.equals("phoenix")) {
                new Thread(() -> MWCooldowns.runPhoenix2()).start();
            }
        }

        // listen for KillCounter
        if (me.contains("/18 Assists")) {
            Assists++;
            KillCounter.updateCounter();
        } else if (me.contains("/18 Kills")) {
            Kills++;
            KillCounter.updateCounter();
        } else if (me.contains("FINAL KILL") && !me.endsWith("!")) {
            if (me.contains("ASSIST")) {
                FinalAssists++;
                KillCounter.updateCounter();
            } else {
                FinalKills++;
                KillCounter.updateCounter();
            }
        }

        if (me.startsWith("+") && me.contains("coins") && !(me.contains("for being generous"))) {
            String[] splitMessage = me.split("coins");
            me = splitMessage[0].replace("+", "");
            me = me.replace(" ", "");
            int coins = Integer.parseInt(me);
            CoinCounter.totalCoins += coins;
        } else if (me.startsWith("Total") && me.contains("Earned")) {
            CoinCounter.totalCoins = 0;
        } else if (me.contains("Teleporting") && me.contains("3")) {
            CoinCounter.totalCoins = 0;
        } else if (me.startsWith("Total") && me.contains("Earned")) {
            CoinCounter.totalCoins = 0;
        } else if (me.contains("has joined") && me.endsWith("!")) {
            CoinCounter.totalCoins = 0;
        }

        if (MegaWallsToolsConfig.isCoinCounterEnabled) {
            CoinCounter.updateCoins();
        }


        if (me.contains("You are currently playing on ") && CommandMWCooldowns.requestedMap) {
            event.setCanceled(true);
            String mapString = me.replace("You are currently playing on ", "");
            CommandMWCooldowns.mapCheck(mapString);
            CommandMWCooldowns.requestedMap = false;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals("mwtools")) {
            forcedOnConfigChanged();
        }
    }

    public static void forcedOnConfigChanged() {
        MegaWallsToolsConfig.syncConfig();

        if (!MegaWallsToolsConfig.isKillCounterEnabled) {
            KillCounter.compiledKA = "";
            KillCounter.compiledFKA = "";
        }

        if (MegaWallsToolsConfig.isCoinCounterEnabled) {
            CoinCounter.updateCoins();
        } else {
            CoinCounter.compiledCoins = "";
        }

        if (!MegaWallsToolsConfig.isMWCooldownsEnabled) {
            updateDisplayOne("");
            updateDisplayTwo("");
        }

        MWCooldowns.soundeffect = MegaWallsToolsConfig.mwcAlertSound;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void teamFinalsCounter(ClientChatReceivedEvent event) {
        /* examples of raw kill messages
        §r§cUniversalPull§r§f §r§fwas hugged too hard by §r§9yungnull§r
        §r§eWriggle§r§f §r§fwas killed by §r§clited§r
        §r§azukiu§r§f §r§fwas sliced up by §r§eVinnila§r

        */
        String me = event.message.getFormattedText();


    }

    // Send message when player joins hypixel
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {

        //EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        //player.addChatComponentMessage(new ChatComponentText("Do /mw for help!"));
        //Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Do /mw for help!"));

        Minecraft.getMinecraft().addScheduledTask(() -> {
            ServerData data = Minecraft.getMinecraft().getCurrentServerData();
            if (data != null && data.serverIP != null && !event.isLocal) {
                System.out.println("Connecting to server: " + data.serverIP);
                if (data.serverIP.equalsIgnoreCase("mc.hypixel.net")) {
                    System.out.println("Joined 'Hypixel'");
                    resetCounters();
                    //sendWelcomeMessage(1000);
                    checkForUpdate(1000);
                } else {
                    checkForUpdate(1000);
                }
            }
        });
    }

    public void sendWelcomeMessage(int delay) {
        if(delay > 2500){
            System.out.println("Could not send welcome message due to timeout!");
            return;
        }
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                Utils.sendMessage(C.AQUA + VERSION + " loaded in! Do /mw for help!");
                Utils.sendMessage(C.AQUA + "Found a bug? Report it in my discord: ");
                ChatComponentText t = new ChatComponentText(C.GREEN + "[" + C.WHITE + "MWTOOLS" + C.GREEN + "] " + " https://discord.gg/bdZ5JUe");
                t.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/bdZ5JUe"));
                Minecraft.getMinecraft().thePlayer.addChatMessage(t);
                if (!debugString.equals("")) {
                    Utils.sendMessage(C.DARK_RED + debugString);
                }
                checkUUID();
            } catch (InterruptedException e1) {
                System.out.println("Error occured! Trying update delay of " + delay*2 + " now...");
                sendWelcomeMessage(delay*2);
                e1.printStackTrace();
            }
        }
        ).start();
    }

    public void checkUUID() {
        String playerUUID = Minecraft.getMinecraft().getSession().getPlayerID().replace("-", "");
        Utils.getBlacklistedUUIDs();
        for (String s: UUIDs) {
            if (s.equals(playerUUID)) {
                try {
                    Thread.sleep(5000);
                    for (int i = 5; i > 0; i--) {
                        Utils.sendRawMessage("You are blacklisted from MWTools! " + Integer.toString(i) + "s");
                        Thread.sleep(1000);
                    }
                    Minecraft.getMinecraft().shutdown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setUUIDs(final ArrayList uuids) {
        UUIDs = (ArrayList<String>)uuids;
    }

    public void checkForUpdate(int delay) {
        if(delay > 2500){
            System.out.println("Could not send welcome message due to timeout!");
            return;
        }
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                checkUUID();
                Utils.sendMessage(C.AQUA + VERSION + " loaded in! Do /mw for help!");
                Utils.sendMessage(C.AQUA + "Found a bug? Report it in my discord: ");
                ChatComponentText t = new ChatComponentText(C.GREEN + "[" + C.WHITE + "MWTOOLS" + C.GREEN + "] " + " https://discord.gg/bdZ5JUe");
                t.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/bdZ5JUe"));
                Minecraft.getMinecraft().thePlayer.addChatMessage(t);
                if (!debugString.equals("")) {
                    Utils.sendMessage(C.DARK_RED + debugString);
                }

                final String rawLatestVersion = IOUtils.toString(new URL("https://gist.github.com/Cowzgonecrazy/7308cbe7745dc3e16f72d96cffb33e1b/raw"));
                System.out.println("Active version: " + VERSION);
                System.out.println("Latest version: " + rawLatestVersion);
                final String latestVersionDownload = IOUtils.toString(new URL("https://gist.github.com/Cowzgonecrazy/15a588380f293f308187a8d57a3ce488/raw"));

                String versionString = VERSION.replace(".", "");
                String latestverion = rawLatestVersion.replace(".", "");

                System.out.println("Active version w/o periods: " + versionString);
                System.out.println("Latest version w/o periods: " + latestverion);

                int lversion = Integer.valueOf(latestverion); // latest version
                int cversion = Integer.valueOf(versionString); // current version
                System.out.println("Comparing " + lversion + " to " + cversion);
                if (cversion < lversion) {
                    System.out.println("MWTools is out of date!");
                    Utils.sendMessage("Outdated version! Please update the mod!");
                    ChatComponentText updateLink = new ChatComponentText(C.GREEN + "[" + C.WHITE + "MWTOOLS" + C.GREEN + "] " + "v" + rawLatestVersion);
                    updateLink.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, latestVersionDownload));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(updateLink);
                }

            } catch (InterruptedException e1) {
                System.out.println("Error occured! Trying update delay of " + delay*2 + " now...");
                sendWelcomeMessage(delay*2);
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ).start();
    }
    /**
     * @Author Simisage/TheDestinyPig/Kevin - thank you for this
     * Checks for any NEW potions added
     */
    public static void CheckPotions(){
        if (Minecraft.getMinecraft().thePlayer != null && !Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty()){
            Collection<PotionEffect> collection = Minecraft.getMinecraft().thePlayer.getActivePotionEffects();
            if (!collection.isEmpty()) {
                for (PotionEffect potioneffect : Minecraft.getMinecraft().thePlayer.getActivePotionEffects()) {
                    if(!cache_potions.contains(potioneffect)) {
                        MWCooldowns.onPotionAdded(potioneffect);
                        cache_potions.add(potioneffect);
                    }
                }
            }
        }
    }

    // Check what mw class you are using
    public static void UpdateClass() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        InventoryPlayer inventory = Minecraft.getMinecraft().thePlayer.inventory;
        MWClass = "";
        MWCooldowns.cooldown = "";
        MWCooldowns.cooldown2 = "";
        boolean found = true;
        boolean done = false;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack slotItem = inventory.getStackInSlot(i);
            if (slotItem != null && slotItem.getDisplayName().contains("Enderman")){
                MWClass = "enderman";
                updateDisplayOne(C.BLUE + "Teleport: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo(C.LIGHT_PURPLE + "Soul Charge: " + C.GREEN + C.BOLD + "READY");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Enderman Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Golem")){
                MWClass = "golem";
                updateDisplayOne(C.YELLOW + "Iron Heart: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Golem Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Squid")){
                MWClass = "squid";
                updateDisplayOne(C.GREEN + "Rejuvenate: " + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Squid Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Blaze")){
                MWClass = "blaze";
                updateDisplayOne(C.GOLD + "Fossil Fuels: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Blaze Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Creeper")){
                MWClass = "creeper";
                updateDisplayOne(C.DARK_GREEN + "Willpower: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Creeper Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Pigman")){
                MWClass = "pigman";
                updateDisplayOne(C.DARK_PURPLE + "Endurance: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Pigman Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Pirate")){
                MWClass = "pirate";
                updateDisplayOne(C.AQUA + "Sea Legs: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Pirate Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Skeleton")){
                MWClass = "skeleton";
                updateDisplayOne(C.AQUA + "Agile: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Skeleton Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Phoenix")){
                MWClass = "phoenix";
                updateDisplayOne(C.RED + "Inferno: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Phoenix Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Hunter")){
                MWClass = "hunter";
                updateDisplayOne(C.GREEN + "Force of Nature: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Hunter Detected");
                done = true;
                break;
            } else if(slotItem != null && slotItem.getDisplayName().contains("Werewolf")){
                MWClass = "werewolf";
                updateDisplayOne(C.RED + "Devour: " + C.GREEN + C.BOLD + "READY");
                updateDisplayTwo("");
                Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "Werewolf Detected");
                done = true;
                break;
            }
        }
        if (!done && found){
            MWClass = "";
            MWCooldowns.cooldown = "";
            MWCooldowns.cooldown2 = "";
            Utils.sendRawMessage(C.GRAY + "[" + C.AQUA +"MWCooldowns" + C.GRAY + "] " + C.GREEN + "No Supported Class Detected");
            found = false;
        }
        Thread.yield();
    }

    private void resetCounters() {
        KillCounter.Kills = 0;
        KillCounter.Assists = 0;
        KillCounter.FinalKills = 0;
        KillCounter.FinalAssists = 0;
        KillCounter.compiledKA = "";
        KillCounter.compiledFKA = "";

        CoinCounter.totalCoins = 0;
        if (MegaWallsToolsConfig.isCoinCounterEnabled) {
            CoinCounter.updateCoins();
        }

        MWClass = "";
        MWCooldowns.ready = true;
        MWCooldowns.cooldown = "";
        MWCooldowns.cooldown2 = "";
    }
}
