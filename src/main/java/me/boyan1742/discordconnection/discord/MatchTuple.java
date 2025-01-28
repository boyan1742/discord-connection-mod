package me.boyan1742.discordconnection.discord;

public record MatchTuple(String text, Style style) {
    @Override
    public String toString() {
        return "('" + text + "', " + style + ")";
    }

    public net.minecraft.network.chat.Style getMinecraftStyle() {
        switch (style) {
            case BOLD -> {
                return net.minecraft.network.chat.Style.EMPTY.withBold(true);
            }
            case ITALIC -> {
                return net.minecraft.network.chat.Style.EMPTY.withItalic(true);
            }
            case BOLD_ITALIC -> {
                return net.minecraft.network.chat.Style.EMPTY.withBold(true).withItalic(true);
            }
            case STRIKETHROUGH -> {
                return net.minecraft.network.chat.Style.EMPTY.withStrikethrough(true);
            }
            default -> {
                return net.minecraft.network.chat.Style.EMPTY;
            }
        }
    }
}
