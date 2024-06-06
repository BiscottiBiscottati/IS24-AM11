package it.polimi.ingsw.am11.utils;

import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ArgParser {
    private static final Pattern OPTION_START = Pattern.compile("^-[a-zA-Z]\\w*");
    private final @NotNull List<Option> options;
    private final @NotNull List<String> positionalArgs;

    public ArgParser() {
        options = new ArrayList<>(4);
        this.positionalArgs = new ArrayList<>(4);
    }

    public void parse(String @NotNull [] args) throws ParsingErrorException {
        int length = args.length;
        for (int i = 0; i < length; i++) {
            if (args[i].isEmpty()) throw new ParsingErrorException("Argument " + i + " is empty");
            if (OPTION_START.matcher(args[i]).matches()) {
                int tempI = i;
                Option option = getOption(args[i].substring(1))
                        .orElseThrow(
                                () -> new ParsingErrorException("Invalid option: " + args[tempI]));

                if (option.hasValue()) {
                    if (i + 1 >= args.length) {
                        throw new ParsingErrorException("Missing value for option: " + args[i]);
                    }
                    option.setValue(args[++ i]);
                } else {
                    option.setValue("");
                }

            } else {
                this.positionalArgs.add(args[i]);
            }
        }
    }

    public @NotNull Optional<Option> getOption(String name) {
        for (Option option : options) {
            if (option.getName().equals(name)) {
                return Optional.of(option);
            }
        }
        return Optional.empty();
    }

    public @NotNull List<String> getPositionalArgs() {
        return positionalArgs;
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public void addOption(@NotNull String name, String description, String defaultValue) {
        options.add(new Option(name, description, defaultValue));
    }

    public void addOption(@NotNull String name, String description, boolean hasValue) {
        options.add(new Option(name, description, hasValue));
    }
}
