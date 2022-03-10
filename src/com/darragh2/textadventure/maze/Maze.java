package com.darragh2.textadventure.maze;

import com.darragh2.textadventure.Player;
import com.darragh2.textadventure.ReadInput;
import com.darragh2.textadventure.maze.mapsite.MapSite;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Stores the Maze a Player can walk around in.
 * May want to use (x, y) to get specific MapSites.
 * Shouldn't matter how the maze is represented, either an ArrayList<ArrayList<MapSite>> or a graph database
 */
public class Maze {

    private ArrayList<ArrayList<MapSite>> maze;
    private Player player;
    private int[] startingMapSite = new int[]{0, 0};
    private ReadInput<MapSite> defaultCommands;

    public Maze(ArrayList<ArrayList<MapSite>> maze) {
        this.maze = maze;

        defaultCommands = new ReadInput<>();
        defaultCommands.putCommand("move", mapSite1 -> mapSite1.getPlayer().move());
        defaultCommands.putCommand("turn left", mapSite1 -> mapSite1.getPlayer().turnLeft());
        defaultCommands.putCommand("turn right", mapSite1 -> mapSite1.getPlayer().turnRight());
    }

    public MapSite getMapSite(int x, int y)
    {
        return maze.get(x).get(y);
    }

    public void setMapSite(int x, int y, MapSite mapSite)
    {
        maze.get(x).set(y, mapSite);
    }

    public void addMapSite(MapSite mapSite)
    {
        maze.get(maze.size() - 1).add(mapSite);
    }

    public void addColumn(ArrayList<MapSite> column) { maze.add(column); }

    public void setMaze(ArrayList<ArrayList<MapSite>> maze) {
        this.maze = maze;
    }

    public ArrayList<ArrayList<MapSite>> getMaze()
    {
        return maze;
    }

    public ListIterator<ArrayList<MapSite>> getMazeIterator()
    {
        return maze.listIterator();
    }

    public void setStartingMapSite(int[] xy) {
        startingMapSite = xy;
    }

    /**
     * Gets Player. If no Player has been instantiated, instantiate a new one
     * @return the requested Player object
     */
    public Player getPlayer() {
        if (player == null) {
            player = new Player(getMapSite(startingMapSite[0], startingMapSite[1]), defaultCommands);
        }
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ReadInput<MapSite> getDefaultCommands() {
        return defaultCommands;
    }

    public void setDefaultCommands(ReadInput<MapSite> defaultCommands) {
        this.defaultCommands = defaultCommands;
    }
}
