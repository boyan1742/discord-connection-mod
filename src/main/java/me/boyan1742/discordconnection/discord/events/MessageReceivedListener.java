package me.boyan1742.discordconnection.discord.events;

import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.MatchTuple;
import me.boyan1742.discordconnection.discord.Style;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageReceivedListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (Bot.getInstance() == null || !event.getChannel().getId().equals(Bot.getInstance().getChatChannelID()) ||
                event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;

        String senderName = event.getAuthor().getEffectiveName();
        String message = event.getMessage().getContentRaw();

        Component messageComp;
        try {
            messageComp = transformMessageComponent(message);
        } catch (Exception ex) {
            messageComp = Component.literal(message).withStyle(x -> x.applyFormat(ChatFormatting.RESET));
        }

        Component beginning = Component.literal("[DISCORD | " + senderName + "] ")
                .withStyle(style -> style.withColor(ChatFormatting.GOLD)
                        .withBold(true).withItalic(true));

        Component comp = Component.empty().append(beginning).append(messageComp);

        ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()
                .forEach(x -> x.sendSystemMessage(comp));
    }

    private Component transformMessageComponent(String message) {
        List<MatchTuple> matches = new ArrayList<>();

        // Regex to match bold, italic, and bold-italic text
        String regex = "(\\*\\*\\*(.*?)\\*\\*\\*)|(\\*\\*(.*?)\\*\\*)|(\\*(.*?)\\*|_(.*?)_)|(~~(.*?)~~)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        int lastEnd = 0; // Tracks the end of the last match

        // Find all matches
        while (matcher.find()) {
            // Add plain text before the match (if any)
            if (matcher.start() > lastEnd) {
                String plainText = message.substring(lastEnd, matcher.start());
                matches.add(new MatchTuple(plainText, Style.NONE));
            }

            // Determine the style based on the matched group
            Style style = Style.NONE;
            String matchedText = "";

            if (matcher.group(1) != null) { // Bold and italic (***text***)
                style = Style.BOLD_ITALIC;
                matchedText = matcher.group(2);
            } else if (matcher.group(3) != null) { // Bold (**text**)
                style = Style.BOLD;
                matchedText = matcher.group(4);
            } else if (matcher.group(5) != null) { // Italic (*text* or _text_)
                style = Style.ITALIC;
                matchedText = matcher.group(6) != null ? matcher.group(6) : matcher.group(7);
            } else if (matcher.group(8) != null) { // Strikethrough (~~text~~)
                style = Style.STRIKETHROUGH;
                matchedText = matcher.group(9);
            }

            // Add the matched text and its style to the list
            matches.add(new MatchTuple(matchedText, style));

            // Update the end of the last match
            lastEnd = matcher.end();
        }

        // Add remaining plain text after the last match (if any)
        if (lastEnd < message.length()) {
            String plainText = message.substring(lastEnd);
            matches.add(new MatchTuple(plainText, Style.NONE));
        }

        MutableComponent component = Component.empty();

        for (var s : matches) {
            var sComp = Component.literal(s.text()).withStyle(s.getMinecraftStyle());

            component.append(sComp);
        }

        return component;
    }
}