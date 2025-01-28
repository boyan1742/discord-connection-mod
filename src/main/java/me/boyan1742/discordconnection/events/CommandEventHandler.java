package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.Config;
import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.DiscordChannelType;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandEventHandler {

    @SubscribeEvent
    public static void onCommand(CommandEvent event) {
        if (Bot.getInstance() == null) {
            return;
        }

        var src = event.getParseResults().getContext().getSource();

        if (!src.isPlayer() && !Config.echoServerCommands) {
            return;
        }

        var player = src.getPlayer();
        if (player != null && Config.ignoredUsers.contains(src.getPlayer().getName().getString())) {
            return;
        }

        var cmdRoot = event.getParseResults().getContext().getNodes().get(0).getNode().getName();
        if (Config.ignoredServerCommands.contains(cmdRoot.toLowerCase())) {
            return;
        }

        var command = event.getParseResults().getReader().getString();

        Bot.getInstance().sendMessage(DiscordChannelType.SERVER_LOGS_CHANNEL,
                String.format("%s executed a command: %s",
                        player == null ? src.getDisplayName().getString() : player.getDisplayName().getString(),
                        command));
    }
}
