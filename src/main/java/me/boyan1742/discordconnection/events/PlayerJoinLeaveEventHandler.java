package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.Config;
import me.boyan1742.discordconnection.Utils;
import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.DiscordChannelType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.awt.*;

@Mod.EventBusSubscriber
public class PlayerJoinLeaveEventHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (Bot.getInstance() == null || Config.ignoredUsers.contains(event.getEntity().getName().getString()))
            return;

        setPlayerCountActivity(false);

        String username = Utils.emojifulTransformString(event.getEntity().getDisplayName().getString());

        Bot.getInstance().sendEmbed(DiscordChannelType.JOIN_LEAVE_CHANNEL,
                username, "Joined!", Color.GREEN);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (Bot.getInstance() == null || Config.ignoredUsers.contains(event.getEntity().getName().getString()))
            return;

        setPlayerCountActivity(true);
        String username = Utils.emojifulTransformString(event.getEntity().getDisplayName().getString());

        Bot.getInstance().sendEmbed(DiscordChannelType.JOIN_LEAVE_CHANNEL,
                username, "Left!", Color.CYAN);
    }

    public static void setPlayerCountActivity(boolean subtract) {
        int playerCount = ServerLifecycleHooks.getCurrentServer().getPlayerCount() - (subtract ? 1 : 0);

        if (playerCount <= 0) {
            Bot.getInstance().setBotActivity(Config.textActivityNoPlayers);
        } else if (playerCount == 1) {
            Bot.getInstance().setBotActivity(Config.textActivityOnePlayer);
        } else {
            Bot.getInstance().setBotActivity(playerCount + Config.textActivityTwoOrMorePlayers);
        }
    }
}
