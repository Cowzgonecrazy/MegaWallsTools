package cowzgonecrazy.megawallstools.Modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import cowzgonecrazy.megawallstools.Commands.CommandMWCooldowns;
import cowzgonecrazy.megawallstools.MegaWallsTools;
import com.google.common.collect.Lists;
import cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig;
import cowzgonecrazy.megawallstools.hudproperty.IRenderer;
import cowzgonecrazy.megawallstools.hudproperty.util.ScreenPosition;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;

import static cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig.hunterLevel;
import static cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig.phoenixLevel;

public class MWCooldowns implements IRenderer{
    static Minecraft mc = Minecraft.getMinecraft();
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    public static String version = "Mega Walls Tools v" + MegaWallsTools.VERSION;
    public static String cooldown = "\u00A7b" + version;
    public static String cooldown2 = "";
    public static String soundeffect = MegaWallsToolsConfig.mwcAlertSound;
    public static int hunterEffect;
    public static boolean ready = true, ready2 = true, skip=false, alerts=true, enabled=true, resurrected = false;
    public static double GuiX, GuiY = .5;
    public static int GuiXAbsolute, GuiYAbsolute;
    public static float volume = 1.0f, pitch = 1.0f;
    private static List<Float> hunter = Lists.newArrayList(0f, 60f, 56.25f, 52.5f, 48.75f, 45f, 41.25f, 37.5f, 33.75f, 30f);
    private static List<Float> phoenix = Lists.newArrayList(0f, 30f, 33.75f, 37.5f, 41.25f, 45f, 48.75f, 52.5f, 56.25f, 60f);

