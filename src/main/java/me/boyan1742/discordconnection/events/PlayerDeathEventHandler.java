package me.boyan1742.discordconnection.events;

import me.boyan1742.discordconnection.Config;
import me.boyan1742.discordconnection.Utils;
import me.boyan1742.discordconnection.discord.Bot;
import me.boyan1742.discordconnection.discord.DiscordChannelType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerDeathEventHandler {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player) || Bot.getInstance() == null || Config.ignoredUsers.contains(event.getEntity().getName().getString())) {
            return;
        }

        String deathMessage = event.getSource().getLocalizedDeathMessage(player).getString();

        Bot.getInstance().sendEmbed(DiscordChannelType.DEATHS_CHANNEL,
                Utils.emojifulTransformString(player.getDisplayName().getString()),
                Utils.emojifulTransformString(deathMessage
                        .replace(player.getDisplayName().getString(), "")));
    }
}
