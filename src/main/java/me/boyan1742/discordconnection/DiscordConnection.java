package me.boyan1742.discordconnection;

import com.mojang.logging.LogUtils;
import me.boyan1742.discordconnection.events.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DiscordConnection.MODID)
public class DiscordConnection {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "discordconnection";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    public DiscordConnection() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ChatEventHandler.class);
        MinecraftForge.EVENT_BUS.register(ServerExecutionEvents.class);
        MinecraftForge.EVENT_BUS.register(CommandEventHandler.class);
        MinecraftForge.EVENT_BUS.register(PlayerJoinLeaveEventHandler.class);
        MinecraftForge.EVENT_BUS.register(PlayerDeathEventHandler.class);
        MinecraftForge.EVENT_BUS.register(PlayerAdvancementEventHandler.class);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        //((org.apache.logging.log4j.core.Logger) LOGGER).addAppender(new ErrorLogAppender());
    }
}

