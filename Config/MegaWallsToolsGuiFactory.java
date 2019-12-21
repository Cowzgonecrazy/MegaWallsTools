package cowzgonecrazy.megawallstools.Config;

import cowzgonecrazy.megawallstools.MegaWallsTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MegaWallsToolsGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {return ModGuiConfig.class;}

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {return null;}

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {return null;}

    public static class ModGuiConfig extends GuiConfig{

        public ModGuiConfig(GuiScreen guiScreen){
            super(guiScreen, getConfigElements(), MegaWallsTools.MODID, false, false, "Mega Walls Tools v" + MegaWallsTools.VERSION);
        }

        private static List<IConfigElement> getConfigElements()
        {
            List<IConfigElement> list = new ArrayList<IConfigElement>();
            list.add(new DummyConfigElement.DummyCategoryElement("General", "", GeneralEntry.class));
            list.add(new DummyConfigElement.DummyCategoryElement("MWCooldowns", "", MWCooldownsEntry.class));
            return list;
        }

        public static class GeneralEntry extends GuiConfigEntries.CategoryEntry
        {
            public GeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
            {
                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen()
            {

                List<IConfigElement> list = new ArrayList<IConfigElement>();
                list.addAll((new ConfigElement(MegaWallsToolsConfig.config.getCategory(MegaWallsToolsConfig.CATEGORY_GENERAL))).getChildElements());

                return new GuiConfig(this.owningScreen, list, this.owningScreen.modID, MegaWallsToolsConfig.CATEGORY_GENERAL,
                        false,
                        false, "General");
            }
        }

        public static class MWCooldownsEntry extends GuiConfigEntries.CategoryEntry
        {
            public MWCooldownsEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
            {
                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen()
            {

                List<IConfigElement> list = new ArrayList<IConfigElement>();
                list.addAll((new ConfigElement(MegaWallsToolsConfig.config.getCategory(MegaWallsToolsConfig.CATEGORY_MWCOOLDOWNS))).getChildElements());

                return new GuiConfig(this.owningScreen, list, this.owningScreen.modID, MegaWallsToolsConfig.CATEGORY_MWCOOLDOWNS,
                        false,
                        false, "MWCooldowns");
            }
        }
    }
}