    public void save(ScreenPosition position) {
        GuiX = position.getRelativeX();
        GuiY = position.getRelativeY();
        GuiXAbsolute = position.getAbsoluteX();
        GuiYAbsolute = position.getAbsoluteY();
        SaveCoords();
    }
    /**
     * Plays notification sound
     */
    public static void playSounds(){
        try {
            if(MegaWallsToolsConfig.isMWCAlertsEnabled && MegaWallsToolsConfig.isMWCooldownsEnabled){
                mc.thePlayer.playSound(CommandMWCooldowns.sounds[Arrays.asList(CommandMWCooldowns.sounds).indexOf(soundeffect)], MWCooldowns.volume, MWCooldowns.pitch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Loads up the position of the GUI
     */
    @Override
    public ScreenPosition load() {
        return ScreenPosition.fromAbsolutePosition(GuiXAbsolute, GuiYAbsolute);
    }
    /**
     * @Author orangemarshall
     * Gets height of GUI text
     */
    public int getHeight() {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT*2;
    }
    @Override
    public int getWidth() {
        return Math.max(Minecraft.getMinecraft().fontRendererObj.getStringWidth(cooldown),Minecraft.getMinecraft().fontRendererObj.getStringWidth(cooldown));
    }

    @Override
    public void render(ScreenPosition position) {
        Double myDouble = position.getAbsoluteY()+getHeight()/1.7;
        Minecraft.getMinecraft().fontRendererObj.drawString(cooldown2, position.getAbsoluteX(), myDouble.intValue(), 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString(cooldown, position.getAbsoluteX(), position.getAbsoluteY(), 0xFFFFFF);
    }

    @Override
    public void renderDummy(ScreenPosition position) {
        Double myDouble = position.getAbsoluteY()+getHeight()/1.7;
        Minecraft.getMinecraft().fontRendererObj.drawString(cooldown2, position.getAbsoluteX(), myDouble.intValue(), 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString(cooldown, position.getAbsoluteX(), position.getAbsoluteY(), 0xFFFFFF);
    }

    public static void updateDisplayOne(String c1) {
        if (MegaWallsToolsConfig.isMWCooldownsEnabled) {
            cooldown = c1;
        } else {
            cooldown = "";
            cooldown2 = "";
        }
    }

    public static void updateDisplayTwo(String c2) {
        if (MegaWallsToolsConfig.isMWCooldownsEnabled) {
            cooldown2 = c2;
        } else {
            cooldown = "";
            cooldown2 = "";
        }
    }

    public static void runPhoenix(){
        try {
            for(int i=20; i>0; i--){
                updateDisplayOne(C.RED + "Inferno: " + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.RED + "Inferno: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runPigman(){
        try {
            for(int i=30; i>0; i--){
                updateDisplayOne(C.DARK_PURPLE + "Endurance: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.DARK_PURPLE + "Endurance: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runSkeleton(){
        try {
            for(int i=10; i>0; i--){
                updateDisplayOne(C.AQUA + "Agile: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.AQUA + "Agile: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runPirate(){
        try {
            for(int i=30; i>0; i--){
                updateDisplayOne(C.AQUA + "Sea Legs: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.AQUA + "Sea Legs: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runCreeper(){
        try {
            for(int i=20; i>0; i--){
                updateDisplayOne(C.AQUA + "Willpower: "+ C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.AQUA + "Willpower: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runSquid(){
        try {
            for(int i=50; i>0; i--){
                updateDisplayOne(C.GREEN + "Rejuvenate: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.GREEN + "Rejuvenate: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runBlaze(){
        try {
            updateDisplayOne(C.GOLD + "Fossil Fuels: " + C.RED + C.BOLD + "ACTIVE");
            Thread.sleep(5000);
            for (int i=10; i>0; i--){
                updateDisplayOne(C.GOLD + "Fossil Fuels: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.RED + "Fossil Fuels: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runGolem(){
        try {
            for(int i=20; i>0; i--) {
                updateDisplayOne(C.YELLOW + "Iron Heart: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.YELLOW + "Iron Heart: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // new hunter code
    // First version before Super's testing, leave 5s delay in
    public static void runHunter(){
        try {
            for (int i = 5; i > 0; i--) {
                updateDisplayOne(C.GREEN + "Force of Nature: " + C.RED + "Wait " + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }

            for (int i = hunterEffect; i > 0; i--) {
                updateDisplayOne(C.GREEN + "Force of Nature: " + C.RED + "Active " + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }

            for (double i = (hunter.get(hunterLevel) - hunterEffect - 5); i > 0.0; i-= .25) {
                updateDisplayOne(C.GREEN + "Force of Nature: " + C.RED + Double.toString(i) + "s");
                Thread.sleep(250);
            }
            updateDisplayOne(C.GREEN + "Force of Nature: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runHunterDuels() {
        try {
            for (int i = hunterEffect; i > 0; i--) {
                updateDisplayOne(C.GREEN + "Force of Nature: " + C.RED + "Active " + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }

            for (double i = (hunter.get(hunterLevel) - hunterEffect); i > 0.0; i-= .25) {
                updateDisplayOne(C.GREEN + "Force of Nature: " + C.RED + Double.toString(i) + "s");
                Thread.sleep(250);
            }
            playSounds();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runEnderman(){
        try {
            for(int i=6; i>0; i--){
                updateDisplayOne(C.DARK_PURPLE + "Teleport: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.DARK_PURPLE + "Teleport: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runEnderman2(){
        try {
            for (int i=10; i>0; i--){
                updateDisplayTwo(C.LIGHT_PURPLE + "Soul Charge: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayTwo(C.LIGHT_PURPLE + "Soul Charge: " + C.GREEN + C.BOLD + "READY");
            ready2 = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runWerewolf(){
        try {
            for (int i=6; i>0; i--){
                updateDisplayOne(C.RED + "Devour: " + C.RED + Integer.toString(i) + "s");
                Thread.sleep(1000);
            }
            updateDisplayOne(C.RED + "Devour: " + C.GREEN + C.BOLD + "READY");
            ready = true;
            playSounds();
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runPhoenix2() {
        try {
            for (double i = (phoenix.get(phoenixLevel)); i > 0.0; i -= 0.25) {
                if (!resurrected) {
                    updateDisplayTwo(C.RED + "Resurrect: " + Double.toString(i));
                    Thread.sleep(250);
                } else {
                    break;
                }
            }
            cooldown2 = "";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void SaveCoords(){
        try {
            File f = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + File.separator + "config", "MWCooldowns.cfg");
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(GuiX + ":" + GuiY + ":" + GuiXAbsolute + ":" + GuiYAbsolute + ":" + soundeffect + ":" + volume + ":" + pitch);
            bw.close();
            fw.close();
        } catch (IOException e) {
            mc.thePlayer.addChatMessage(new ChatComponentText("IO Error, report this."));
            e.printStackTrace();
        }
    }

    public static void LoadCoords(){
        File f = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + File.separator + "config", "MWCooldowns.cfg");
        if(f.exists()) {
            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String[] tmp = br.readLine().split(":");
                GuiX = Double.valueOf(tmp[0]);
                GuiY = Double.valueOf(tmp[1]);
                GuiXAbsolute = Integer.valueOf(tmp[2]);
                GuiYAbsolute = Integer.valueOf(tmp[3]);
                soundeffect = tmp[4];
                volume = Float.valueOf(tmp[5]);
                pitch = Float.valueOf(tmp[6]);
                br.close();
                fr.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void onPotionAdded(PotionEffect potioneffect) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (MegaWallsToolsConfig.isMWCooldownsEnabled) {
            if (MegaWallsTools.MWClass.equals("golem") && ready && potioneffect.getDuration() < 300 && potioneffect.getEffectName().equals("potion.absorption") && potioneffect.getAmplifier() == 1){
                ready = false;
                new Thread(() -> runGolem()).start();
            } else if(MegaWallsTools.MWClass.equals("squid") && ready && potioneffect.getDuration() < 30 && potioneffect.getEffectName().equals("potion.regeneration") && potioneffect.getAmplifier() > 5){
                ready = false;
                new Thread(() -> runSquid()).start();
            } else if(MegaWallsTools.MWClass.equals("pigman") && mc.thePlayer.getHealth()< 15 && ready && potioneffect.getDuration() < 150 && potioneffect.getEffectName().equals("potion.resistance") && potioneffect.getAmplifier() == 1){
                ready = false;
                new Thread(() -> runPigman()).start();
            } else if(MegaWallsTools.MWClass.equals("blaze") && ready && potioneffect.getDuration() < 130 && potioneffect.getEffectName().equals("potion.regeneration") && potioneffect.getAmplifier() == 1){
                ready = false;
                new Thread(() -> runBlaze()).start();
            } else if(MegaWallsTools.MWClass.equals("creeper") && ready && potioneffect.getDuration() < 300 && potioneffect.getEffectName().equals("potion.moveSpeed") && potioneffect.getAmplifier() == 2){
                ready = false;
                new Thread(() -> runCreeper()).start();
            } else if(MegaWallsTools.MWClass.equals("pirate") && ready && potioneffect.getDuration() > 200 && potioneffect.getDuration() < 310 && potioneffect.getEffectName().equals("potion.moveSpeed") && potioneffect.getAmplifier() == 1){
                ready = false;
                new Thread(() -> runPirate()).start();
            } else if(MegaWallsTools.MWClass.equals("skeleton") && ready && potioneffect.getDuration() < 150 && potioneffect.getEffectName().equals("potion.moveSpeed") && potioneffect.getAmplifier() == 1){
                ready = false;
                new Thread(() -> runSkeleton()).start();
            } else if(MegaWallsTools.MWClass.equals("enderman") && ready && potioneffect.getDuration() < 150 && potioneffect.getEffectName().equals("potion.moveSpeed") && potioneffect.getAmplifier() < 4){
                ready = false;
                new Thread(() -> runEnderman()).start();
            } else if(MegaWallsTools.MWClass.equals("enderman") && ready2 && potioneffect.getEffectName().equals("potion.regeneration") && potioneffect.getAmplifier() == 1){
                ready2 = false;
                new Thread(() -> runEnderman2()).start();
            } else if(MegaWallsTools.MWClass.equals("werewolf") && ready && potioneffect.getDuration() < 150 && potioneffect.getEffectName().equals("potion.regeneration") && potioneffect.getAmplifier() < 1){
                ready = false;
                new Thread(() -> runWerewolf()).start();
            }
        }
    }
}
