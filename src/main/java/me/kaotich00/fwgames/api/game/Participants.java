package me.kaotich00.fwgames.api.game;

import me.kaotich00.fwgames.data.FwLocation;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public interface Participants {

    String getName();

    UUID getUniqueId();

    FwLocation getOriginalPosition();

}
