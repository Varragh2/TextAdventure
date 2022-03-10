package com.darragh2.textadventure.maze.mapsite;

import com.darragh2.textadventure.Direction;
import com.darragh2.textadventure.Player;
import com.darragh2.textadventure.ReadInput;

public class StoppedRoom extends MapSite {

    private int numTrys = 3;
    private ReadInput<MapSite> playerCommands = new ReadInput<>();

    public StoppedRoom() {
        setAddCommandsOnEnter(false);
        ReadInput<MapSite> readInput = new ReadInput<>();
        readInput.setDefaultCommand( mapSite -> {
            StoppedRoom stoppedRoom = (StoppedRoom) mapSite;
            stoppedRoom.numTrys -= 1;
            if (numTrys <= 0) {
                stoppedRoom.setCurrentCommands(playerCommands);
            }
            else {
                System.out.println("You still can't move for " + numTrys + " times.");
            }
        });
        setDefaultCommands(readInput);
    }

    /**
     * Called when a Player enters the MapSite.
     * Must readInput.putCommand() in this method to add commands in this MapSite
     * @param player the Player instance that entered
     * @param playerCommands the list of commands that by default are added without overwriting any existing to MapSite.defaultCommands
     * @return true if the Player can enter the MapSite, false otherwise
     */
    @Override
    public boolean enter(Player player, ReadInput<MapSite> playerCommands) {
        super.enter(player, playerCommands);
        this.playerCommands = playerCommands;
        return true;
    }

    @Override
    public String getDescription(Direction direction) {
        return "This is a stopped room";
    }
}
