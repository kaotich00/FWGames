package me.kaotich00.fwgames.commands.user;

import me.kaotich00.fwgames.api.game.GameService;
import me.kaotich00.fwgames.utils.ChatUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class JoinCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if( !(src instanceof Player) ) {
            src.sendMessage(ChatUtils.formatErrorMessage("Only players can run this command"));
            return CommandResult.empty();
        }

        Player player = (Player) src;

        GameService gameService = Sponge.getServiceManager().provideUnchecked(GameService.class);
        String eventName = args.<String>getOne("evento").get();

        if( gameService.isNameAvailable(eventName) ) {
            src.sendMessage(ChatUtils.formatErrorMessage("Non esiste nessun evento con questo nome"));
            return CommandResult.empty();
        }

        if( gameService.isPlayerInGame(player, eventName).isPresent() ) {
            src.sendMessage(ChatUtils.formatErrorMessage("Sei gia' parte di questo evento"));
            return CommandResult.empty();
        }

        if( !gameService.isStarted(eventName) ) {
            src.sendMessage(ChatUtils.formatErrorMessage("L'evento è attualmente in corso, impossibile joinare"));
            return CommandResult.empty();
        }

        gameService.addPlayerToGame(player,eventName);
        return CommandResult.success();
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .arguments( GenericArguments.onlyOne( GenericArguments.string( Text.of("evento") ) ) )
                .executor( new JoinCommand() )
                .build();
    }

}
