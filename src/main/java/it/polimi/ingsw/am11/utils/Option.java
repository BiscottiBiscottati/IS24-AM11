package it.polimi.ingsw.am11.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Option {
    private final String name;
    private final String description;
    private final boolean hasValue;
    @Nullable
    private String value;

    public Option(@NotNull String name, String description, @Nullable String defaultValue) {
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
