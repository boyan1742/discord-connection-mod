package me.boyan1742.discordconnection;

import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.DiscordChannelType;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorLogAppender extends AbstractAppender {
    protected ErrorLogAppender() {
        super("ErrorLogAppender", null, null, true, null);
        start();
    }

    @Override
    public void append(LogEvent event) {
        if (event.getLevel().isMoreSpecificThan(org.apache.logging.log4j.Level.ERROR) && Bot.getInstance() != null) {

            Throwable throwable = event.getThrown();
            String stacktrace = "";
            if (throwable != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);

                stacktrace = sw.toString();
            }

            Bot.getInstance().sendMessage(DiscordChannelType.SERVER_LOGS_CHANNEL,
                    String.format("Caught Exception: %s\n\nStacktrace: %s",
                            event.getMessage().getFormattedMessage(), stacktrace));
        }
    }
}
