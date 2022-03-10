package com.darragh2.textadventure;

import com.darragh2.textadventure.maze.mapsite.MapSite;

/**
 * TODO: change implementation of Player so that it doesn't print anything
 * TODO: make Player class a true Singleton maybe
 */
public class Player
{
    private Direction direction = Direction.SOUTH;
    private MapSite currentMapSite;
    private boolean playing = true;
    private boolean alive = true;
    private boolean hasKey = false;
    private boolean won = false;
    private ReadInput<MapSite> defaultCommands;

    /**
     * When player is instantiated it calls getDescription on the passed mapSite, printing the result to the console and in turn starting the game.
     * @param mapSite the MapSite that acts as an entry point into the maze
     * @param defaultCommands the default commands, usually involve moving the player around
     */
    public Player (MapSite mapSite, ReadInput<MapSite> defaultCommands) {
        this.defaultCommands = defaultCommands;
        setCurrentMapSite(mapSite);
    }

    public void turnLeft() {
        direction = Direction.turnLeft(direction);
        System.out.println(currentMapSite.getDescription(direction));
    }

    public void turnRight() {
        direction = Direction.turnRight(direction);
        System.out.println(currentMapSite.getDescription(direction));
    }

    public void turnAround() {
        direction = Direction.oppositeDirection(direction);
        System.out.println(currentMapSite.getDescription(direction));
    }

    public void move() {
        //Returns null if there is no room to move into,
        //or the room that has been moved into
        if (currentMapSite.getAdjacentMapSite(direction) == null) {
            System.out.println("You cannot move there");
        }
        else {
            boolean hasLeftRoom = currentMapSite.leave();
            if (!hasLeftRoom) {
                return;
            }
            if (playing) {
                currentMapSite = currentMapSite.getAdjacentMapSite(direction);
                boolean hasEnteredRoom = currentMapSite.enter(this, defaultCommands);
            }
            else {
                endGame();
            }
        }

        /**

        //If player walks into room3 you die
        if (currentMapSite.numRoom == 3)
        {
            alive = false;
        }
        if (currentMapSite.numRoom == 2)
        {
            System.out.println("There is a cat that asks if you want to answer some trivia. ");
            TextAdventure.readPlayerInput.readTrivia();
        }
        if (currentMapSite.numRoom == 6)
        {
            won = true;
        }
         **/
    }

    /**
     * runs when Player.playing = false
     */
    private void endGame() {
        if (playing) {
            return;
        }
        if (!alive) {
            System.out.println("You have died!!");
        } else if (won) {
            System.out.println("You have won the game, congrats!!!");
        }
        //TODO: 3/7/2022 Should flesh this out later and add specific death messages
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {this.direction = direction;}

    public void setCurrentMapSite(MapSite mapSite) {
        currentMapSite = mapSite;
        currentMapSite.enter(this, defaultCommands);
    }

    public MapSite getCurrentMapSite() {
        return currentMapSite;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        if (!playing) {
            endGame();
        }
        this.playing = playing;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        if (!alive) {
            playing = false;
        }
        this.alive = alive;
    }

    public void setWon(boolean won) {
        if (won) {
            playing = false;
        }
        this.won = won;
    }
    public boolean hasWon() {
        return won;
    }

    public void runCommands() {
        currentMapSite.runCommands();
    }

    public boolean hasKey() {
        return hasKey;
    }

    public void setKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    public ReadInput<MapSite> getDefaultCommands() {
        return defaultCommands;
    }

    public void setDefaultCommands(ReadInput<MapSite> defaultCommands) {
        this.defaultCommands = defaultCommands;
    }
}