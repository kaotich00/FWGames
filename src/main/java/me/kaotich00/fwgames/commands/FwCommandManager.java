package me.kaotich00.fwgames.commands;

import me.kaotich00.fwgames.Fwgames;
import org.spongepowered.api.Sponge;

public class FwCommandManager {

    private Fwgames plugin;

    public FwCommandManager(Fwgames plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        Sponge.getCommandManager().register(plugin, FwGameCommand.build(), "fwgames", "fwgame", "fwg");
    }

}
