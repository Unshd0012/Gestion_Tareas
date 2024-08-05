import com.uns.controller.ControllerViewMain;
import com.uns.model.Conexion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/com/uns/view/view_main.fxml"));
            Parent root = loaderMain.load();
            ControllerViewMain controllerViewMain = loaderMain.getController();
            Scene sceneMain = new Scene(root);
            sceneMain.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/uns/res/css/main.css")).toExternalForm());
            primaryStage.setScene(sceneMain);
            primaryStage.getIcons().add(new Image("/com/uns/res/img/to-do-list.png"));
            primaryStage.setResizable(false);
            primaryStage.setTitle("Gestion de Tareas");
            primaryStage.show();
            controllerViewMain.setStage(primaryStage);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }

    }
}