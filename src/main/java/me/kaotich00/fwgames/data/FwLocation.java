package me.kaotich00.fwgames.data;

import com.flowpowered.math.vector.Vector3d;
import me.kaotich00.fwgames.api.game.CustomLocation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.Serializable;
import java.util.UUID;

public class FwLocation implements CustomLocation, Serializable {

    private UUID worldUUID;
    private double positionX, positionY, positionZ;
    private transient Location<World> playerLocation;

    public FwLocation( Location<World> location ) {
        this.playerLocation = location;
        this.worldUUID = location.getExtent().getUniqueId();
        this.positionX = location.getX();
        this.positionY = location.getY();
        this.positionZ = location.getZ();
    }

    @Override
    public Location<World> buildLocation() {
        World world = Sponge.getServer().getWorld(worldUUID).get();
        Vector3d position = new Vector3d( positionX, positionY, positionZ );
        Location<World> location = new Location<World>( world, position );

        return location;
    }

    @Override
    public Location<World> getLocation() {
        if( playerLocation == null )
            playerLocation = buildLocation();
        return playerLocation;
    }
}
