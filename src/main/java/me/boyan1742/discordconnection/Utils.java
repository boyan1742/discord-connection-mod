package me.boyan1742.discordconnection;

import net.minecraft.network.chat.TextColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String emojifulTransformString(String str) {
        if(!Config.emojifulReplaceDashWithUnderscore) {
            return str;
        }

        String regex = ":([\\w\\W-]+):";
        Matcher matcher = Pattern.compile(regex).matcher(str);

        // StringBuffer to build the result
        StringBuilder result = new StringBuilder();

        // Iterate through all matches and replace '-' with '_'
        while (matcher.find()) {
            // Get the matched text and replace '-' with '_'
            String matchedText = matcher.group(1).replace("-", "_");

            // Replace the matched part with the modified text
            matcher.appendReplacement(result, ":" + matchedText + ":");
        }

        // Append the remainder of the string after the last match
        matcher.appendTail(result);

        return result.toString();
    }

    public static Color fromMinecraftColor(String colorName) {

        Color color = Color.white;

        switch (colorName.toLowerCase()) {
            case "black" -> color = Color.decode("#000000");
            case "dark_blue" -> color = Color.decode("#0000AA");
            case "dark_green" -> color = Color.decode("#00AA00");
            case "dark_aqua" -> color = Color.decode("#00AAAA");
            case "dark_red" -> color = Color.decode("#AA0000");
            case "dark_purple" -> color = Color.decode("#AA00AA");
            case "gold" -> color = Color.decode("#FFAA00");
            case "gray" -> color = Color.decode("#AAAAAA");
            case "dark_gray" -> color = Color.decode("#555555");
            case "blue" -> color = Color.decode("#5555FF");
            case "green" -> color = Color.decode("#55FF55");
            case "aqua" -> color = Color.decode("#55FFFF");
            case "red" -> color = Color.decode("#FF5555");
            case "light_purple" -> color = Color.decode("#FF55FF");
            case "yellow" -> color = Color.decode("#FFFF55");
            case "white" -> color = Color.decode("#FFFFFF");
        }

        return color;
    }

    public static Color fromMinecraftColor(TextColor colorName) {
        if(colorName == null) {
            return Color.white;
        }

        var clr = colorName.toString();

        return fromMinecraftColor(clr);
    }
}
