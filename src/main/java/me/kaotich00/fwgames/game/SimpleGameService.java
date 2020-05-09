package me.kaotich00.fwgames.game;

import me.kaotich00.fwgames.api.game.Game;
import me.kaotich00.fwgames.api.game.GameService;
import me.kaotich00.fwgames.api.game.Participants;
import me.kaotich00.fwgames.data.FwLocation;
import me.kaotich00.fwgames.utils.ChatUtils;
import me.kaotich00.fwgames.utils.GameUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SimpleGameService implements GameService {

    private Map<String, Game> gamesList = new HashMap();
    private Map<UUID, FwLocation> disconnectedBeforeEnd = new HashMap();
    private File configDir;

    public SimpleGameService(File configDir){
        this.configDir = configDir;
        if(!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    @Override
    public void createGame(Player manager, String eventName) {

        // Create a new Game instance
        FwGame newGame = new FwGame(eventName, manager.getUniqueId(), manager.getLocation());
        gamesList.put(eventName,newGame);

        // Send the message to broadcast
        String message = "&r&6L'evento &a"+ eventName +"&6 sta per cominciare, digita \n &a/fwgames join "+ eventName +" &r&6per partecipare. ATTENZIONE: l'inventario viene cancellato in automatico.";
        ChatUtils.sendBroadcastMessage( message );

        // Save changes
        saveGames();

    }

    @Override
    public void startGame(String eventName) {
        Game game = getGame(eventName);
        game.setStatus( GameUtils.GAME_STARTED );

        // Send the message to broadcast
        String message = "&r&6L'evento &a"+ eventName +"&6 e' appena partito. Da questo momento non sara' piu' possibile joinare.";
        ChatUtils.sendBroadcastMessage( message );

        // Save changes
        saveGames();
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
                    this.disconnectedBeforeEnd.put( optUser.get().getUniqueId(), participants.getOriginalPosition() );
                }
                optUser.get().setLocation(participants.getOriginalPosition().getLocation().getPosition(), participants.getOriginalPosition().getLocation().getExtent().getUniqueId());
                optUser.get().getInventory().clear();
            }
        });

        // Remove entries
        this.gamesList.remove(eventName);

        // Send the message to broadcast
        String message = "&r&6L'evento &a"+ eventName +"&6 e' terminato. Grazie a tutti i partecipanti. Alla prossima!";
        ChatUtils.sendBroadcastMessage( message );

        // Save changes
        saveGames();
    }

    @Override
    public Game getGame(String eventName) {
        return this.gamesList.get(eventName);
    }

    @Override
    public boolean isStarted(String eventName) {
        return gamesList.get(eventName).getStatus() == GameUtils.GAME_STARTED;
    }

    @Override
    public Map getGamesList() {
        return null;
    }

    @Override
    public boolean isNameAvailable(String name) {
        return !gamesList.containsKey(name);
    }

    @Override
    public void saveGames() {
        File gamesFile = new File(configDir, "games");
        File disconnectedFile = new File(configDir, "disconnected");
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(gamesFile));
            objectOutputStream.writeObject(gamesList);
            objectOutputStream.flush();
            objectOutputStream.close();

            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new FileOutputStream(disconnectedFile));
            objectOutputStream2.writeObject(disconnectedBeforeEnd);
            objectOutputStream2.flush();
            objectOutputStream2.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void loadGames() {
        HashMap<String,Game> gamesList = new HashMap<>();
        File gamesFile = new File(configDir, "games");
        if(gamesFile.exists()){
            try{
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(gamesFile));
                this.gamesList = (HashMap<String, Game>) objectInputStream.readObject();
                objectInputStream.close();
            }catch (IOException | ClassNotFoundException e){
                this.gamesList = gamesList;
            }
        }else{
            this.gamesList = gamesList;
        }

        HashMap<UUID,FwLocation> disconnectedPlayers = new HashMap<>();
        File disconnectedFile = new File(configDir, "disconnected");
        if( disconnectedFile.exists() ) {
            try{
                ObjectInputStream objectInputStream2 = new ObjectInputStream(new FileInputStream(disconnectedFile));
                this.disconnectedBeforeEnd = (HashMap<UUID, FwLocation>) objectInputStream2.readObject();
                objectInputStream2.close();
            }catch (IOException | ClassNotFoundException e){
                this.disconnectedBeforeEnd = disconnectedPlayers;
            }
        }else{
            this.disconnectedBeforeEnd = disconnectedPlayers;
        }
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
        player.transferToWorld( game.getSpawnPoint().getLocation().getExtent(), game.getSpawnPoint().getLocation().getPosition() );

        return true;
    }

    @Override
    public boolean removePlayerFromGame(Player player, String eventName) {

        // Get the participant
        Participants participant = isPlayerInGame(player,eventName).get();

        // Remove participant from game
        Game game = gamesList.get(eventName);
        game.getParticipants().remove(participant);

        // Send leave message to player
        String message = "Hai lasciato l'evento &a" + eventName;
        player.sendMessage( ChatUtils.formatMessage(message) );

        // Send leave message to manager
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
        player.transferToWorld( participant.getOriginalPosition().getLocation().getExtent(), participant.getOriginalPosition().getLocation().getPosition());

        return true;
    }

    @Override
    public Optional<Participants> isPlayerInGame(Player player, String eventName) {
        Game game = gamesList.get(eventName);
        return game.getParticipants().stream().filter(p -> p.getUniqueId().equals(player.getUniqueId())).findAny();
    }

    @Override
    public boolean isGameManager(Player player, String eventName) {

        Game game = getGame(eventName);

        if( game == null )
            return false;

        return game.getManager().equals(player.getUniqueId());
    }

    @Override
    public FwLocation wasPlayerInEvent(UUID player) {
        return disconnectedBeforeEnd.containsKey(player) ? disconnectedBeforeEnd.get(player) : null;
    }

    @Override
    public void removePlayerFromDisconnected(UUID player) {
        this.disconnectedBeforeEnd.remove(player);
    }


}
