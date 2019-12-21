package cowzgonecrazy.megawallstools.Commands;

import cowzgonecrazy.megawallstools.Modules.C;
import cowzgonecrazy.megawallstools.Modules.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommandRandomClass implements ICommand {

    private String[] allClasses = {"Skeleton", "Zombie", "Creeper", "Enderman", "Herobrine", "Spider", "Squid", "Dreadlord", "Shaman", "Arcanist", "Golem", "Blaze", "Pigman", "Pirate", "Hunter", "Werewolf", "Moleman", "Phoenix"}; // 18 total
    private String[] damageClasses = {"Herobrine", "Creeper", "Pirate", "Arcanist"}; // 4 total
    private String[] controlClasses = {"Squid", "Creeper", "Blaze", "Shaman", "Moleman"}; // 5 total
    private String[] fighterClasses = {"Herobrine", "Arcanist", "Pirate", "Hunter", "Shaman", "Pigman", "Golem", "Dreadlord", "Werewolf"}; // 9
    private String[] rangedClasses = {"Skeleton", "Phoenix", "Blaze", "Hunter"}; // 4
    private String[] tankClasses = {"Squid", "Zombie", "Golem", "Werewolf"}; // 4
    private String[] supportClasses = {"Pigman", "Zombie", "Phoenix"}; // 3
    private String[] mobilityClasses = {"Enderman", "Skeleton", "Spider", "Moleman"}; // 4
    private String[] difficultyOne = {"Herobrine", "Squid", "Arcanist", "Dreadlord"}; // 4
    private String[] difficultyTwo = {"Enderman", "Zombie", "Blaze", "Golem", "Pigman", "Werewolf", "Moleman"}; // 7
    private String[] difficultyThree = {"Spider", "Skeleton", "Hunter", "Pirate", "Shaman"}; // 5
    private String[] difficultyFour = {"Creeper", "Phoenix"}; // 2
    Random r = new Random();

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }


    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }


    //@Override
    public String getCommandName() {
        return "randomclass";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "Returns the name of a random mega walls class for you to play!";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> commandAliases = new ArrayList();
        commandAliases.add("rc");
        return commandAliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        try {
            if (args.length < 1) {
                int temp = r.nextInt(18);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + allClasses[temp]);
            } else if (args[0].equalsIgnoreCase("damage")) {
                int temp = r.nextInt(4);
                Utils.sendMessage(C.GREEN + "Your random damage class is " + C.AQUA + damageClasses[temp]);
            } else if (args[0].equalsIgnoreCase("control")) {
                int temp = r.nextInt(5);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + controlClasses[temp]);
            } else if (args[0].equalsIgnoreCase("fighter")) {
                int temp = r.nextInt(9);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + fighterClasses[temp]);
            } else if (args[0].equalsIgnoreCase("ranged") || args[0].equalsIgnoreCase("bow")) {
                int temp = r.nextInt(4);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + rangedClasses[temp]);
            } else if (args[0].equalsIgnoreCase("tank")) {
                int temp = r.nextInt(4);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + tankClasses[temp]);
            } else if (args[0].equalsIgnoreCase("support")) {
                int temp = r.nextInt(3);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + supportClasses[temp]);
            } else if (args[0].equalsIgnoreCase("mobility")) {
                int temp = r.nextInt(4);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + mobilityClasses[temp]);
            } else if (args[0].equalsIgnoreCase("difficultyOne")) {
                int temp = r.nextInt(4);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + difficultyOne[temp]);
            } else if (args[0].equalsIgnoreCase("difficultyTwo")) {
                int temp = r.nextInt(7);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + difficultyTwo[temp]);
            } else if (args[0].equalsIgnoreCase("difficultyThree")) {
                int temp = r.nextInt(5);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + difficultyThree[temp]);
            } else if (args[0].equalsIgnoreCase("difficultyFour")) {
                int temp = r.nextInt(2);
                Utils.sendMessage(C.GREEN + "Your random class is " + C.AQUA + difficultyFour[temp]);
            } else if (args[0].equalsIgnoreCase("help")) {
                Utils.sendMessage(C.AQUA + "Want something more specific? " + C.GREEN + "Try " + C.GREEN + "/randomclass"+ C.GREEN + "damage, " + C.GREEN + "control, " + C.GREEN + "fighter, " + C.GREEN + "ranged/bow, "  + C.GREEN + "tank, " + C.GREEN + "support, " + C.GREEN + "mobility");
                Utils.sendMessage(C.AQUA + "Want a specific difficulty? "  + C.GREEN + "Try "  + C.GREEN + "/randomclass" + C.GREEN + "difficultyOne, " + C.GREEN + "difficultyTwo, " + C.GREEN + "difficultyThree, " + C.GREEN + "or " + C.GREEN + "difficultyFour");
            }
        } catch (Exception e) {

        }
    }
}