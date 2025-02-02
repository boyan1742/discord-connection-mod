package me.boyan1742.discordconnection.discord.commands;

import me.boyan1742.discordconnection.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TPSCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("tps")) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setTitle("Current TPS: " + Utils.getTPS())
                            .build())
                    .queue();
        }
    }
}
