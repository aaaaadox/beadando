package hu.unideb;

import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;

public class BoardController {

    @FXML
    private GridPane board;

    private BoardModel model = new BoardModel();

    @FXML
    private void initialize() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                board.add(createSquare(i, j), j, i);
            }
        }

        ((StackPane) board.getChildren().get(6 * board.getColumnCount())).getChildren().add(createPiece());

        model.youLostProperty().addListener(this::handleYouLost);
        model.youWonProperty().addListener(this::handleYouWon);
        model.posXProperty().addListener(this::handlePieceRepaintX);
        model.posYProperty().addListener(this::handlePieceRepaintY);
    }

    private Circle createPiece() {
        var piece = new Circle(25);
        piece.setFill(Color.LIGHTGREEN);
        return piece;
    }

    private void handlePieceRepaintX(ObservableValue observableValue, Number oldValue, Number newValue) {
        Logger.debug("Change in X detected");
        Logger.debug("Old value: {}, new value: {}", oldValue.intValue(), newValue.intValue());

        // remove the piece from its old position
        ((StackPane) board.getChildren().get(oldValue.intValue() * board.getColumnCount() + model.getPosY())).getChildren().remove(0);

        // create a piece at its new position
        ((StackPane) board.getChildren().get(newValue.intValue() * board.getColumnCount() + model.getPosY())).getChildren().add(createPiece());
    }

    private void handlePieceRepaintY(ObservableValue observableValue, Number oldValue, Number newValue) {
        Logger.debug("Change in Y detected");
        Logger.debug("Old value: {}, new value: {}", oldValue.intValue(), newValue.intValue());

        // remove the piece from its old position
        ((StackPane) board.getChildren().get(model.getPosX() * board.getColumnCount() + oldValue.intValue())).getChildren().remove(0);

        // create a piece at its new position
        ((StackPane) board.getChildren().get(model.getPosX() * board.getColumnCount() + newValue.intValue())).getChildren().add(createPiece());
    }

    private void handleYouLost(ObservableValue observableValue, boolean oldValue, boolean newValue) {
        if (newValue) {
            Logger.info("The user has lost.");
            Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
            gameOverAlert.setTitle("Game Over");
            gameOverAlert.setHeaderText(":(");
            gameOverAlert.setContentText("You lost!");
            gameOverAlert.showAndWait();
            Platform.exit();
        }
    }

    private void handleYouWon(ObservableValue observableValue, boolean oldValue, boolean newValue) {
        if (newValue) {
            Logger.info("The user has won!");
            Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
            gameOverAlert.setTitle("Game Over");
            gameOverAlert.setHeaderText("Congratulations!");
            gameOverAlert.setContentText("You won!");
            gameOverAlert.showAndWait();
            Platform.exit();
        }
    }

    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add(
                switch(model.getSquare(i, j)) {
                    case WHITE -> {
                        Logger.debug("created white square");
                        yield "whitecell";
                    }
                    case RED -> {
                        Logger.debug("created red square");
                        yield "redcell";
                    }
                    case BLUE -> {
                        Logger.debug("created blue square");
                        yield "bluecell";
                    }
                }
        );
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        // detect the click
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Click on square ({}, {})", row, col);

        // pass it on
        model.move(row, col);
    }
}
