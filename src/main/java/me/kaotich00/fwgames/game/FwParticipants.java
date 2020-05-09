package me.kaotich00.fwgames.game;

import me.kaotich00.fwgames.api.game.Participants;
import me.kaotich00.fwgames.data.FwLocation;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.Serializable;
import java.util.UUID;

public class FwParticipants implements Participants, Serializable {

    private String name;
    private UUID uniqueId;
    private FwLocation originalPosition;

    public FwParticipants(Player player){
        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
        this.originalPosition = new FwLocation( player.getLocation() );
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
    public FwLocation getOriginalPosition() {
        return this.originalPosition;
    }
}
