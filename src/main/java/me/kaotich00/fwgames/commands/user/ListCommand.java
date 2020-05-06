package me.kaotich00.fwgames.commands.user;

import me.kaotich00.fwgames.Fwgames;
import me.kaotich00.fwgames.api.game.Game;
import me.kaotich00.fwgames.api.game.GameService;
import me.kaotich00.fwgames.commands.admin.CreateCommand;
import me.kaotich00.fwgames.utils.ChatUtils;
import me.kaotich00.fwgames.utils.NameUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListCommand implements CommandExecutor {

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

        if( !gameService.isPlayerInGame(player, eventName).isPresent() ) {
            src.sendMessage(ChatUtils.formatErrorMessage("Non fai parte di questo evento"));
            return CommandResult.empty();
        }

        Game game = gameService.getGame(eventName);

        final UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        final Set<Text> participants = game.getParticipants()
                                            .stream()
                                            .map(p -> uss.get(p.getUniqueId())
                                                    .map(NameUtils::getDisplayName)
                                                    .orElse(Text.of()))
                                            .collect(Collectors.toSet());
        src.sendMessage( ChatUtils.formatMessage(Text.joinWith(Text.of(","), participants).toPlain()) );
        return CommandResult.success();
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("evento"))))
                .executor(new ListCommand())
                .build();
    }

}
