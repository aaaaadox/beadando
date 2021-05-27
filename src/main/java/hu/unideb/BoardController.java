package hu.unideb;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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

        model.youLostProperty().addListener(this::handleYouLost);
        model.youWonProperty().addListener(this::handleYouWon);
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
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Click on square (%d,%d)\n", row, col);
        model.move(row, col);
    }
}
