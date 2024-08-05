import com.uns.controller.ControllerViewMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/com/uns/view/view_main.fxml"));
        Parent root = loaderMain.load();
        ControllerViewMain controllerViewMain = loaderMain.getController();
        Scene sceneMain = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/uns/res/css/main.css").toExternalForm());
        primaryStage.setScene(sceneMain);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Tareas");
        primaryStage.show();


    }
}