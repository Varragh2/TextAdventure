package com.darragh2.textadventure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A datatype that contains a graph of Nodes with methods for adding, setting, and removing Nodes
 */
public class NodeGraph2D <T> {
    private Node startingNode;
    private int size;

    public NodeGraph2D(T T) {
        this(T, new int[] {0, 0});
    }

    public NodeGraph2D (T object, int[] startingPosition) {
        this.startingNode = new Node(object, startingPosition);
    }

    public void addNode(int[]xy, Direction direction, T T) {
        this.addNode(getNode(xy), direction, new Node(T, Direction.add(xy, direction)));
    }

    public void addNode(Node prevNode, Direction direction, Node newNode) {
        size++;
        prevNode.setAdjacentNode(direction, newNode);
        newNode.setAdjacentNode(Direction.oppositeDirection(direction), prevNode);
        try {
            //This won't always work
            prevNode.getAdjacentNode(Direction.turnRight(direction)).getAdjacentNode(direction).setAdjacentNode(Direction.turnLeft(direction), newNode);
            prevNode.getAdjacentNode(Direction.turnLeft(direction)).getAdjacentNode(direction).setAdjacentNode(Direction.turnLeft(direction), newNode);

            prevNode.getAdjacentNode(Direction.turnRight(direction)).getAdjacentNode(direction).getAdjacentNode(direction).getAdjacentNode(Direction.turnLeft(direction)).setAdjacentNode(Direction.oppositeDirection(direction), newNode);
            prevNode.getAdjacentNode(Direction.turnLeft(direction)).getAdjacentNode(direction).getAdjacentNode(direction).getAdjacentNode(Direction.turnRight(direction)).setAdjacentNode(Direction.oppositeDirection(direction), newNode);
        }
        catch (NullPointerException ignore) {
        }

    }

    public Node getNode(int[]xy) {
        Node node = null;
        Node prevNode = startingNode;
        for (int i = 0; i < xy[0]; i++) {
            node = prevNode.getAdjacentNode(new int[] {0, 1});
        }
        for (int i = 0; i < xy[1]; i++) {
            node = prevNode.getAdjacentNode(new int[] {-1, 0});
        }
        return node;
    }

    class Node {
        private final T T; //The object that this Node wraps

        Map<Direction, Node> adjacentNodes;
        private final int[] xy;
        private Node up;
        private Node down;
        private Node left;
        private Node right;

        Node (T T, int[] xy) {
            this.T = T;
            this.xy = xy;
            adjacentNodes = new HashMap<>();
            Arrays.stream(Direction.values()).forEach( direction -> adjacentNodes.put(direction, null));
        }

        public T get() {
            return this.T;
        }

        public Node getAdjacentNode(int[] direction) throws IllegalArgumentException {
            String list = "[" + direction[0] + "," + direction[1] + "]";
            return switch (list) {
                case "[1,0]" -> up;
                case "[-1,0]" -> down;
                case "[0,1]" -> right;
                case "[0,-1]" -> left;
                default -> throw new IllegalArgumentException(list + " not an acceptable statement");
            };
        }

        public Node getAdjacentNode(Direction direction) {
            return adjacentNodes.get(direction);
        }

        public void setAdjacentNode(Direction direction, Node newNode) {
            adjacentNodes.put(direction, newNode);
        }

        public void setAdjacentNode(int[] direction, Node newNode) {
            String list = "[" + direction[0] + "," + direction[1] + "]";
            switch (list) {
                case "[1,0]" -> up = newNode;
                case "[-1,0]" -> down = newNode;
                case "[0,1]" -> right = newNode;
                case "[0,-1]" -> left = newNode;
                default -> throw new IllegalArgumentException(list + " not an acceptable statement");
            }

            if (newNode.getAdjacentNode(oppositeDirection(direction)) != null) {
                newNode.setAdjacentNode(oppositeDirection(direction), this);
            }
        }

        public int[] oppositeDirection(int[] direction) {
            String list = "[" + direction[0] + "," + direction[1] + "]";
            return switch (list) {
                case "[1,0]" -> new int[] {-1, 0};
                case "[-1,0]" -> new int[] {1, 0};
                case "[0,1]" -> new int[] {0, -1};
                case "[0,-1]" -> new int[] {0, 1};
                default -> throw new IllegalArgumentException(list + " not an acceptable statement");
            };
        }

    }
}

