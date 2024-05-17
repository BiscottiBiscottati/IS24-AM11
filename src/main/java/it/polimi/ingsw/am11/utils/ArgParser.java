package it.polimi.ingsw.am11.utils;

import it.polimi.ingsw.am11.utils.exceptions.ParsingErrorException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArgParser {
    private final List<Option> options;
    private final List<String> positionalArgs;

    public ArgParser() {
        this.options = new ArrayList<>(4);
        this.positionalArgs = new ArrayList<>(4);
    }

    public void parse(String @NotNull [] args) throws ParsingErrorException {
        for (int i = 0; i < args.length; i++) {
            if (args[i].isEmpty()) throw new ParsingErrorException("Argument " + i + " is empty");
            if (args[i].charAt(0) == '-') {
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

    public Optional<Option> getOption(String name) {
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
        this.options.add(option);
    }

    public void addOption(String name, String description, String defaultValue) {
        this.options.add(new Option(name, description, defaultValue));
    }

    public void addOption(String name, String description, boolean hasValue) {
        this.options.add(new Option(name, description, hasValue));
    }
}
