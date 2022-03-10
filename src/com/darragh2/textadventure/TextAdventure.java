package com.darragh2.textadventure;

import com.darragh2.textadventure.maze.mapsite.MapSite;
import com.darragh2.textadventure.maze.mapsite.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class TextAdventure {

    static Player player; //Global singular instantiation of Player
    static ReadInput<Player> readPlayerInput;
    static ArrayList<ArrayList<MapSite>> board;

    public static void main(String[] args) {
        //The Main method of the program
        String[] room0desc = {"This is the way you have came from, a line of chalk snakes backward deeper into the temple.", "Cobblestones lead onto a platform illuminated by, is that sunlight?!!", "A stone floor extends beneath you, going under a stone arch", "A stone wall fifty feet tall with vines growing on it stands before you."};
        String[] room1desc = { "In this direction you see a stone arch with a small white marking of chalk.", "You see a dimly lit cavern in front of you.", "A glowing light beckons you down the tunnel.", "An ivy covered wall stands before you."};
        String[] room2desc = { "More cobblestones lead off into the distance.", "Stairs lead down into complete darkness.", "Two behemoth statues block the passage.", "There is still an ivy covered wall standing before you." };
        String[] room3desc = {"The floor you stand on starts to shake. Then it falls beneath you into a deep pit.", "The floor you stand on starts to shake. Then it falls beneath you into a deep pit.", "", ""};
        String[] room4desc = {"Cobblestones lead onto a platform illuminated by, is that sunlight?!!", "You see the edge of the room.", "Stairs lead down into complete darkness.", "More cobblestones lead off into the distance."};
        String[] room5desc = {"As you fumble around in the dark, your knee bumps what must be a stair leading up.", "In the darkness with your hands out in front of you, your fingers grasp what must be a keyhole!", " You stumble into a wall at the end of the cave.", "As you fumble around in the dark, your knee bumps what must be a stair leading up."};

        ArrayList<String[]> roomDescriptions = new ArrayList<>();
        roomDescriptions.add(room0desc);
        roomDescriptions.add(room1desc);
        roomDescriptions.add(room2desc);
        roomDescriptions.add(room3desc);
        roomDescriptions.add(room4desc);
        roomDescriptions.add(room5desc);

        //Use object.getClass().getName() or object.getClass().getSimpleName() to get the name of the type of the object

        HashMap<String, Consumer<Player>> roomCommands = new HashMap<>();
        roomCommands.put("move", Player::move);
        roomCommands.put("turn left", Player::turnLeft);
        roomCommands.put("turn right", Player::turnRight);
        roomCommands.put("direction", player -> System.out.println("You are facing " + player.getDirection()));


        String input = "yes";
        while (true)
        {
            //Creates a room layout
            board = createBoard(2, 3, roomDescriptions);
            player = new Player(board.get(0).get(0), new ReadInput<>());
            readPlayerInput = new ReadInput<>(roomCommands);
            System.out.println(board.get(0).get(0).getDescription(player.getDirection()));

            while (player.isAlive() && !player.hasWon())
            {
                System.out.println("Enter command: ");
                readPlayerInput.runCommands(player);
            }
            //input = readPlayerInput.endGame();
        }

    }

        public static ArrayList<ArrayList<MapSite>> createBoard(int width, int height, ArrayList<String[]> descriptions) {
            ArrayList<ArrayList<MapSite>> board = new ArrayList<>();
            Iterator<String[]> it = descriptions.iterator();
            int numRooms = 0;
            for (int x = 0; x < width; x++) {
                board.add(new ArrayList<>());
                ArrayList<MapSite> row = board.get(x);
                for (int y = 0; y < height; y++) {
                    Room room = new Room(it.next());
                    numRooms++;
                    putNearbyRooms(room, board, row, x, y);
                    row.add(room);
                }
            }
            return board;
        }

        private static void putNearbyRooms(Room room, ArrayList<ArrayList<MapSite>> board, ArrayList<MapSite> row, int x, int y) {
            if (y > 0) {
                //Only if y - 1 >= 0
                //So get() doesn't throw an IndexOutOfBoundsException
                room.setAdjacentMapSite(Direction.NORTH, row.get(y - 1));
            }

            if (x == 0) {
                //If there is no previous row, return
                return;
            }
            //get previous row
            ArrayList<MapSite> prevRow = board.get(x - 1);
            //System.out.println("prevRow: " + prevRow.size());

            if (y > -1) {
                //y has to be 0 or greater
                room.setAdjacentMapSite(Direction.EAST, prevRow.get(y));
            }
        }
}
