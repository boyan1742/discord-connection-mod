package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.DiscordChannelType;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber
public class ServerExecutionEvents {

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
}
