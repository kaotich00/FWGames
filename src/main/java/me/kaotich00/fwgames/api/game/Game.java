package me.kaotich00.fwgames.api.game;

import me.kaotich00.fwgames.data.FwLocation;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public interface Game {

    String getName();

    UUID getManager();

    Instant getCreationDate();

    int getStatus();

    void setStatus(int status);

    Set<Participants> getParticipants();

    FwLocation getSpawnPoint();

}
