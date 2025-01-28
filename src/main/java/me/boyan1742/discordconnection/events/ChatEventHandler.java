package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.Config;
import me.boyan1742.discordconnection.DiscordConnection;
import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.DiscordChannelType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber
public class ChatEventHandler {

    @SubscribeEvent
    public static void onChatMessage(ServerChatEvent event) {
        if (event.getPlayer() == null || Bot.getInstance() == null)
            return;

        String username = event.getUsername();
        Color embedColor = Color.ORANGE;

        if (event.getPlayer().getTeam() != null) {
            Team playerTeam = event.getPlayer().getTeam();
            String teamColor = playerTeam.getColor().getName();

            DiscordConnection.LOGGER.info(teamColor);

            if(!teamColor.isBlank()) {
                switch (teamColor.toLowerCase()) {
                    case "black" -> embedColor = Color.decode("#000000");
                    case "dark_blue" -> embedColor = Color.decode("#0000AA");
                    case "dark_green" -> embedColor = Color.decode("#00AA00");
                    case "dark_aqua" -> embedColor = Color.decode("#00AAAA");
                    case "dark_red" -> embedColor = Color.decode("#AA0000");
                    case "dark_purple" -> embedColor = Color.decode("#AA00AA");
                    case "gold" -> embedColor = Color.decode("#FFAA00");
                    case "gray" -> embedColor = Color.decode("#AAAAAA");
                    case "dark_gray" -> embedColor = Color.decode("#555555");
                    case "blue" -> embedColor = Color.decode("#5555FF");
                    case "green" -> embedColor = Color.decode("#55FF55");
                    case "aqua" -> embedColor = Color.decode("#55FFFF");
                    case "red" -> embedColor = Color.decode("#FF5555");
                    case "light_purple" -> embedColor = Color.decode("#FF55FF");
                    case "yellow" -> embedColor = Color.decode("#FFFF55");
                    case "white" -> embedColor = Color.decode("#FFFFFF");
                }
            }

            DiscordConnection.LOGGER.info("{}", embedColor.getRGB());

            Component comp = Component.empty();

            var component = playerTeam.getFormattedName(comp);

            StringBuilder sb = new StringBuilder();

            component.toFlatList().forEach(x -> {
                String str = x.getContents().toString();
                if (str.contains("-") && Config.emojifulReplaceDashWithUnderscore) {
                    str = str.replace('-', '_');
                }

                sb.append(str, 8, str.length() - 1);
            });

            sb.append(event.getUsername());
            username = sb.toString();
        }

        String message = event.getMessage().getString();
        Bot.getInstance().sendEmbed(DiscordChannelType.CHAT_CHANNEL, username, message, embedColor);
    }
}
