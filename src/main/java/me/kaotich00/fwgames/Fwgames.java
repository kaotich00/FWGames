package me.kaotich00.fwgames;

import com.google.inject.Inject;
import me.kaotich00.fwgames.api.game.GameService;
import me.kaotich00.fwgames.commands.FwCommandManager;
import me.kaotich00.fwgames.game.SimpleGameService;
import me.kaotich00.fwgames.listeners.PlayerJoinListener;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.IOException;

@Plugin(id = Fwgames.ID, name = Fwgames.NAME, version = Fwgames.VERSION, description = Fwgames.DESCRIPTION)
public class Fwgames {

    public static final String ID = "fwgames";
    public static final String NAME = "fwgames";
    public static final String VERSION = "0.0.1";
    public static final String DESCRIPTION = "A game event manager for server minigames";

    @Inject
    private PluginContainer container;

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Succussfully loaded [" + NAME + "] version " + VERSION );
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event ) {
        Sponge.getServiceManager().setProvider( this.container, GameService.class, new SimpleGameService());
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        registerCommands();
        registerListeners();
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event){

    }

    private void registerListeners(){
        Sponge.getEventManager().registerListeners(this.container, new PlayerJoinListener());
    }

    private void registerCommands() {
        final FwCommandManager commandManager = new FwCommandManager(this);
        commandManager.registerCommands();
    }

    public Logger getLogger() {
        return logger;
    }
}
