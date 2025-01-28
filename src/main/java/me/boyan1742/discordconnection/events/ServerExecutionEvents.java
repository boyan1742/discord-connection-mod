package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.DiscordChannelType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber
public class ServerExecutionEvents {

    private static long lastTime = System.nanoTime();


    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) throws InterruptedException {
        new Bot();

        PlayerJoinLeaveEventHandler.setPlayerCountActivity(false);
        Bot.getInstance().sendEmbed(DiscordChannelType.STATUS_CHANNEL, "Server", "Stared!", Color.BLUE);
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        if (Bot.getInstance() == null) {
            return;
        }

        Bot.getInstance().sendEmbed(DiscordChannelType.STATUS_CHANNEL, "Server", "Stopped!", Color.red);
        Bot.getInstance().shutdown();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        long currentTime = System.nanoTime();
        double elapsedSeconds = (currentTime - lastTime) / 1_000_000_000.0;
        lastTime = currentTime;

        double tps = 20.0 / elapsedSeconds; // Target TPS is 20
        //System.out.println("Current TPS: " + tps);

        if (tps < 15.0 && Bot.getInstance() != null) {
            Bot.getInstance().sendEmbed(DiscordChannelType.STATUS_CHANNEL, "Server",
                    "[WARNING] Server tps below 15, currently: " + tps, Color.yellow);
        }
    }
}
