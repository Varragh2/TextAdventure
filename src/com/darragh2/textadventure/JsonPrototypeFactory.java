package com.darragh2.textadventure;

import com.darragh2.textadventure.maze.Maze;
import com.darragh2.textadventure.maze.MazeBuilder;
import com.darragh2.textadventure.maze.mapsite.MapSite;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A prototype factory initialized with calls to register() of the prototypes to instantiate from passed Json file
 */
public class JsonPrototypeFactory {

    private Gson gson;
    private JsonReader reader;
    private static HashMap<String, Class<? extends MapSite>> prototypes;

    public JsonPrototypeFactory(Reader json) {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                ArrayList<String> restrictions = new ArrayList<>();
                restrictions.add("adjacentMapSites");
                return restrictions.contains(fieldAttributes.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                ArrayList<Class<?>> restrictions = new ArrayList<>();
                restrictions.add(Player.class);
                restrictions.add(ReadInput.class);
                return restrictions.contains(aClass);
            }
        });
        gson = builder.create();
        reader = new JsonReader(json);
        prototypes = new HashMap<>();
    }

    /**
     * Registers a name and a class to the list of prototypes that can be read in from file
     * @param name the name to be registered. Must match name in file
     * @param mapSite the class that is read from file. Must conform to the MapSite interface
     */
    public void register(String name, Class<? extends MapSite> mapSite) {
        prototypes.put(name, mapSite);
    }

    /**
     * Parses json into Maze filled with MapSites
     */
    public Maze read() throws IOException {
        MazeBuilder builder = new MazeBuilder();

        //Begins the object
        reader.beginObject();
        while (!reader.peek().equals(JsonToken.END_OBJECT)) {
            builder.nextColumn();
            readColumn(builder);
        }
        return builder.build();
    }

    /**
     * reads a column of MapSites
     * @throws IOException because it needs it
     */
    private void readColumn(MazeBuilder builder) throws IOException {
        //First name should be column
        if (!reader.nextName().equals("column")) {
            throw new JsonSyntaxException("column should be the first name in json");
        }
        reader.beginArray();
        //Keep iterating until the end of column
        while (!reader.peek().equals(JsonToken.END_ARRAY)) {
            readMapSite(builder);
        }
        reader.endArray();
    }

    /**
     * Reads individual MapSites from Json
     * @throws IOException because it is required
     */
    private void readMapSite(MazeBuilder builder) throws IOException {
        reader.beginObject();
        reader.nextName();
        String name = reader.nextString();
        reader.nextName();
        //Reads MapSite from gson based on the registered prototype
        MapSite map = gson.fromJson(reader, prototypes.get(name));
        builder.addMapSite(map);
        reader.endObject();
    }

}

