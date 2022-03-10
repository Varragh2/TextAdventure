package com.darragh2.textadventure.maze;

import com.darragh2.textadventure.Direction;
import com.darragh2.textadventure.maze.mapsite.MapSite;

import java.util.ArrayList;

public class MazeBuilder {

    private Maze maze;
    private final ArrayList<ArrayList<MapSite>> mazeArrayList;
    private ArrayList<MapSite> currentColumn;
    private ArrayList<MapSite> prevColumn;
    private int[] startingMapSite = new int[] {0, 0};

    public MazeBuilder() {
        mazeArrayList = new ArrayList<>();
    }

    public MazeBuilder nextColumn() {
        prevColumn = currentColumn;
        currentColumn = new ArrayList<>();
        mazeArrayList.add(currentColumn);
        return this;
    }

    public MazeBuilder addMapSite(MapSite mapSite) {
        currentColumn.add(mapSite);
        int y = currentColumn.size() - 1;

        if (y > 0) {
            //Only if y - 1 >= 0
            //So get() doesn't throw an IndexOutOfBoundsException
            mapSite.setAdjacentMapSite(Direction.NORTH, currentColumn.get(y - 1));
        }

        if (prevColumn == null) {
            //If there is no previous row, return
            return this;
        }
        //y has to be 0 or greater
        mapSite.setAdjacentMapSite(Direction.WEST, prevColumn.get(y));
        return this;
    }

    public int[] getStartingMapSite() {
        return startingMapSite;
    }

    public MazeBuilder setStartingMapSite(int[] startingMapSite) {
        this.startingMapSite = startingMapSite;
        return this;
    }

    public Maze build() {
        Maze maze = new Maze(mazeArrayList);
        maze.setStartingMapSite(startingMapSite);
        return maze;
    }
}
