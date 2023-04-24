package view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {
    @NotNull
    public String command;
    @Nullable
    public String subCommand;
    @NotNull
    public HashMap<String, String> options;

    private ParsedLine(@NotNull String command) {
        this.command = command;
        this.subCommand = null;
        this.options = new HashMap<>();
    }

    private static final String argumentRegex = "((?<argument>([^\\s\\-\"]+)|(-\\d+))|(\"(?<quotedArgument>[^\"]*)\"))",
            optionRegex = "((?<optionName>--?[^\\s\\-\\d][^\\s\\-]*)(?<arguments>(\\s+" + argumentRegex + "))?)",
    //an option cannot have more than one argument
    commandRegex = "(\\s*(?<command>\\S+)(\\s+(?<subcommand>[^\\s\\-]+))?(?<options>(\\s+" + optionRegex + ")+)?\\s*)";

    private static final Pattern argumentPattern = Pattern.compile(argumentRegex),
            optionPattern = Pattern.compile(optionRegex),
            commandPattern = Pattern.compile(commandRegex);

    @Nullable
    public static ParsedLine parseLine(@NotNull String line) {
        Matcher commandMatcher = commandPattern.matcher(line);
        if (commandMatcher.matches()) {
            ParsedLine parsedLine = new ParsedLine(commandMatcher.group("command"));

            String subcommand = commandMatcher.group("subcommand");
            if (subcommand != null) parsedLine.subCommand = subcommand;

            String options = commandMatcher.group("options");
            if (options != null) {
                Matcher optionMatcher = optionPattern.matcher(options);
                while (optionMatcher.find()) {
                    ArrayList<String> argumentsArrayList = new ArrayList<>();

                    String arguments = optionMatcher.group("arguments");
                    if (arguments != null) {
                        Matcher argumentMatcher = argumentPattern.matcher(arguments);
                        while (argumentMatcher.find()) {
                            String argument = argumentMatcher.group("argument");
                            argumentsArrayList.add(
                                    (argument != null) ? argument : argumentMatcher.group("quotedArgument"));
                        }
                    }

                    String argument = argumentsArrayList.isEmpty() ? null : argumentsArrayList.get(0);
                    parsedLine.options.put(optionMatcher.group("optionName"), argument);
                }
            }

            return parsedLine;
        }

        return null;
    }
}
