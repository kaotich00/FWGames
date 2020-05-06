package me.kaotich00.fwgames;

import com.google.inject.Inject;
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

    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        registerListeners();
    }

    private void registerListeners() {
        logger.info("[" + NAME + "] Successfully registered listeners" );
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event){

    }

    private void registerConfigService() throws IOException, ObjectMappingException {

    }

    public Logger getLogger() {
        return logger;
    }
}
