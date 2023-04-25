package view;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class Command {
    public final String command;
    @Nullable
    public final String subcommand;
    public final Consumer<ParsedLine> util;

    public Command(String command, @Nullable String subcommand, Consumer<ParsedLine> util) {
        this.command = command;
        this.subcommand = subcommand;
        this.util = util;
    }

}
