package hu.unideb;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class BoardModel {
    public static int BOARD_SIZE = 7;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];

    private ReadOnlyBooleanWrapper youLost = new ReadOnlyBooleanWrapper();

    private ReadOnlyBooleanWrapper youWon = new ReadOnlyBooleanWrapper();

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

    public void move(int i, int j) {
        // stubbed
    }
}
