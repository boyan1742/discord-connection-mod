package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.Config;
import me.boyan1742.discordconnection.DiscordConnection;
import me.boyan1742.discordconnection.Utils;
import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.DiscordChannelType;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class PlayerAdvancementEventHandler {

    private static final Map<Player, Long> lastProcessedTime = new HashMap<>();
    private static final long COOLDOWN_MS = 1000;

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        if (Bot.getInstance() == null || event.getAdvancement().getDisplay() == null ||
                !event.getAdvancement().getDisplay().shouldShowToast() ||
                !event.getAdvancement().getDisplay().shouldAnnounceChat() ||
                Config.ignoredUsers.contains(event.getEntity().getName().getString()))
            return;

        var player = event.getEntity();
        long currentTime = System.currentTimeMillis();

        if (lastProcessedTime.containsKey(player) &&
                currentTime - lastProcessedTime.get(player) < COOLDOWN_MS) {
            return;
        }

        var serverPlayers = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()
                .stream().filter(x -> x.getUUID().equals(player.getUUID())).toList();

        if (serverPlayers.size() != 1) {
            return;
        }

        if (!serverPlayers.get(0).getAdvancements().getOrStartProgress(event.getAdvancement()).isDone()) {
            return;
        }

        Color embedColor = Color.green;
        StringBuilder sb = new StringBuilder();

        sb.append(event.getAdvancement().getChatComponent().getString());
        if (event.getAdvancement().getDisplay() != null) {
            sb.append("\n").append(event.getAdvancement().getDisplay().getDescription().getString());

            embedColor = Utils.fromMinecraftColor(event.getAdvancement().getChatComponent().getStyle().getColor());
        }

        Bot.getInstance().sendEmbed(DiscordChannelType.ADVANCEMENTS_CHANNEL,
                Utils.emojifulTransformString(event.getEntity().getDisplayName().getString()),
                sb.toString(), embedColor);

        lastProcessedTime.put(player, currentTime);
    }
}
