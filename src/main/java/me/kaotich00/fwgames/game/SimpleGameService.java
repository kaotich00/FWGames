package me.kaotich00.fwgames.game;

import me.kaotich00.fwgames.api.game.Game;
import me.kaotich00.fwgames.api.game.GameService;
import me.kaotich00.fwgames.api.game.Participants;
import me.kaotich00.fwgames.utils.ChatUtils;
import me.kaotich00.fwgames.utils.GameUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SimpleGameService implements GameService {

    private Map<String, Game> gamesList = new HashMap();
    private Map<UUID, Location<World>> playersDisonnectedDuringEvent = new HashMap();

    @Override
    public void createGame(Player manager, String eventName) {

        // Create a new Game instance
        FwGame newGame = new FwGame(eventName, manager.getUniqueId(), manager.getLocation());
        gamesList.put(eventName,newGame);
        // Send the message to broadcast
        String message = "&r&6L'evento &a"+ eventName +"&6 sta per cominciare, digita \n &a/fwgames join "+ eventName +" &r&6per partecipare. ATTENZIONE: l'inventario viene cancellato in automatico.";
        ChatUtils.sendBroadcastMessage( message );

    }

    @Override
    public boolean addPlayerToGame(Player player, String eventName) {

        // Create a new participant
        final Participants participant = new FwParticipants(player);

        // Add participant to game
        Game game = gamesList.get(eventName);
        game.getParticipants().add(participant);

        // Send join message to player
        String message = "Sei entrato con successo nell'evento &a" + eventName;
        player.sendMessage( ChatUtils.formatMessage(message) );

        // Send join message to manager
        final UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        String joinMessage = "L'utente &a" + player.getName() + "&r&6 e' entrato nell'evento";
        Optional<User> manager = uss.get(game.getManager());
        if(manager.isPresent()) {
            if(manager.get().isOnline()) {
                manager.get().getPlayer().get().sendMessage(ChatUtils.formatMessage(joinMessage));
            }
        }

        // Teleport player to event
        player.getInventory().clear();
        player.transferToWorld( game.getSpawnPoint().getExtent(), game.getSpawnPoint().getPosition() );

        return true;
    }

    @Override
    public boolean removePlayerFromGame(Player player, String eventName) {

        // Get the participant
        Participants participant = isPlayerInGame(player,eventName).get();

        // Add participant to game
        Game game = gamesList.get(eventName);
        game.getParticipants().remove(participant);

        // Send join message to player
        String message = "Hai lasciato l'evento &a" + eventName;
        player.sendMessage( ChatUtils.formatMessage(message) );

        // Send join message to manager
        final UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        String joinMessage = "&cL'utente &a" + player.getName() + "&r&c ha lasciato l'evento";
        Optional<User> manager = uss.get(game.getManager());
        if(manager.isPresent()) {
            if(manager.get().isOnline()) {
                manager.get().getPlayer().get().sendMessage(ChatUtils.formatMessage(joinMessage));
            }
        }

        // Teleport player back to origin
        player.getInventory().clear();
        player.transferToWorld( participant.getOriginalPosition().getExtent(), participant.getOriginalPosition().getPosition() );

        return true;
    }

    @Override
    public boolean isNameAvailable(String name) {
        return !gamesList.containsKey(name);
    }

    @Override
    public Optional<Participants> isPlayerInGame(Player player, String eventName) {
        Game game = gamesList.get(eventName);
        return game.getParticipants().stream().filter(p -> p.getUniqueId().equals(player.getUniqueId())).findAny();
    }

    @Override
    public Map getGamesList() {
        return null;
    }

    @Override
    public boolean isStarted(String eventName) {
        return gamesList.get(eventName).getStatus() == GameUtils.GAME_STARTED;
    }

    @Override
    public Game getGame(String eventName) {
        return this.gamesList.get(eventName);
    }

    @Override
    public void startGame(String eventName) {
        Game game = getGame(eventName);
        game.setStatus( GameUtils.GAME_STARTED );

        // Send the message to broadcast
        String message = "&r&6L'evento &a"+ eventName +"&6 e' appena partito. Da questo momento non sara' piu' possibile joinare.";
        ChatUtils.sendBroadcastMessage( message );
    }

    @Override
    public void endGame(String eventName) {
        Game game = getGame(eventName);

        // Teleport all players to original position
        game.getParticipants().forEach(participants -> {
            final UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
            Optional<User> optUser = uss.get(participants.getUniqueId());
            if( optUser.isPresent() ) {
                if( !optUser.get().isOnline() ) {
                    this.playersDisonnectedDuringEvent.put( optUser.get().getUniqueId(), participants.getOriginalPosition() );
                }
                optUser.get().setLocation(participants.getOriginalPosition().getPosition(), participants.getOriginalPosition().getExtent().getUniqueId());
                optUser.get().getInventory().clear();
            }
        });

        // Remove entries
        this.gamesList.remove(eventName);

        // Send the message to broadcast
        String message = "&r&6L'evento &a"+ eventName +"&6 e' terminato. Grazie a tutti i partecipanti. Alla prossima!";
        ChatUtils.sendBroadcastMessage( message );
    }

    @Override
    public boolean isGameManager(Player player, String eventName) {

        Game game = getGame(eventName);

        if( game == null )
            return false;

        return game.getManager().equals(player.getUniqueId());
    }

    @Override
    public Location<World> wasPlayerInEvent(UUID player) {
        return playersDisonnectedDuringEvent.containsKey(player) ? playersDisonnectedDuringEvent.get(player) : null;
    }

    @Override
    public void removePlayerFromDisconnected(UUID player) {
        this.playersDisonnectedDuringEvent.remove(player);
    }


}
