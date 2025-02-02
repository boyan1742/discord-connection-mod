package me.boyan1742.discordconnection.discord.commands;

import me.boyan1742.discordconnection.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

public class OnlineCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("online")) {
            var server = ServerLifecycleHooks.getCurrentServer();
            StringBuilder sb = new StringBuilder();
            if (server != null) {
                var playerList = server.getPlayerList();
                playerList.getPlayers().forEach(x ->
                        sb.append(Utils.emojifulTransformString(x.getDisplayName().getString()))
                                .append("\n"));
            }

            String playerList = sb.toString();
            if (playerList.isEmpty()) {
                playerList = "Server is empty!";
            }

            if (playerList.endsWith("\n")) {
                playerList = playerList.substring(0, playerList.length() - 1);
            }

            event.replyEmbeds(
                            new EmbedBuilder()
                                    .setTitle("Currently online players")
                                    .setDescription(playerList)
                                    .build())
                    .queue();
        }
    }
}
