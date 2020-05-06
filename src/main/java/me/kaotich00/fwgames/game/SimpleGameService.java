package me.kaotich00.fwgames.game;

import me.kaotich00.fwgames.api.game.Game;
import me.kaotich00.fwgames.api.game.GameService;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;

public class SimpleGameService implements GameService {

    private Map<String, Game> gamesList = new HashMap();

    @Override
    public void createGame(Player manager, String name) {
        // Create a new Game instance
        FwGame newGame = new FwGame(name, manager.getUniqueId(), manager.getLocation());
        gamesList.put(name,newGame);
        // Send the message to broadcast

    }

    @Override
    public boolean addPlayerToGame() {
        return false;
    }

    @Override
    public boolean removePlayerFromGame() {
        return false;
    }

    @Override
    public boolean isNameAvailable(String name) {
        return !gamesList.containsKey(name);
    }

    @Override
    public Map getGamesList() {
        return null;
    }
}
