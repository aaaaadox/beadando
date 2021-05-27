package hu.unideb;

import javafx.beans.property.*;
import org.tinylog.Logger;

public class BoardModel {
    public static int BOARD_SIZE = 7;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];

    private ReadOnlyBooleanWrapper youLost = new ReadOnlyBooleanWrapper();

    private ReadOnlyBooleanWrapper youWon = new ReadOnlyBooleanWrapper();

    private ReadOnlyIntegerWrapper posX = new ReadOnlyIntegerWrapper(6);

    private ReadOnlyIntegerWrapper posY = new ReadOnlyIntegerWrapper(0);

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

    public void move(int i, int j) {
        // check if it's even near the current pos
        var nextDirection = determineDirection(i, j);
        if (nextDirection != null) {
            // check if it's a legal step to make
            var currSquare = board[posX.get()][posY.get()].get();
            switch (currSquare) {
                case WHITE -> {
                    if (Direction.forwardOf(previousDirection) != nextDirection) {
                        Logger.warn("Illegal move attempt detected.");
                        return;
                    }
                }
                case RED -> {
                    if (Direction.forwardOf(previousDirection) != nextDirection ||
                        Direction.rightOf(previousDirection) != nextDirection) {
                        Logger.warn("Illegal move attempt detected.");
                        return;
                    }
                }
                case BLUE -> {
                    if (Direction.forwardOf(previousDirection) != nextDirection ||
                        Direction.leftOf(previousDirection) != nextDirection) {
                        Logger.warn("Illegal move attempt detected.");
                        return;
                    }
                }
            }

            // at this point we can be sure the move can be taken
            switch (nextDirection) {
                case NORTH -> posY.set(posY.get() - 1);
                case EAST -> posX.set(posX.get() + 1);
                case SOUTH -> posY.set(posY.get() + 1);
                case WEST -> posX.set(posX.get() - 1);
            }

            // check if we won yet (should've done this with binding but i'm tired)
            if (posX.get() == 0 && posY.get() == 6) {
                youWon.set(true);
            }
        }
    }

    private Direction determineDirection(int i, int j) {
        if (i > posX.get() && j == posY.get()) {
            return Direction.SOUTH;
        } else if (i < posX.get() && j == posY.get()) {
            return Direction.NORTH;
        } else if (i == posX.get() && j > posY.get()) {
            return Direction.EAST;
        } else if (i == posX.get() && j < posY.get()) {
            return Direction.WEST;
        } else {
            return null;
        }
    }
}
