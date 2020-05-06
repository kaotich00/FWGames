package me.kaotich00.fwgames.game;

import me.kaotich00.fwgames.api.game.Game;
import me.kaotich00.fwgames.api.game.Participants;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FwGame implements Game {

    private String name;
    private UUID manager;
    private Instant creationDate;
    private short status; // 0 - init, 1 - started
    private Set<Participants> participants;
    private Location<World> spawnPoint;

    public FwGame(String name, UUID manager, Location<World> spawnPoint) {
        this.name = name;
        this.manager = manager;
        this.creationDate = Instant.now();
        this.status = 0;
        this.participants = new HashSet<>();
        this.spawnPoint = spawnPoint;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getManager() {
        return this.manager;
    }

    @Override
    public Instant getCreationDate() {
        return this.creationDate;
    }

    @Override
    public short getStatus() {
        return this.status;
    }

    @Override
    public Set<Participants> getParticipants() {
        return this.participants;
    }

    @Override
    public Location<World> getSpawnPoint() {
        return this.spawnPoint;
    }
}
