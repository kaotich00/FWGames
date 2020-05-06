package me.kaotich00.fwgames.utils;

import me.kaotich00.fwgames.Fwgames;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class ChatUtils {

    public static void sendBroadcastMessage( String message ) {
        Sponge.getServer().getBroadcastChannel().send(formatMessage(message));
    }

    public static Text formatMessage( String message ) {
        return Text.of(TextSerializers.FORMATTING_CODE.deserialize(getPluginPrefix() + " &r&6"  + message));
    }

    public static Text formatErrorMessage( String message ) {
        return Text.of(TextSerializers.FORMATTING_CODE.deserialize(getPluginPrefix() + " &r&c"  + message));
    }

    public static String getPluginPrefix() {
        return "&2&l[&r&a&l" + Fwgames.NAME + "&r&2&l]";
    }

}
