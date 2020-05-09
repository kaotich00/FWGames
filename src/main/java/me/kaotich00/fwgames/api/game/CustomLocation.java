package me.kaotich00.fwgames.api.game;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface CustomLocation {

    Location<World> buildLocation();

    Location<World> getLocation();

}
