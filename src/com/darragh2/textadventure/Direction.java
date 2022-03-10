package com.darragh2.textadventure;

/**
 * An enum to encapsulate all Direction related code
 */
public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    /**
     * Turns left
     * @param direction The starting direction
     * @return The direction rotated counter-clockwise
     */
    public static Direction turnLeft(Direction direction)
    {
        return switch (direction) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    /**
     * Turns right
     * @param direction The starting direction
     * @return The direction rotated clockwise
     */
    public static Direction turnRight(Direction direction)
    {
        return switch (direction) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    /**
     * Returns the opposite direction
     * @param direction The starting direction
     * @return The direction rotated 180 degrees
     */
    public static Direction oppositeDirection(Direction direction)
    {
        return switch (direction) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    public static int[] add(int[] num, Direction direction) {
        return switch (direction) {
            case NORTH -> new int[] {num[0] + 1, num[1]};
            case SOUTH -> new int[] {num[0] - 1, num[1]};
            case WEST -> new int[] {num[0], num[1] + 1};
            case EAST -> new int[] {num[0], num[1] - 1};
        };
    }

    public static int[] toIntArray(Direction direction) {
        return switch (direction) {
            case NORTH -> new int[] {1, 0};
            case SOUTH -> new int[] {-1, 0};
            case WEST -> new int[] {0, 1};
            case EAST -> new int[] {0, -1};
        };
    }
}