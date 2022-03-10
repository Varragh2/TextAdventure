package com.darragh2.textadventure.maze.mapsite;

import com.darragh2.textadventure.Direction;
import com.darragh2.textadventure.ReadInput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;


public class Room extends MapSite
{

    //descriptions is a hashtable that contains the descriptions for the rooms
    //adjacentMapSites is a hashtable that keeps track of which rooms this room is next to

    private HashMap<Direction, String> descriptions = new HashMap<>();
    private int numRoom;

    public Room() {
        this( new String[]{"", "", "", ""});
    }

    public Room(String[] desc) {
        this(desc, new ReadInput<>());
    }

    public Room(String[] desc, ReadInput<MapSite> readInput) {
        super();
        setDescriptions(desc);
    }

    public void setDescriptions(String[] str) {
        if (str.length < 4) {
            throw new IndexOutOfBoundsException("str should have at least 4 items in the list");
        }
        Iterator<String> strings = Arrays.stream(str).iterator();
        Arrays.stream(Direction.values()).iterator().forEachRemaining( (direction) -> {
            descriptions.put(direction, strings.next());
        });
    }

    public String getDescription(Direction direction) {
        return descriptions.get(direction);
    }

    public int getNumRoom() {
        return numRoom;
    }
}