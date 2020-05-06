package me.kaotich00.fwgames.game;

import me.kaotich00.fwgames.api.game.Participants;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class FwParticipants implements Participants {

    private String name;
    private UUID uniqueId;
    private Location<World> originalPosition;

    public FwParticipants(Player player){
        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
        this.originalPosition = player.getLocation();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public Location<World> getOriginalPosition() {
        return this.originalPosition;
    }
}
