package it.polimi.ingsw.am11.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class Option {
    private static final Pattern CHECK_NAME = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");

    private final @NotNull String name;
    private final String description;
    private final boolean hasValue;
    @Nullable
    private String value;

    public Option(@NotNull String name, String description, @Nullable String defaultValue) {
        if (! CHECK_NAME.matcher(name).matches())
            throw new IllegalArgumentException("Invalid option name must start with a letter");
        this.name = name;
        this.description = description;
        this.value = defaultValue;
        this.hasValue = true;
    }

    public Option(@NotNull String name, String description, boolean hasValue) {
        this.name = name;
        this.description = description;
        this.hasValue = hasValue;
        this.value = null;
    }

    public String getName() {
        return name;
    }

    public @Nullable String getValue() {
        return value;
    }

    public void setValue(@Nullable String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean hasValue() {
        return hasValue;
    }
}
