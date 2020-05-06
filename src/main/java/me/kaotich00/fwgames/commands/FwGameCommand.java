package me.kaotich00.fwgames.commands;

import me.kaotich00.fwgames.commands.admin.CreateCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

public class FwGameCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return null;
    }

    public static CommandSpec build() {

        return CommandSpec.builder()
                .executor(new FwGameCommand())
                .child(CreateCommand.build(), "create", "init")
                .build();
    }

}
