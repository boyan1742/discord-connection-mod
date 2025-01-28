package me.boyan1742.discordconnection;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = DiscordConnection.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final String DEFAULT_BOT_TOKEN = "INSERT_BOT_TOKEN_HERE";

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    //private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER.comment("Whether to log the dirt block on common setup").define("logDirtBlock", true);

    //private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER.comment("A magic number").defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    //public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER.comment("What you want the introduction message to be for the magic number").define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    //private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER.comment("A list of items to log on common setup.").defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    private static final ForgeConfigSpec.ConfigValue<String> BOT_TOKEN = BUILDER.comment("The discord bot's token. DO NOT SHARE THIS!!!").define("botToken", DEFAULT_BOT_TOKEN);
    private static final ForgeConfigSpec.BooleanValue ECHO_SERVER_COMMANDS = BUILDER.comment("Should commands executed on the server and by it be echoed inside of discord.").define("echoServerCommands", false);

    private static final ForgeConfigSpec.ConfigValue<String> STATUS_CHANNEL_ID = BUILDER.push("channelIDs").comment("The discord channel's ID where all server status messages will be sent.").define("statusChannelID", "0");
    private static final ForgeConfigSpec.ConfigValue<String> JOIN_LEAVE_CHANNEL_ID = BUILDER.comment("The discord channel's ID where all join and leave messages will be sent.").define("joinLeaveChannelID", "0");
    private static final ForgeConfigSpec.ConfigValue<String> DEATHS_CHANNEL_ID = BUILDER.comment("The discord channel's ID where all death messages will be sent.").define("deathsChannelID", "0");
    private static final ForgeConfigSpec.ConfigValue<String> ADVANCEMENTS_CHANNEL_ID = BUILDER.comment("The discord channel's ID where all advancements messages will be sent.").define("advancementsChannelID", "0");
    private static final ForgeConfigSpec.ConfigValue<String> CHAT_CHANNEL_ID = BUILDER.comment("The discord channel's ID where all chat messages will be sent.").define("chatChannelID", "0");
    private static final ForgeConfigSpec.ConfigValue<String> SERVER_LOGS_CHANNEL_ID = BUILDER.comment("The discord channel's ID where all server logs will be sent. This channel should possibly be private.").define("serverLogsChannelID", "0");

    private static final ForgeConfigSpec.ConfigValue<String> ACTIVITY_NO_PLAYERS = BUILDER.pop().push("messages").comment("The text of the bot when there are none players online.").define("noPlayersText", "No-one is playing!");
    private static final ForgeConfigSpec.ConfigValue<String> ACTIVITY_ONE_PLAYER = BUILDER.comment("The text of the bot when there is one player online.").define("onePlayerText", "One player is online!");
    private static final ForgeConfigSpec.ConfigValue<String> ACTIVITY_TWO_PLAYERS = BUILDER.comment("The text of the bot when there are two or more players online.").define("twoPlayersText", " players are online!");

    private static final ForgeConfigSpec.BooleanValue EMOJIFUL_REPLACE_DASH_WITH_UNDERSCORE = BUILDER.pop().push("EMOJIFUL").comment("A compatibility setting with the mod 'Emojiful' that makes it's emoji that have a dash be compatible with discord (discord has the same emoji name/id but with underscore).").define("emojifulReplaceDashWithUnderscore", false);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> IGNORED_USERS_LIST = BUILDER.pop().push("lists").comment("A list of ignored users. These users' actions will not be printed in the specific discord channels.").defineList("ignoredUsers", List.of("Admin"), x -> x instanceof String str && !str.isEmpty());
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> IGNORED_SERVER_COMMANDS = BUILDER.comment("A list of ignored commands. These commands will never be printed inside of the server logs discord channel.").defineList("ignoredCommands", List.of("help", "?", "msg", "w", "say"), x -> x instanceof String str && !str.isEmpty());

    static final ForgeConfigSpec SPEC = BUILDER.pop().build();

    //public static boolean logDirtBlock;
    //public static int magicNumber;
    //public static String magicNumberIntroduction;
    //public static Set<Item> items;

    public static String botToken;
    public static boolean echoServerCommands;

    public static String statusChannelID;
    public static String joinLeaveChannelID;
    public static String deathsChannelID;
    public static String advancementsChannelID;
    public static String chatChannelID;
    public static String serverLogsChannelID;

    public static Set<String> ignoredServerCommands;
    public static Set<String> ignoredUsers;

    public static String textActivityNoPlayers;
    public static String textActivityOnePlayer;
    public static String textActivityTwoOrMorePlayers;

    public static boolean emojifulReplaceDashWithUnderscore;

    //private static boolean validateItemName(final Object obj) {
    //    return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    //}

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        //logDirtBlock = LOG_DIRT_BLOCK.get();
        //magicNumber = MAGIC_NUMBER.get();
        //magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        //items = ITEM_STRINGS.get().stream().map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).collect(Collectors.toSet());

        botToken = BOT_TOKEN.get();

        echoServerCommands = ECHO_SERVER_COMMANDS.get();

        statusChannelID = STATUS_CHANNEL_ID.get();
        joinLeaveChannelID = JOIN_LEAVE_CHANNEL_ID.get();
        deathsChannelID = DEATHS_CHANNEL_ID.get();
        advancementsChannelID = ADVANCEMENTS_CHANNEL_ID.get();
        chatChannelID = CHAT_CHANNEL_ID.get();
        serverLogsChannelID = SERVER_LOGS_CHANNEL_ID.get();

        textActivityNoPlayers = ACTIVITY_NO_PLAYERS.get();
        textActivityOnePlayer = ACTIVITY_ONE_PLAYER.get();
        textActivityTwoOrMorePlayers = ACTIVITY_TWO_PLAYERS.get();

        emojifulReplaceDashWithUnderscore = EMOJIFUL_REPLACE_DASH_WITH_UNDERSCORE.get();

        ignoredServerCommands = new HashSet<>(IGNORED_SERVER_COMMANDS.get());
        ignoredUsers = new HashSet<>(IGNORED_USERS_LIST.get());
    }
}
