package me.boyan1742.discordconnection.discord;

import me.boyan1742.discordconnection.Config;
import me.boyan1742.discordconnection.DiscordConnection;
import me.boyan1742.discordconnection.discord.commands.OnlineCommand;
import me.boyan1742.discordconnection.discord.commands.TPSCommand;
import me.boyan1742.discordconnection.discord.events.MessageReceivedListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;

import java.awt.*;
import java.util.Collections;

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
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MessageReceivedListener())
                .addEventListeners(new TPSCommand())
                .addEventListeners(new OnlineCommand())
                .build();

        jda.awaitReady();

        jda.updateCommands().addCommands(Collections.emptyList()).queue();

        jda.updateCommands().addCommands(
                Commands.slash("tps", "Gives you the current server TPS."),
                Commands.slash("online", "Lists the players that are online."))
                .queue();

        statusChannel = jda.getTextChannelById(statusChannelID);
        joinLeaveChannel = jda.getTextChannelById(joinLeaveChannelID);
        deathsChannel = jda.getTextChannelById(deathsChannelID);
        advancementsChannel = jda.getTextChannelById(advancementsChannelID);
        chatChannel = jda.getTextChannelById(chatChannelID);
        serverLogsChannel = jda.getTextChannelById(serverLogsChannelID);

        //IMPORTANT: this is always the last line.
        INSTANCE = this;
    }

    public void sendMessage(DiscordChannelType channelType, String message) {
        if(INSTANCE == null) {
            return;
        }

        TextChannel channel = getChannelFromType(channelType);

        if (channel == null) {
            DiscordConnection.LOGGER.info("One of the channels might not be setup or not existing! ChannelType: {}", channelType);
            return;
        }

        channel.sendMessage(message).queue();
    }

    public void sendEmbed(DiscordChannelType channelType, String title, String contents) {
        sendEmbed(channelType, title, contents, Color.black);
    }

    public void sendEmbed(DiscordChannelType channelType, String title, String contents, Color color) {
        if (INSTANCE == null) {
            return;
        }

        TextChannel channel = getChannelFromType(channelType);

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

    private TextChannel getChannelFromType(DiscordChannelType channelType) {
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

        return channel;
    }

    public String getChatChannelID() {
        return chatChannel == null ? "" : chatChannel.getId();
    }

    public String getBotID() {
        return jda.getToken();
    }

    public void shutdown() {
        jda.shutdown();
    }

    public void setBotActivity(String text) {
        jda.getPresence().setActivity(Activity.customStatus(text));
    }
}
