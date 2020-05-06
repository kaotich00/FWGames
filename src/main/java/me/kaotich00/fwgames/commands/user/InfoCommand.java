package me.kaotich00.fwgames.commands.user;

import me.kaotich00.fwgames.api.game.Game;
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
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class InfoCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if( !(src instanceof Player) ) {
            src.sendMessage(ChatUtils.formatErrorMessage("Only players can run this command"));
            return CommandResult.empty();
        }

        GameService gameService = Sponge.getServiceManager().provideUnchecked(GameService.class);
        String eventName = args.<String>getOne("evento").get();

        if( gameService.isNameAvailable(eventName) ) {
            src.sendMessage(ChatUtils.formatErrorMessage("Non esiste nessun evento con questo nome"));
            return CommandResult.empty();
        }

        Game game = gameService.getGame(eventName);

        final UserStorageService uss = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        String infoMessage =    "&2&l------------------{ &aFW Games &2&l}------------------\n" +
                                "&aNome evento: &r " + eventName + "\n" +
                                "&aOrganizzatore: &r " + uss.get(game.getManager()).get().getName() + " \n" +
                                "&aData creazione: &r " + game.getCreationDate().toString() + " \n" +
                                "&aPartecipanti: &r " + game.getParticipants().size() + " \n";
        src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(infoMessage));

        return CommandResult.success();
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("evento"))))
                .executor(new InfoCommand())
                .build();
    }

}
