package hu.unideb;

import java.util.List;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import util.guice.PersistenceModule;
import javax.inject.Inject;
import org.tinylog.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BeadandoApplication extends Application {

    private GuiceContext context = new GuiceContext(this, () -> List.of(
            new AbstractModule() {
                @Override
                protected void configure() {
                    install(new PersistenceModule("beadando"));
                    bind(GameResultDao.class);
                }
            }
    ));

    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("Starting application");
        context.init();
        fxmlLoader.setLocation(getClass().getResource("/opening.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("JavaFX Beadandó");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
