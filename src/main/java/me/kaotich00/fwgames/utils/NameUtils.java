package me.kaotich00.fwgames.utils;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class NameUtils {

    public static Text getDisplayName(User user) {
        return user.get(Keys.DISPLAY_NAME).orElse(Text.of(user.getName()));
    }

}
