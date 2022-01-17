package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

 interface helperController {
    void onLoad();
}

public class ScreenController {
    private HashMap<String, Pane> screenMap;
    private Scene main;
    private static ScreenController instance;
    private ScreenController(Scene main) {
        screenMap= new HashMap<>();
        this.main = main;
    }
    public static ScreenController getInstance(Scene main)
    {
        if (instance == null)
            instance = new ScreenController(main);
        return instance;
    }
    public static ScreenController getInstance() throws Exception {
        if (instance == null)
            throw new Exception("ScreenController не может быть использован не имея сцены");
        return instance;
    }

    protected void addScreen(String name, Pane pane){
        instance.screenMap.put(name, pane);
    }

    protected void removeScreen(String name){
        instance.screenMap.remove(name);
    }

    protected <T extends helperController> void activate(String name){
        main.setRoot( instance.screenMap.get(name) );
    }
}