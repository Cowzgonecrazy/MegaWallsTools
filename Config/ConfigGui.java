package cowzgonecrazy.megawallstools.Config;

import cowzgonecrazy.megawallstools.MegaWallsTools;
import cowzgonecrazy.megawallstools.Modules.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.util.Arrays;

import static cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig.CATEGORY_MWCOOLDOWNS;
import static cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig.possibleColors;
import static cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig.possibleSounds;
import static cowzgonecrazy.megawallstools.Modules.C.compileColor;
import static cowzgonecrazy.megawallstools.Modules.C.compileHexColor;

public class ConfigGui extends GuiScreen {
    private GuiButton ButtonClose;

    private GuiButton ButtonToggleMWCooldowns;
    private GuiButton ButtonToggleCoinCounter;
    private GuiButton ButtonCoinCounterColor;
    private GuiButton ButtonCoinsColor;
    private GuiButton ButtonToggleKillCounter;
    private GuiButton ButtonKillsColor;
    private GuiButton ButtonToggleWitherWarning;
    private GuiSlider ButtonSetWitherWarning;
    private GuiButton ButtonToggleMWCAlerts;
    private GuiButton ButtonSetAlertsSound;
    private GuiSlider ButtonSetHunterLevel;
    private GuiSlider ButtonSetPhoenixLevel;

    private Property property;
    private boolean fakeDefault;

    ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
    @Override
    public void initGui() {
        super.initGui();
        loadButtons();
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == ButtonClose) {
            mc.thePlayer.closeScreen();
        } else if (button == ButtonToggleMWCooldowns) {
            property = MegaWallsToolsConfig.config.get("mwcooldowns", "MWCooldowns", true);
            if (MegaWallsToolsConfig.isMWCooldownsEnabled) {
                property.set(false);
            } else {
                property.set(true);
            }
        } else if (button == ButtonToggleCoinCounter) {
            property = MegaWallsToolsConfig.config.get("general", "Coin Counter", true);
            if (MegaWallsToolsConfig.isCoinCounterEnabled) {
                property.set(false);
            } else {
                property.set(true);
            }
        } else if (button == ButtonCoinCounterColor) {
            property = MegaWallsToolsConfig.config.get("general", "Coins: Color", "Green");
            String currentColor = property.getString();
            int currentIndex = Arrays.asList(possibleColors).indexOf(currentColor);
            if (currentIndex == 15) {
                property.set(possibleColors[0]);
            } else {
                property.set(possibleColors[currentIndex + 1]);
            }
            CoinCounter.updateCoins();
        } else if (button == ButtonCoinsColor) {
            property = MegaWallsToolsConfig.config.get("general", "# of Coins Color", "White");
            String currentColor = property.getString();
            int currentIndex = Arrays.asList(possibleColors).indexOf(currentColor);
            if (currentIndex == 15) {
                property.set(possibleColors[0]);
            } else {
                property.set(possibleColors[currentIndex + 1]);
            }
        } else if (button == ButtonToggleKillCounter) {
            property = MegaWallsToolsConfig.config.get("general", "Kill Counter", true);
            if (MegaWallsToolsConfig.isKillCounterEnabled) {
                property.set(false);
            } else {
                property.set(true);
            }
        } else if (button == ButtonKillsColor) {
            property = MegaWallsToolsConfig.config.get("general", "Kill Counter Color", "White");
            String currentColor = property.getString();
            int currentIndex = Arrays.asList(possibleColors).indexOf(currentColor);
            if (currentIndex == 15) {
                property.set(possibleColors[0]);
            } else {
                property.set(possibleColors[currentIndex + 1]);
            }
        } else if (button == ButtonToggleWitherWarning) {
            property = MegaWallsToolsConfig.config.get("general", "Wither Warning", true);
            if (MegaWallsToolsConfig.isWitherWarningEnabled) {
                property.set(false);
            } else {
                property.set(true);
            }
        } else if (button == ButtonSetWitherWarning) {
            property = MegaWallsToolsConfig.config.get("general", "Wither Warning Value", 50);
            property.set(ButtonSetWitherWarning.getValueInt());
        } else if (button == ButtonToggleMWCAlerts) {
            property = MegaWallsToolsConfig.config.get("mwcooldowns", "MWCooldownsAlerts", true);
            if (MegaWallsToolsConfig.isMWCAlertsEnabled) {
                property.set(false);
            } else {
                property.set(true);
            }
        } else if (button == ButtonSetAlertsSound) {
            property = MegaWallsToolsConfig.config.get("mwcooldowns", "MWCAlerts Sound", "ding");
            int currentIndex = Arrays.asList(possibleSounds).indexOf(property.getString());
            if (currentIndex == 6) {
                property.set(possibleSounds[0]);
            } else {
                property.set(possibleSounds[currentIndex + 1]);
            }
            MWCooldowns.playSounds();
        } else if (button == ButtonSetHunterLevel) {
            property = MegaWallsToolsConfig.config.get("mwcooldowns", "Hunter Level", 9);
            property.set(ButtonSetHunterLevel.getValueInt());
        } else if (button == ButtonSetPhoenixLevel) {
            property = MegaWallsToolsConfig.config.get("mwcooldowns", "Phoenix Level", 9);
            property.set(ButtonSetPhoenixLevel.getValueInt());
        }

