package hu.unideb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardModelTest {

    private BoardModel model;

    @BeforeEach
    private void setUp() {
        model = new BoardModel();
    }

    @Test
    void testCannotMoveFarAway() {
        // try moving to the finish
        model.move(0,6);

        // verify that im still at the start
        assertEquals(6, model.getPosX());
        assertEquals(0, model.getPosY());
    }

    @Test
    void testCannotMoveIllegally() {
        // try moving EAST, despite only being able to move NORTH initially
        model.move(6,1);

        // verify that im still at the start
        assertEquals(6, model.getPosX());
        assertEquals(0, model.getPosY());
    }

    @Test
    void testCanMoveLegally() {
        // try moving NORTH
        model.move(5,0);

        // verify that i moved NORTH
        assertEquals(5, model.getPosX());
        assertEquals(0, model.getPosY());
    }
}
