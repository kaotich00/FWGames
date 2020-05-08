package me.kaotich00.fwgames.listeners;

import me.kaotich00.fwgames.api.game.GameService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class PlayerJoinListener {

    @Listener( order = Order.FIRST, beforeModifications = true)
    public void onPlayerBanEvent( ClientConnectionEvent.Join event ){

        final GameService gameService = Sponge.getServiceManager().provideUnchecked(GameService.class);

        // If the player was in a game before the event ended
        Location<World> originalPosition = gameService.wasPlayerInEvent(event.getTargetEntity().getUniqueId());
        if( originalPosition != null ) {
            Player player = event.getTargetEntity();
            player.setLocation(originalPosition.getPosition(), originalPosition.getExtent().getUniqueId());
            gameService.removePlayerFromDisconnected(player.getUniqueId());
        }

    }

}
