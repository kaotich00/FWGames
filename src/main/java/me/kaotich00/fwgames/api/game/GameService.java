package me.kaotich00.fwgames.api.game;

import org.spongepowered.api.entity.living.player.Player;

import java.util.Map;

public interface GameService {

    void createGame(Player player, String name);

    boolean addPlayerToGame();

    boolean removePlayerFromGame();

    boolean isNameAvailable(String name);

    Map getGamesList();

}
