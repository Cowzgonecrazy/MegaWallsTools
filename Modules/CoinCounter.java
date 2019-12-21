package cowzgonecrazy.megawallstools.Modules;

import cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig;
import cowzgonecrazy.megawallstools.MegaWallsTools;
import cowzgonecrazy.megawallstools.hudproperty.IRenderer;
import cowzgonecrazy.megawallstools.hudproperty.util.ScreenPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentText;
import cowzgonecrazy.megawallstools.Modules.C;

import java.io.*;

import static cowzgonecrazy.megawallstools.Modules.C.compileColor;

public class CoinCounter implements IRenderer {
    static Minecraft mc = Minecraft.getMinecraft();
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    public static double GuiX, GuiY = .5;
    public static int GuiXAbsolute, GuiYAbsolute;
    public static int totalCoins;
    public static String compiledCoins;

    public static void updateCoins() {
       if (MegaWallsToolsConfig.isCoinCounterEnabled) {
           compiledCoins = compileColor(MegaWallsToolsConfig.coinCounterDisplayColor) + "Coins: " + compileColor(MegaWallsToolsConfig.coinCounterCoinsColor) + Integer.toString(totalCoins);
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
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(compiledCoins);
    }
    @Override
    public void render(ScreenPosition position) {
        Double myDouble = position.getAbsoluteY()+getHeight()/1.7;
        Minecraft.getMinecraft().fontRendererObj.drawString(compiledCoins, position.getAbsoluteX(), myDouble.intValue(), 0xFFFFFF);
    }
    @Override
    public void renderDummy(ScreenPosition position) {
        Double myDouble = position.getAbsoluteY()+getHeight()/1.7;
        Minecraft.getMinecraft().fontRendererObj.drawString(compiledCoins, position.getAbsoluteX(), myDouble.intValue(), 0xFFFFFF);
    }

    public void SaveCoords(){
        try {
            File f = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + File.separator + "config", "MegaWallsToolsCoinCounter.cfg");
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(GuiX + ":" + GuiY + ":" + GuiXAbsolute + ":" + GuiYAbsolute);
            bw.close();
            fw.close();
        } catch (IOException e) {
            mc.thePlayer.addChatMessage(new ChatComponentText("IO Error, report this."));
            e.printStackTrace();
        }
    }

    public static void LoadCoords(){
        File f = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + File.separator + "config", "MegaWallsToolsCoinCounter.cfg");
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
            } catch (IOException e){
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                MegaWallsTools.debugString += "CoinCounter coords failed to load, resetting to prevent crash. Do /mw hud to fix";
                GuiX = 0;
                GuiY = 0;
                GuiXAbsolute = 0;
                GuiYAbsolute = 0;
            }
        }
    }
}
