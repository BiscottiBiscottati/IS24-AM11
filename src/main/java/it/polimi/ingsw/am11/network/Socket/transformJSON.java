package it.polimi.ingsw.am11.network.Socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class transformJSON {
    private final ObjectMapper mapper;

    public transformJSON() {
        mapper = new ObjectMapper();
    }

    public @Nullable JsonNode stringToJson(String jsonString) {
        try {
            return mapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public @Nullable String jsonToString(JsonNode json) {
        try {
            return mapper.writeValueAsString(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}