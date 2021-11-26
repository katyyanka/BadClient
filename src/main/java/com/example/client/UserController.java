package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UserController {

    public Button exit;
    @FXML
    void initialize(){
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                exit.getScene().getWindow().hide();
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Шуточки-Маршруточки");
                stage.getIcons().add(new Image("/iconPNG.png"));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
