package me.kaotich00.fwgames.api.game;

import me.kaotich00.fwgames.data.FwLocation;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface GameService {

    /* Methods for game management */
    void createGame(Player player, String eventName);

    void startGame(String eventName);

    void endGame(String eventName);

    Game getGame( String eventName );

    boolean isStarted(String eventName);

    Map getGamesList();

    boolean isNameAvailable(String name);

    void saveGames();

    void loadGames();

    /* Methods for participants management */

    boolean addPlayerToGame(Player player, String eventName);

    Optional<Participants> isPlayerInGame(Player player, String eventName);

    boolean removePlayerFromGame(Player player, String eventName);

    boolean isGameManager(Player player, String eventName);

    FwLocation wasPlayerInEvent(UUID player);

    void removePlayerFromDisconnected(UUID player);

}
