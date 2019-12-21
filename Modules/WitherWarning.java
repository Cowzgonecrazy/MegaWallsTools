package cowzgonecrazy.megawallstools.Modules;

import java.util.Collection;

import cowzgonecrazy.megawallstools.Config.MegaWallsToolsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.Int;

public class WitherWarning {
    public static int warning = 100;
    public static String playerTeam = "";
    Scoreboard board = new Scoreboard();
    Minecraft mc = Minecraft.getMinecraft();
    public int getWitherHealth(){
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        ScoreObjective scoreObj = scoreboard.getObjectiveInDisplaySlot(1);
        Collection<Score> scores = scoreboard.getSortedScores(scoreObj);
        int health = 0;
        for(Score score : scores){
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score.getPlayerName());
            String line = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score.getPlayerName());
            if(line.contains("Your") && line.contains("\u00A7a")){
                String trimmed = line.split("her: \u00A7a")[1];
                health = Integer.valueOf(trimmed);
            }else if(line.contains("Your") && line.contains("\u00A7c")){
                String trimmed = line.split("her: \u00A7c")[1];
                System.out.println(trimmed);
                health = Integer.valueOf(trimmed);
            }else{
                continue;
            }
        }
        return health;
    }

    @SubscribeEvent
    public void StartWitherCheck(ClientChatReceivedEvent event){
        String me = event.message.getUnformattedText();
        if(me.contains("The walls have come down") || me.contains("You logged back in!")){
            new Thread(() -> WitherCheck()).start();
        }
    }

    public void WitherCheck(){
        if (MegaWallsToolsConfig.isWitherWarningEnabled) {
            Utils.sendMessage("Wither warning is disabled!");
        } else {
            Utils.sendMessage("Wither warning enabled! You will be warned at " + Integer.toString(MegaWallsToolsConfig.witherWarning) + " health!");
        }

        while(getWitherHealth() > MegaWallsToolsConfig.witherWarning){
            if (MegaWallsToolsConfig.isWitherWarningEnabled) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (MegaWallsToolsConfig.isWitherWarningEnabled) {
            Utils.sendMessage(C.RED + C.BOLD + "WARNING: Your Wither is low health!");
            try {
                mc.thePlayer.playSound("random.anvil_use", 1.0f, 1.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

