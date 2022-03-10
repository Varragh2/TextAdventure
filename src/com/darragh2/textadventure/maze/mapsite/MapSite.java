package com.darragh2.textadventure.maze.mapsite;

import com.darragh2.textadventure.Direction;
import com.darragh2.textadventure.Player;
import com.darragh2.textadventure.ReadInput;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * The individual pieces of the maze
 */
public abstract class MapSite {

    private final HashMap<Direction, MapSite> adjacentMapSites = new HashMap<>(4);
    //Check if player is null to determine if player is in this MapSite
    private Player player;
    //These commands are always added to currentCommands on MapSite.enter(), never changed
    private ReadInput<MapSite> defaultCommands = new ReadInput<>();
    //CurrentCommands is the ReadInput<> that is run on MapSite.runCommands()
    private ReadInput<MapSite> currentCommands = new ReadInput<>();
    //Defines whether Player.defaultCommands are added to currentCommands when MapSite.enter() is called
    private boolean addCommandsOnEnter = true;

    public MapSite() {
        for (Direction direction : Direction.values()) {
            adjacentMapSites.put(direction, null);
        }
        defaultCommands.putCommand("help", s -> System.out.println("Commands: " + String.join(", ", s.getCurrentCommands().getCommands().keySet().stream().filter( key -> !key.equals("help") ).collect(Collectors.toSet()))));
    }

    public MapSite getAdjacentMapSite(Direction direction) {
        return adjacentMapSites.get(direction);
    }

    public void setAdjacentMapSite(Direction direction, MapSite mapSite) {
        if (adjacentMapSites.get(direction) != null) {
            return;
        }
        adjacentMapSites.put(direction, mapSite);
        if (mapSite.getAdjacentMapSite(Direction.oppositeDirection(direction)) == null) {
            mapSite.setAdjacentMapSite(Direction.oppositeDirection(direction), this);
        }
    }

    /**
     * Called when a Player enters the MapSite.
     * Must readInput.putCommand() in this method to add commands in this MapSite
     * @param player the Player instance that entered
     * @param playerCommands the list of commands that by default are added without overwriting any existing to MapSite.defaultCommands
     * @return true if the Player can enter the MapSite, false otherwise
     */
    public boolean enter(Player player, ReadInput<MapSite> playerCommands) {
        this.player = player;
        this.currentCommands = defaultCommands.clone();
        if (addCommandsOnEnter) {
            this.currentCommands.putIfAbsentAllCommands(playerCommands);
        }
        System.out.println(getDescription(player.getDirection()));
        return true;
    }

    /**
     * Called when a Player leaves the MapSite.
     * Sets this.Player == null and sets currentCommands to defaultCommands
     * @return true if player can leave, false otherwise
     */
    public boolean leave() {
        player = null;
        currentCommands.putAllCommands(defaultCommands);
        return true;
    }

    /**
     * Runs currentCommands on this instance
     */
    public void runCommands() {
        currentCommands.runCommands(this);
    }

    public HashMap<Direction, MapSite> getAdjacentMapSites() {
        return adjacentMapSites;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public ReadInput<MapSite> getDefaultCommands() {
        return defaultCommands;
    }

    /**
     * Sets the default commands that are always stored, whether a Player is in the MapSite
     * @param defaultCommands defaultCommands
     */
    public void setDefaultCommands(ReadInput<MapSite> defaultCommands) {
        this.defaultCommands = defaultCommands;
    }

    /**
     * Defines whether this MapSite will add the passed commands on MapSite.enter() to MapSite.currentCommands
     * @param addCommandsOnEnter boolean
     */
    public void setAddCommandsOnEnter(boolean addCommandsOnEnter) {
        this.addCommandsOnEnter = addCommandsOnEnter;
    }

    /**
     * @return the commands that are currently active
     */
    public ReadInput<MapSite> getCurrentCommands() {
        return currentCommands;
    }

    /**
     * Overrides the currentCommands changing what is called in MapSite.runCommands().
     * It is safe to down cast from MapSite in currentCommands, to be able to call methods specific to a subclass
     * @param currentCommands ReadInput<MapSite> that is set as reference
     */
    public void setCurrentCommands(ReadInput<MapSite> currentCommands) {
        this.currentCommands = currentCommands;
    }

    public abstract String getDescription(Direction direction);
}
