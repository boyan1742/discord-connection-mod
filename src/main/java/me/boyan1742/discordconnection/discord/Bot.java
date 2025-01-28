package me.boyan1742.discordconnection.discord;

import me.boyan1742.discordconnection.Config;
import me.boyan1742.discordconnection.DiscordConnection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.internal.utils.JDALogger;

import java.awt.*;

public class Bot {
    private static Bot INSTANCE = null;

    public static Bot getInstance() {
        return INSTANCE;
    }

    private TextChannel statusChannel;
    private TextChannel joinLeaveChannel;
    private TextChannel advancementsChannel;
    private TextChannel deathsChannel;
    private TextChannel chatChannel;
    private TextChannel serverLogsChannel;

    private final JDA jda;

    public Bot() throws InterruptedException {
        DiscordConnection.LOGGER.info("Creating discord bot object.");

        String botToken = Config.botToken;
        if (botToken.isBlank() || botToken.equals(Config.DEFAULT_BOT_TOKEN)) {
            DiscordConnection.LOGGER.error("Invalid Bot Token specified, Terminating mod execution!");
            jda = null;
            return;
        }

        String statusChannelID = Config.statusChannelID;
        String joinLeaveChannelID = Config.joinLeaveChannelID;
        String deathsChannelID = Config.deathsChannelID;
        String advancementsChannelID = Config.advancementsChannelID;
        String chatChannelID = Config.chatChannelID;
        String serverLogsChannelID = Config.serverLogsChannelID;

        JDALogger.setFallbackLoggerEnabled(false);

        jda = JDABuilder.createDefault(botToken)
                .setActivity(Activity.customStatus("Kur"))
                .setStatus(OnlineStatus.ONLINE)
                .build();

        jda.awaitReady();

        statusChannel = jda.getTextChannelById(statusChannelID);
        joinLeaveChannel = jda.getTextChannelById(joinLeaveChannelID);
        deathsChannel = jda.getTextChannelById(deathsChannelID);
        advancementsChannel = jda.getTextChannelById(advancementsChannelID);
        chatChannel = jda.getTextChannelById(chatChannelID);
        serverLogsChannel = jda.getTextChannelById(serverLogsChannelID);

        //IMPORTANT: this is always the last line.
        INSTANCE = this;
    }

    public void sendEmbed(DiscordChannelType channelType, String title, String contents) {
        sendEmbed(channelType, title, contents, Color.black);
    }

    public void sendEmbed(DiscordChannelType channelType, String title, String contents, Color color) {
        if (INSTANCE == null) {
            return;
        }

        TextChannel channel;

        switch (channelType) {
            case STATUS_CHANNEL -> channel = statusChannel;
            case JOIN_LEAVE_CHANNEL -> channel = joinLeaveChannel;
            case DEATHS_CHANNEL -> channel = deathsChannel;
            case ADVANCEMENTS_CHANNEL -> channel = advancementsChannel;
            case CHAT_CHANNEL -> channel = chatChannel;
            case SERVER_LOGS_CHANNEL -> channel = serverLogsChannel;
            default -> channel = null;
        }

        if (channel == null) {
            DiscordConnection.LOGGER.info("One of the channels might not be setup or not existing! ChannelType: {}", channelType);
            return;
        }

        channel.sendMessageEmbeds(
                new EmbedBuilder()
                        .setTitle(title)
                        .setDescription(contents)
                        .setColor(color)
                        .build())
                .queue();
    }

    public void shutdown() {
        jda.shutdown();
    }
}
