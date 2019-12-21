package cowzgonecrazy.megawallstools.Modules;

import cowzgonecrazy.megawallstools.MegaWallsTools;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.apache.commons.io.IOUtils;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

// code inspired by Sk1ers public mod tbh
public class Utils {
    private final static String prefix = C.GREEN + "[" + C.WHITE + "MWTOOLS" + C.GREEN + "]";

    public static void sendRawMessage(IChatComponent comp) {
        try {
            Minecraft.getMinecraft().thePlayer.addChatMessage(comp);
        } catch (Exception e) {

        }
    }

    public static void sendRawMessage(String message) {
        sendRawMessage(new ChatComponentText(message));
    }

    public static void sendMessage(String message) {
        sendRawMessage(new ChatComponentText(prefix + " " + message));
    }

    public static void sendMessageToServer(String message) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
    }

    public static void getBlacklistedUUIDs() {
        try {
            final String rawUUIDs = IOUtils.toString(new URL("https://gist.github.com/Cowzgonecrazy/91470f62d4c2204883b96dfe5b5ea9d9/raw"));
            MegaWallsTools.setUUIDs(new ArrayList((Collection<?>)Arrays.asList(rawUUIDs.split("\n"))));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}