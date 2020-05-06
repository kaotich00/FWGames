package me.kaotich00.fwgames.api.game;

import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;
import java.util.Optional;

public interface GameService {

    void createGame(Player player, String eventName);

    boolean addPlayerToGame(Player player, String eventName);

    boolean removePlayerFromGame(Player player, String eventName);

    boolean isNameAvailable(String name);

    Optional<Participants> isPlayerInGame(Player player, String eventName);

    Map getGamesList();

    boolean isStarted(String eventName);

    Game getGame( String eventName );

    void startGame(String eventName);

    void endGame(String eventName);

    boolean isGameManager(Player player, String eventName);

}
