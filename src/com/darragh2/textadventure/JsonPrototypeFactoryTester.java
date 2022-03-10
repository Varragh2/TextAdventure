package com.darragh2.textadventure;

import com.darragh2.textadventure.maze.Maze;
import com.darragh2.textadventure.maze.MazeBuilder;
import com.darragh2.textadventure.maze.mapsite.*;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JsonPrototypeFactoryTester {

    private static final String maze_json = "C:\\Users\\Thomas Nolan\\IdeaProjects\\TextAdventure\\src\\com\\darragh2\\textadventure\\resources\\Maze.json";
    private static final String map_json = "C:\\Users\\Thomas Nolan\\IdeaProjects\\TextAdventure\\src\\com\\darragh2\\textadventure\\resources\\Map.json";

    public static void main(String[] args) throws IOException {

        JsonPrototypeFactory prototypeFactory = new JsonPrototypeFactory(new FileReader(maze_json));

        prototypeFactory.register("room", Room.class);
        prototypeFactory.register("door", Door.class);
        Maze maze1 = prototypeFactory.read();

        Gson gson = new GsonBuilder().setPrettyPrinting().setExclusionStrategies(new ExclusionStrategy() {
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
        }).create();


        ReadInput<MapSite> defaultCommands = new ReadInput<>();
        defaultCommands.putCommand("move", mapSite -> mapSite.getPlayer().move());
        defaultCommands.putCommand("turn left", mapSite -> mapSite.getPlayer().turnLeft());
        defaultCommands.putCommand("turn right", mapSite -> mapSite.getPlayer().turnRight());
        defaultCommands.putCommand("turn around", mapSite -> mapSite.getPlayer().turnAround());
        defaultCommands.putCommand("direction", mapSite -> System.out.println("You are facing " + mapSite.getPlayer().getDirection()));
        defaultCommands.putCommand("help", s -> System.out.println("Commands: " + String.join(", ", s.getCurrentCommands().getCommands().keySet().stream().filter( key -> !key.equals("help") ).collect(Collectors.toSet()))));


        ReadInput<MapSite> readInputRoom1 = new ReadInput<>();
        readInputRoom1.putCommand( "eat", mapSite -> System.out.println("Player ate") );
        Room room1 = new Room(new String[]{"Room1North", "Room1East", "Room1South", "Room1West"}, readInputRoom1);


        MapSite room2 = new MapSite() {
            //This nice piece of Java syntax { } is an initializer block that can be used in place of an explicit constructor in an anonymous class.
            {
                ReadInput<MapSite> readInputRoom2 = new ReadInput<>();
                readInputRoom2.putCommand("die", mapSite -> mapSite.getPlayer().setAlive(false));
                setDefaultCommands(readInputRoom2);
            }

            @Override
            public String getDescription(Direction direction) {
                return switch (direction) {
                    case NORTH -> "Back to Room1";
                    case EAST -> "A wall in front of you";
                    case SOUTH -> "No more Rooms South";
                    case WEST -> "West";
                };
            }
        };

        Maze maze = new MazeBuilder().nextColumn().addMapSite(room1).addMapSite(room2).addMapSite(new StoppedRoom()).nextColumn().addMapSite(new TriviaRoom()).build();

        Player player = new Player(maze.getMapSite(0, 0), defaultCommands);
        while (player.getPlaying()) {
            player.runCommands();
        }


        /**
        Player player = new Player(maze.getDefaultMapSite());
        player.enter();
        while (player.alive()) {
            ReadInput readInput = maze.getCommands();
            //should be the super ReadInput
            readInput.runCommands();
         }

         **/
    }
}
