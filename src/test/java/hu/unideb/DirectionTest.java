package hu.unideb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void of() {
        assertSame(Direction.NORTH, Direction.of(1, 0));
        assertSame(Direction.EAST, Direction.of(0, 1));
        assertSame(Direction.SOUTH, Direction.of(-1, 0));
        assertSame(Direction.WEST, Direction.of(0, -1));
        assertThrows(IllegalArgumentException.class, () -> Direction.of(0, 0));
    }
}