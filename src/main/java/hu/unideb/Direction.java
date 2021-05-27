package hu.unideb;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static Direction forwardOf(Direction prevDirection) {
        return switch (prevDirection) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
        };
    }

    public static Direction rightOf(Direction prevDirection) {
        return switch (prevDirection) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public static Direction leftOf(Direction prevDirection) {
        return switch (prevDirection) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }
}
