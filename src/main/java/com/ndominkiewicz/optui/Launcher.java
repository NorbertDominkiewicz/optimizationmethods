package com.ndominkiewicz.optui;

import com.ndominkiewicz.optui.utils.Cords;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class Launcher extends Application {
    static Launcher instance;
    static Stage stage;
    static Scene scene;
    Cords position;

    @Override
    public void start(Stage mainStage) throws IOException {
        instance = this;
        position = new Cords();
        stage = mainStage;
        scene = new Scene(loadFXML());
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("SheetLab");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        loadAppMovement();
    }

    private Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/views/main.fxml"));
        return fxmlLoader.load();
    }

    private void loadAppMovement() {
        scene.setOnMousePressed((MouseEvent event) -> {
            position.setX((int) (event.getScreenX() - stage.getX()));
            position.setY((int) (event.getScreenY() - stage.getY()));
        });

        scene.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - position.getX());
            stage.setY(event.getScreenY() - position.getY());
        });
    }

    public static void close() {
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(200), new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished((event) -> System.exit(0));
        timeline.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
