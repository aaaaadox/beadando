package hu.unideb;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Direction forward(Direction prevDirection) {
        return switch (prevDirection) {
            case NORTH -> NORTH;
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
        };
    }

    public Direction right(Direction prevDirection) {
        return switch (prevDirection) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public Direction left(Direction prevDirection) {
        return switch (prevDirection) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }
}
