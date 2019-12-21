package cowzgonecrazy.megawallstools.Commands;

import com.google.common.collect.Lists;
import cowzgonecrazy.megawallstools.MegaWallsTools;
import cowzgonecrazy.megawallstools.Modules.*;
import cowzgonecrazy.megawallstools.hudproperty.test.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Collection;
import java.util.List;

public class CommandGetFinals implements ICommand {

    private final List<String> aliases = Lists.newArrayList("");

    Minecraft mc = Minecraft.getMinecraft();

    private static String[] yellowTeam = new String[25];
    private static String[] blueTeam = new String[25];
    private static String[] greenTeam = new String[25];
    private static String[] redTeam = new String[25];

    private static int[] yellowFinals = new int[25];
    private static int[] blueFinals = new int[25];
    private static int[] greenFinals = new int[25];
    private static int[] redFinals = new int[25];

    private static int totalYellowFinals;
    private static int totalBlueFinals;
    private static int totalGreenFinals;
    private static int totalRedFinals;

    //§r§.{1}\\S+§r§f §r§f.*? by §r§.{1}(\\S+)('s |)§r

    @Override
    public String getCommandName() {
        return "mw";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "MWCooldowns Mod";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1 || (args[0] != null)) {
            Utils.sendMessage("Must include an argument! Try 'yellow', 'red', 'blue', 'green' or someones name!");
        } else if (args[0].equalsIgnoreCase("yellow")) {
            Collection<NetworkPlayerInfo> players = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
            int gameTempCount = 0;
            String[] gameTemp = new String[100];
            System.out.println("Outputting players...");
            for (final NetworkPlayerInfo networkplayerinfo : players) {
                System.out.print(networkplayerinfo.getGameProfile().getName());
                System.out.print(", ");
                gameTemp[gameTempCount] = networkplayerinfo.getGameProfile().getName();
                gameTempCount++;
            }

            for (int i = 0; i < yellowTeam.length; i++) {
                for (int c = 0; i < gameTemp.length; i++) {
                    if (yellowTeam[i].equalsIgnoreCase(gameTemp[c])) {
                        totalYellowFinals += yellowFinals[i];
                        break;
                    }
                }
            }
            System.out.println("Yellow finals found: " + totalYellowFinals);
            Utils.sendMessage("Yellow has " + totalYellowFinals + " total finals!");
        } else if (args[0].equalsIgnoreCase("blue")) {

        } else if (args[0].equalsIgnoreCase("green")) {

        } else if (args[0].equalsIgnoreCase("red")) {

        }
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public List getCommandAliases() {
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
