package hu.unideb;

import javafx.beans.property.*;
import org.tinylog.Logger;

import java.util.Arrays;

public class BoardModel {
    public static int BOARD_SIZE = 7;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];

    private ReadOnlyBooleanWrapper youLost = new ReadOnlyBooleanWrapper();

    private ReadOnlyBooleanWrapper youWon = new ReadOnlyBooleanWrapper();

    private ReadOnlyIntegerWrapper posX = new ReadOnlyIntegerWrapper();

    private ReadOnlyIntegerWrapper posY = new ReadOnlyIntegerWrapper();

    private Direction previousDirection = Direction.NORTH;

    public BoardModel() {
        // default (white)
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.WHITE);
            }
        }

        // blue
        board[0][0] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[1][0] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[1][2] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[1][5] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[2][2] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[3][0] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[3][1] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[3][2] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[3][4] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);
        board[5][2] = new ReadOnlyObjectWrapper<Square>(Square.BLUE);

        // red
        board[0][4] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[2][1] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[2][3] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[4][0] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[4][1] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[4][3] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[4][4] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[5][3] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[5][6] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[6][2] = new ReadOnlyObjectWrapper<Square>(Square.RED);
        board[6][3] = new ReadOnlyObjectWrapper<Square>(Square.RED);

        // put down our piece at the starting point
        posX.set(6);
        posY.set(0);
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    public Square getSquare(int i, int j) {
        return board[i][j].get();
    }

    public boolean getYouLost() {
        return youLost.get();
    }

    public ReadOnlyBooleanWrapper youLostProperty() {
        return youLost;
    }

    public boolean getYouWon() {
        return youWon.get();
    }

    public ReadOnlyBooleanWrapper youWonProperty() {
        return youWon;
    }

    public int getPosX() {
        return posX.get();
    }

    public ReadOnlyIntegerWrapper posXProperty() {
        return posX;
    }

    public int getPosY() {
        return posY.get();
    }

    public ReadOnlyIntegerWrapper posYProperty() {
        return posY;
    }

    /**
     * Verifies if from a given colored square, considering the passed previous
     * step direction, you can take the passed next step, or not.
     * @param sq a square you're standing on
     * @param prevDirection the direction you stepped onto it
     * @param nextDirection the direction you want to go
     * @return if you can move that way or not
     */
    private boolean isLegal(Square sq, Direction prevDirection, Direction nextDirection) {
        switch (sq) {
            case WHITE -> {
                if (Direction.forwardOf(prevDirection) != nextDirection) {
                    return false;
                }
            }
            case RED -> {
                if (Direction.forwardOf(prevDirection) != nextDirection &&
                        Direction.rightOf(prevDirection) != nextDirection) {
                    return false;
                }
            }
            case BLUE -> {
                if (Direction.forwardOf(prevDirection) != nextDirection &&
                        Direction.leftOf(prevDirection) != nextDirection) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Executes a step, in a given direction. Assumes step legality.
     * @param nextDirection the direction to move in
     */
    private void changePos(Direction nextDirection) {
        switch (nextDirection) {
            case NORTH -> posX.set(posX.get() - 1);
            case EAST -> posY.set(posY.get() + 1);
            case SOUTH -> posX.set(posX.get() + 1);
            case WEST -> posY.set(posY.get() - 1);
        }

        // this was a fun bug to track back
        previousDirection = nextDirection;
    }

    /**
     * Checks if the game is won yet.
     */
    private void checkWinState() {
        if (posX.get() == 0 && posY.get() == 6) youWon.set(true);
    }

    /**
     * Checks if the game is lost yet.
     */
    private void checkLoseState() {
        // stubbed
    }

    /**
     * This publicly exposed method calls the various verification methods
     * first, and then calls for the execution of a move, if found legal.
     *
     * Fails silently with logging, if the to-be-taken move was found illegal.
     * @param i target row
     * @param j target column
     */
    public void move(int i, int j) {
        // check if it's even near the current pos
        var nextDirection = determineDirection(i, j);
        if (nextDirection == null) {
            Logger.warn("Illegal move attempt detected. Target ({}, {}) not in range.", i, j);
        } else {
            // check if it's a legal step to make
            var currSquare = board[posX.get()][posY.get()].get();
            if (isLegal(currSquare, previousDirection, nextDirection)) {
                // at this point we can be sure the move can be taken
                changePos(nextDirection);

                // check if we've won yet (should've done this with binding but i'm tired)
                checkWinState();

                // check if we've lost yet (same here)
                checkLoseState();
            }
            else {
                Logger.error("Illegal move detected! Cannot move {} from a {} square, if the previous move was {}.",
                        nextDirection.name(), currSquare.name(), previousDirection.name());
            }
        }
    }

    /**
     * Converts a target coordinate to a target direction.
     *
     * In case the target coordinate cannot be mapped to a direction,
     * it returns null.
     * @param i target row
     * @param j target column
     * @return a {@link Direction} if such mapping is possible, null otherwise
     */
    private Direction determineDirection(int i, int j) {
        if (i == posX.get() + 1 && j == posY.get()) {
            return Direction.SOUTH;
        } else if (i == posX.get() - 1 && j == posY.get()) {
            return Direction.NORTH;
        } else if (i == posX.get() && j == posY.get() + 1) {
            return Direction.EAST;
        } else if (i == posX.get() && j == posY.get() - 1) {
            return Direction.WEST;
        } else {
            return null;
        }
    }
}
