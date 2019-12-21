package cowzgonecrazy.megawallstools.Modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import cowzgonecrazy.megawallstools.MegaWallsTools;
import com.google.common.collect.Lists;
import cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig;
import cowzgonecrazy.megawallstools.hudproperty.HudPropertyApi;
import cowzgonecrazy.megawallstools.hudproperty.IRenderer;
import cowzgonecrazy.megawallstools.hudproperty.util.ScreenPosition;
import cowzgonecrazy.megawallstools.Modules.C;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import static cowzgonecrazy.megawallstools.Modules.C.compileColor;


public class KillCounter implements IRenderer {
    static Minecraft mc = Minecraft.getMinecraft();
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    public static double GuiX, GuiY = .5;
    public static int GuiXAbsolute, GuiYAbsolute;
    public static int Kills;
    public static int Assists;
    public static String compiledKA = "";
    public static int FinalKills;
    public static int FinalAssists;
    public static String compiledFKA = "";


    public static void updateCounter() {
        if (MegaWallsToolsConfig.isKillCounterEnabled) {
            compiledKA  = compileColor(MegaWallsToolsConfig.killCounterSelectedColor) + C.BOLD + "K/A: " + Integer.toString(Kills) + "/" + Integer.toString(Assists);
            compiledFKA = compileColor(MegaWallsToolsConfig.killCounterSelectedColor) + C.BOLD + "FK/FA: " + Integer.toString(FinalKills) + "/" + Integer.toString(FinalAssists);
        }
    }

    public void save(ScreenPosition position) {
        GuiX = position.getRelativeX();
        GuiY = position.getRelativeY();
        GuiXAbsolute = position.getAbsoluteX();
        GuiYAbsolute = position.getAbsoluteY();
        SaveCoords();
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
        return Math.max(Minecraft.getMinecraft().fontRendererObj.getStringWidth(compiledFKA), Minecraft.getMinecraft().fontRendererObj.getStringWidth(compiledKA));
    }
    @Override
    public void render(ScreenPosition position) {
        Double myDouble = position.getAbsoluteY()+getHeight()/1.7;
        Minecraft.getMinecraft().fontRendererObj.drawString(compiledFKA, position.getAbsoluteX(), myDouble.intValue(), 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString(compiledKA, position.getAbsoluteX(), position.getAbsoluteY(), 0xFFFFFF);
    }
    @Override
    public void renderDummy(ScreenPosition position) {
        Double myDouble = position.getAbsoluteY()+getHeight()/1.7;
        Minecraft.getMinecraft().fontRendererObj.drawString(compiledFKA, position.getAbsoluteX(), myDouble.intValue(), 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString(compiledKA, position.getAbsoluteX(), position.getAbsoluteY(), 0xFFFFFF);
    }

    public void SaveCoords(){
        try {
            File f = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + File.separator + "config", "MegaWallsToolsKillCounter.cfg");
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(GuiX + ":" + GuiY + ":" + GuiXAbsolute + ":" + GuiYAbsolute);
            bw.close();
            fw.close();
        } catch (IOException e) {
            mc.thePlayer.addChatMessage(new ChatComponentText("IO Error, report this."));
        }
    }

    public static void LoadCoords(){
        File f = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + File.separator + "config", "MegaWallsToolsKillCounter.cfg");
        if(f.exists()) {
            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String[] tmp = br.readLine().split(":");
                GuiX = Double.valueOf(tmp[0]);
                GuiY = Double.valueOf(tmp[1]);
                GuiXAbsolute = Integer.valueOf(tmp[2]);
                GuiYAbsolute = Integer.valueOf(tmp[3]);
                br.close();
                fr.close();
            }catch (IOException e){
            }
        }
    }
}
