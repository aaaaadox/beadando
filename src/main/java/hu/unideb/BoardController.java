package hu.unideb;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import org.tinylog.Logger;
import util.javafx.ControllerHelper;
import util.javafx.Stopwatch;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class BoardController {

    @FXML
    private GridPane board;

    private BoardModel model;

    @FXML
    private Label stepsLabel;

    @FXML
    private Label stopwatchLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpFinishButton;

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    private Stopwatch stopwatch = new Stopwatch();

    private String playerName;

    private IntegerProperty steps = new SimpleIntegerProperty();

    private Instant startTime;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @FXML
    private void initialize() {
        stepsLabel.textProperty().bind(steps.asString());
        stopwatchLabel.textProperty().bind(stopwatch.hhmmssProperty());

        resetGame();
    }

    private void resetGame() {
        model = new BoardModel();
        bindGameStateToUI();
        steps.set(0);
        startTime = Instant.now();
        if (stopwatch.getStatus() == Animation.Status.PAUSED) {
            stopwatch.reset();
        }
        stopwatch.start();
    }

    private void bindGameStateToUI() {
        board.getChildren().remove(0,board.getChildren().size());

        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                board.add(createSquare(i, j), j, i);
            }
        }

        Platform.runLater(() -> ((StackPane) board.getChildren().get(6 * board.getColumnCount())).getChildren().add(createPiece()));

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
        steps.set(steps.get() + 1);

        Logger.debug("Change in X detected");
        Logger.debug("Old value: {}, new value: {}", oldValue.intValue(), newValue.intValue());

        // remove the piece from its old position
        ((StackPane) board.getChildren().get(oldValue.intValue() * board.getColumnCount() + model.getPosY())).getChildren().remove(0);

        // create a piece at its new position
        ((StackPane) board.getChildren().get(newValue.intValue() * board.getColumnCount() + model.getPosY())).getChildren().add(createPiece());
    }

    private void handlePieceRepaintY(ObservableValue observableValue, Number oldValue, Number newValue) {
        steps.set(steps.get() + 1);

        Logger.debug("Change in Y detected");
        Logger.debug("Old value: {}, new value: {}", oldValue.intValue(), newValue.intValue());

        // remove the piece from its old position
        ((StackPane) board.getChildren().get(model.getPosX() * board.getColumnCount() + oldValue.intValue())).getChildren().remove(0);

        // create a piece at its new position
        ((StackPane) board.getChildren().get(model.getPosX() * board.getColumnCount() + newValue.intValue())).getChildren().add(createPiece());
    }

    public void handleResetButton(ActionEvent actionEvent)  {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Logger.info("Resetting game");
        stopwatch.stop();
        resetGame();
    }

    public void handleGiveUpFinishButton(ActionEvent actionEvent) throws IOException {
        var buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            stopwatch.stop();
            Logger.info("The game has been given up");
        }
        Logger.debug("Saving result");
        gameResultDao.persist(createGameResult());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        ControllerHelper.loadAndShowFXML(fxmlLoader, "/highscores.fxml", stage);
    }

    private GameResult createGameResult() {
        return GameResult.builder()
                .player(playerName)
                .solved(model.getYouWon())
                .duration(Duration.between(startTime, Instant.now()))
                .steps(steps.get())
                .build();
    }

    private void handleYouLost(ObservableValue observableValue, boolean oldValue, boolean newValue) {
        if (newValue) {
            Logger.info("The user has lost.");
            Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
            gameOverAlert.setTitle("Game Over");
            gameOverAlert.setHeaderText(":(");
            gameOverAlert.setContentText("You lost!");
            gameOverAlert.showAndWait();
        }
    }

    private void handleYouWon(ObservableValue observableValue, boolean oldValue, boolean newValue) {
        if (newValue) {
            Logger.info("The user has won!");
            stopwatch.stop();
            Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
            gameOverAlert.setTitle("Game Over");
            gameOverAlert.setHeaderText("Congratulations!");
            gameOverAlert.setContentText("You won!");
            gameOverAlert.showAndWait();
            resetButton.setDisable(true);
            giveUpFinishButton.setText("Finish");
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
