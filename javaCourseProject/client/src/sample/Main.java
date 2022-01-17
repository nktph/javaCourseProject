package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene mainScene = new Scene(root, 960, 540);
        ScreenController sc = ScreenController.getInstance(mainScene);
        sc.addScreen("loginView", (Pane) root);
        sc.addScreen("StudentView",FXMLLoader.load(getClass().getResource("StudentView.fxml")));
        sc.addScreen("AccountantView",FXMLLoader.load(getClass().getResource("accountantView.fxml")));
        sc.addScreen("HRView",FXMLLoader.load(getClass().getResource("HRView.fxml")));
        sc.addScreen("AdminView",FXMLLoader.load(getClass().getResource("adminView.fxml")));
        primaryStage.setTitle("Scholarship|Login");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
