package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.Config;
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
    private static long lastMessageTime = 0;
    private static final long COOLDOWN = 30_000;

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) throws InterruptedException {
        new Bot();

        PlayerJoinLeaveEventHandler.setPlayerCountActivity(false);
        Bot.getInstance().sendEmbed(DiscordChannelType.STATUS_CHANNEL, "Server", "Started!", Color.BLUE);
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

        var tps = 20.0 / elapsedSeconds; // Target TPS is 20

        if (tps < Config.lowTPSValue && Bot.getInstance() != null && Config.echoLowTPS &&
                System.currentTimeMillis() - lastMessageTime >= COOLDOWN) {

            Bot.getInstance().sendEmbed(DiscordChannelType.STATUS_CHANNEL, "Server",
                    String.format("[WARNING] Server tps below %f, currently: %f", Config.lowTPSValue, tps),
                    Color.yellow);

            lastMessageTime = System.currentTimeMillis();
        }
    }
}
