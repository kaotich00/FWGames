package me.kaotich00.fwgames.game;

import me.kaotich00.fwgames.api.game.Game;
import me.kaotich00.fwgames.api.game.Participants;
import me.kaotich00.fwgames.utils.GameUtils;
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
    private int status;
    private Set<Participants> participants;
    private Location<World> spawnPoint;

    public FwGame(String name, UUID manager, Location<World> spawnPoint) {
        this.name = name;
        this.manager = manager;
        this.creationDate = Instant.now();
        this.status = GameUtils.GAME_INIT;
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
    public int getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
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
