package com.example.client;

import User.Account;
import animations.Shake;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthController implements Encoding,    Serializable {

    String clientMessage="",serverMessage="";
    Socket socket;
    ObjectInputStream inOStream;
    ObjectOutputStream outOStream;
    //DataOutputStream outStream;
    BufferedReader br;

    @Override
    public byte[] encodingPassword(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        return digest.digest(passwordAuth.getText().getBytes(StandardCharsets.UTF_8));
    }

    @FXML
    public Button exit;
    @FXML
    public PasswordField passwordAuth;
    @FXML
    public TextField loginAuth;
    @FXML
    public Button enter;
    @FXML
    public Hyperlink registration;

    private void enter(){
        Account account = new Account();
        account.email = loginAuth.getText();
        account.password = bytesToHex(encodingPassword(passwordAuth.getText()));
        try {
            socket=new Socket("127.0.0.1",8888);
            outOStream=new ObjectOutputStream(socket.getOutputStream());
            inOStream=new ObjectInputStream(socket.getInputStream());
            br=new BufferedReader(new InputStreamReader(System.in));
            outOStream.writeUTF("authorization");
            outOStream.flush();
            outOStream.writeObject(account);
            outOStream.flush();
            serverMessage=inOStream.readUTF();
            showAlert(serverMessage);
            if (serverMessage.equals("Добро пожаловать, Администратор!")){
                openNewWindow("admin.fxml", loginAuth.getText());
            }
            else if (serverMessage.equals("Добро пожаловать, Пользователь!")){
                openNewWindow("user.fxml");
            } else {
                Shake userLoginAnim = new Shake(loginAuth);

                Shake userPasswordAnim = new Shake(passwordAuth);

                userPasswordAnim.playAnim();
                userLoginAnim.playAnim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize() {

        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Stage old = (Stage) exit.getScene().getWindow();
            old.close();
        });

        registration.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                openNewWindow("registration.fxml");
            } catch (IOException e) {
                System.err.println(e);
            }
            Stage old = (Stage) exit.getScene().getWindow();
            old.close();
        });

        enter.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {          enter();        });

        enter.setOnAction(event -> {         enter();                }
        );
        enter.setDefaultButton(true);
        enter.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ENTER)||event.isControlDown()) {
                        enter.fire();
                    }
                }
        );
    }
        private void openNewWindow(String window) throws IOException {
            enter.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Шуточки-Маршруточки");
            stage.getIcons().add(new Image("/iconPNG.png"));
            stage.show();
        }

    private void openNewWindow(String window, String information) throws IOException {
        enter.getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
        System.out.println(information);
        AdminController adminController =
                new AdminController(information);
        loader.setController(adminController);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Шуточки-Маршруточки");
        stage.getIcons().add(new Image("/iconPNG.png"));
        stage.show();
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Результат");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

}