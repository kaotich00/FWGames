package me.kaotich00.fwgames.commands;

import me.kaotich00.fwgames.Fwgames;
import me.kaotich00.fwgames.commands.admin.CreateCommand;
import me.kaotich00.fwgames.commands.admin.EndCommand;
import me.kaotich00.fwgames.commands.admin.StartCommand;
import me.kaotich00.fwgames.commands.user.InfoCommand;
import me.kaotich00.fwgames.commands.user.JoinCommand;
import me.kaotich00.fwgames.commands.user.LeaveCommand;
import me.kaotich00.fwgames.commands.user.ListCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.serializer.TextSerializers;

public class FwGameCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String helpMessage =    "&2&l------------------{ &aFW Games &2&l}------------------\n" +
                                "&rAlternative: &a/fwgames, /fwgame, /fwg\n\n";
        if( src.hasPermission(Fwgames.NAME + ".admin") ){
            helpMessage +=  "&a/fwgame create <evento> &r- Crea un nuovo evento\n" +
                            "&a/fwgame start <evento> &r- Fa partire l'evento\n" +
                            "&a/fwgame end <evento> &r- Termina l'evento\n";
        }
        helpMessage +=  "&a/fwgame join <evento> &r- Ti permette di entrare in un evento\n" +
                        "&a/fwgame leave <evento> &r- Ti permette di uscire dall'evento\n" +
                        "&a/fwgame list <evento> &r- Mostra la lista dei giocatori presenti in un evento\n" +
                        "&a/fwgame info <evento> &r- Mostra le informazioni su un evento";

        src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(helpMessage));

        return CommandResult.success();
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .executor(new FwGameCommand())
                .child(CreateCommand.build(), "create", "init")
                .child(JoinCommand.build(), "join")
                .child(LeaveCommand.build(), "leave")
                .child(InfoCommand.build(), "info")
                .child(ListCommand.build(), "list")
                .child(StartCommand.build(), "start")
                .child(EndCommand.build(), "end")
                .build();
    }

}
