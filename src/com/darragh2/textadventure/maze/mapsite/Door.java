package com.darragh2.textadventure.maze.mapsite;

import com.darragh2.textadventure.Direction;
import com.darragh2.textadventure.Player;
import com.darragh2.textadventure.ReadInput;

public class Door extends MapSite {


    boolean open;

    public Door() {
        super();
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


    @Override
    public String getDescription(Direction direction) {
        return "No description given";
    }

    @Override
    public boolean enter(Player player, ReadInput<MapSite> playerCommands) {
        super.enter(player, playerCommands);
        getDefaultCommands().putCommand("open", mapSite -> {
            Door door = (Door) mapSite;
            door.open = true;
        });
        getDefaultCommands().putCommand("close", mapSite -> {
            Door door = (Door) mapSite;
            door.open = false;
        });
        return true;
    }
}