        MegaWallsToolsConfig.syncConfig();
        MegaWallsToolsConfig.config.save();
        MegaWallsTools.forcedOnConfigChanged();
        loadButtons();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, C.AQUA + "Mega Walls Tools v" + MegaWallsTools.VERSION, this.width / 2, this.height - 20 * (this.height / 20) + 10, -1 );
        drawCenteredString(fontRendererObj,compileColor(MegaWallsToolsConfig.coinCounterDisplayColor) + "Coins: " + compileColor(MegaWallsToolsConfig.coinCounterCoinsColor) + "1337", this.width / 2, this.height - 18 * (this.height / 20) + 7, -1);
        drawCenteredString(fontRendererObj,C.BOLD + compileColor(MegaWallsToolsConfig.killCounterSelectedColor) + "K/A: 10/10", this.width / 2 + 5, this.height - 15 * (this.height / 20) + 7, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void loadButtons() {
        buttonList.clear();

        this.buttonList.add(ButtonClose = new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 8) + 10, "Close"));

        if (MegaWallsToolsConfig.isMWCooldownsEnabled) {
            this.buttonList.add(ButtonToggleMWCooldowns = new GuiButton(1, (this.width / 3) * 2 - 100, this.height - 19 * (this.height / 20), "MWCooldowns: ON"));
        } else {
            this.buttonList.add(ButtonToggleMWCooldowns = new GuiButton(1, (this.width / 3) * 2 - 100, this.height - 19 *(this.height / 20), "MWCooldowns: OFF"));
        }

        if (MegaWallsToolsConfig.isCoinCounterEnabled) {
            this.buttonList.add(ButtonToggleCoinCounter = new GuiButton(2, (this.width / 3) - 100, this.height - 19 * (this.height / 20), "Coin Counter: ON"));
        } else {
            this.buttonList.add(ButtonToggleCoinCounter = new GuiButton(2, (this.width / 3) - 100, this.height - 19 * (this.height / 20), "Coin Counter: OFF"));
        }

        this.buttonList.add(ButtonCoinCounterColor = new GuiButton(3, (this.width / 3) - 100, this.height - 18 * (this.height / 20), "Rotate \"Coin\" Color"));

        this.buttonList.add(ButtonCoinsColor = new GuiButton(4, (this.width / 3) - 100, this.height - 17 * (this.height / 20), "Rotate \"# of coins\" Color"));

        if (MegaWallsToolsConfig.isKillCounterEnabled) {
            this.buttonList.add(ButtonToggleKillCounter = new GuiButton(5, (this.width / 3) - 100, this.height - 16 * (this.height / 20), "Kill Counter: ON"));
        } else {
            this.buttonList.add(ButtonToggleKillCounter = new GuiButton(5, (this.width / 3) - 100, this.height - 16 * (this.height / 20), "Kill Counter: OFF"));
        }

        this.buttonList.add(ButtonKillsColor = new GuiButton(6, (this.width / 3) - 100, this.height - 15 * (this.height / 20), "Rotate Kill Counter Color"));

        if (MegaWallsToolsConfig.isWitherWarningEnabled) {
            this.buttonList.add(ButtonToggleWitherWarning = new GuiButton(7, (this.width / 3) - 100, this.height - 14 * (this.height / 20), "Wither Warning: ON"));
        } else {
            this.buttonList.add(ButtonToggleWitherWarning = new GuiButton(7, (this.width / 3) - 100, this.height - 14 * (this.height / 20), "Wither Warning: OFF"));
        }

        this.buttonList.add(ButtonSetWitherWarning = new GuiSlider(8, (this.width / 3) - 100, this.height - 13 * (this.height / 20), 200, 20, "Wither Health: ", "", 1, 1500, MegaWallsToolsConfig.witherWarning, false, true));
        ButtonSetWitherWarning.dragging = false;

        if (MegaWallsToolsConfig.isMWCAlertsEnabled) {
            this.buttonList.add(ButtonToggleMWCAlerts = new GuiButton(9, (this.width / 3) * 2 - 100, this.height - 18 * (this.height / 20), "MWCooldowns Alerts: ON"));
        } else {
            this.buttonList.add(ButtonToggleMWCAlerts = new GuiButton(9, (this.width / 3) * 2 - 100, this.height - 18 * (this.height / 20), "MWCooldowns Alerts: OFF"));
        }

        this.buttonList.add(ButtonSetAlertsSound = new GuiButton(10, (this.width / 3) * 2  - 100, this.height - 17 * (this.height / 20), "Change Alerts Sound (" + MegaWallsToolsConfig.mwcAlertSound + ")"));
        this.buttonList.add(ButtonSetHunterLevel = new GuiSlider(11, (this.width / 3) * 2 - 100, this.height - 16 * (this.height / 20), 200, 20, "Hunter FoN Level: ", "", 1, 9, MegaWallsToolsConfig.hunterLevel, false, true));
        ButtonSetHunterLevel.dragging = false;
        this.buttonList.add(ButtonSetPhoenixLevel = new GuiSlider(12, (this.width / 3) * 2 - 100, this.height - 15 * (this.height / 20), 200, 20, "Phoenix Res Level: ", "", 1, 9, MegaWallsToolsConfig.phoenixLevel, false, true));
        ButtonSetPhoenixLevel.dragging = false;
    }
}

