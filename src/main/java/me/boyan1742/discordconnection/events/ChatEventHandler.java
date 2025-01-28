package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.Config;
import me.boyan1742.discordconnection.DiscordConnection;
import me.boyan1742.discordconnection.Utils;
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
        if (event.getPlayer() == null || Bot.getInstance() == null || Config.ignoredUsers.contains(event.getUsername()))
            return;

        String username = event.getUsername();
        Color embedColor = Color.ORANGE;

        if (event.getPlayer().getTeam() != null) {
            Team playerTeam = event.getPlayer().getTeam();
            String teamColor = playerTeam.getColor().getName();

            if(!teamColor.isBlank()) {
                embedColor = Utils.fromMinecraftColor(teamColor);
            }

            Component comp = Component.empty();

//            var component = playerTeam.getFormattedName(comp);

//            StringBuilder sb = new StringBuilder();
//
//            component.toFlatList().forEach(x -> {
//                String str = x.getContents().toString();
//                if (str.contains("-") && Config.emojifulReplaceDashWithUnderscore) {
//                    str = str.replace('-', '_');
//                }
//
//                sb.append(str, 8, str.length() - 1);
//            });
//
//            sb.append(event.getUsername());
//            username = sb.toString();

            username = Utils.emojifulTransformString(event.getPlayer().getDisplayName().getString());
        }

        String message = Utils.emojifulTransformString(event.getMessage().getString());

        Bot.getInstance().sendEmbed(DiscordChannelType.CHAT_CHANNEL, username, message, embedColor);
    }
}
