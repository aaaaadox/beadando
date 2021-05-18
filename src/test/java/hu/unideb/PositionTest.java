package hu.unideb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position position;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col())
        );
    }

    @BeforeEach
    void init() {
        position = new Position(0,0);
    }

    @Test
    void getTarget() {
        assertPosition(1, 0, position.getTarget(Direction.NORTH));
        assertPosition(0, 1, position.getTarget(Direction.EAST));
        assertPosition(-1, 0, position.getTarget(Direction.SOUTH));
        assertPosition(0, -1, position.getTarget(Direction.WEST));
    }

    @Test
    void getNorth() {
        assertPosition(1, 0, position.getNorth());
    }

    @Test
    void getEast() {
        assertPosition(0, 1, position.getEast());
    }

    @Test
    void getSouth() {
        assertPosition(-1, 0, position.getSouth());
    }

    @Test
    void getWest() {
        assertPosition(0, -1, position.getWest());
    }

    @Test
    void setTarget_north() {
        position.setTarget(Direction.NORTH);
        assertPosition(1, 0, position);
    }

    @Test
    void setTarget_east() {
        position.setTarget(Direction.EAST);
        assertPosition(0, 1, position);
    }

    @Test
    void setTarget_south() {
        position.setTarget(Direction.SOUTH);
        assertPosition(-1, 0, position);
    }

    @Test
    void setTarget_west() {
        position.setTarget(Direction.WEST);
        assertPosition(0, -1, position);
    }

    @Test
    void setNorth() {
        position.setNorth();
        assertPosition(1, 0, position);
    }

    @Test
    void setEast() {
        position.setEast();
        assertPosition(0, 1, position);
    }

    @Test
    void setSouth() {
        position.setSouth();
        assertPosition(-1, 0, position);
    }

    @Test
    void setWest() {
        position.setWest();
        assertPosition(0, -1, position);
    }

    @Test
    void testEquals() {
        assertEquals(position, position);
        assertEquals(position, position.clone());
        assertNotEquals(position, new Position(position.row(), Integer.MAX_VALUE));
        assertNotEquals(position, new Position(Integer.MIN_VALUE, position.col()));
        assertNotEquals(position, new Position(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertNotEquals(null, position);
        assertNotEquals("test", position);
    }

    @Test
    void testHashCode() {
        assertEquals(position.hashCode(), position.hashCode());
        assertEquals(position.hashCode(), position.clone().hashCode());
    }

    @Test
    void testClone() {
        var copy = position.clone();
        assertEquals(position, copy);
        assertNotSame(position, copy);
    }

    @Test
    void testToString() {
        assertEquals("(0,0)", position.toString());
    }
}