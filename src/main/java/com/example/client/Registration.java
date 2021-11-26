package com.example.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registration implements Encoding {

    @FXML
    public Button addUser;
    @FXML
    public TextField lastname;
    @FXML
    public TextField name;
    @FXML
    public TextField phone;
    @FXML
    public TextField email;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;
    @FXML
    private RadioButton noInformation;
    @FXML
    public PasswordField password;
    @FXML
    public PasswordField passwordRepeat;
    String clientMessage="",serverMessage="";
    Socket socket;
    ObjectInputStream inOStream;
    ObjectOutputStream outOStream;
    BufferedReader br;

    @FXML
    private Label phoneEx;
    @FXML
    private Label nameEx;
    @FXML
    private Label lastnameEx;
    @FXML
    private Label emailEx;
    @FXML
    private Label passEx1;
    @FXML
    private Label passEx2;
    @FXML
    public Button back;

    void checking(String condition, String newValue, Label mistake){
        if(!newValue.matches(condition)) {
            mistake.setText("Некорректный формат");
        } else {
            mistake.setText("");
        }
    }

    void checking(String condition, String newValue, Label mistake,String ex){
        if(!newValue.matches(condition)) {
            mistake.setText(ex);
        } else {
            mistake.setText("");
        }
    }

    @FXML
    public void initialize() {

        phone.textProperty().addListener((observable, oldValue, newValue) -> checking("[+][0-9]{12,13}", newValue, phoneEx));

        name.textProperty().addListener((observable, oldValue, newValue) -> checking("[a-zA-Zа-яА-ЯёЁ -]+",newValue, nameEx));

        lastname.textProperty().addListener((observable, oldValue, newValue) -> checking("[a-zA-Zа-яА-ЯёЁ -]+",newValue, lastnameEx));

        email.textProperty().addListener((observable, oldValue, newValue) -> checking("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",newValue, emailEx));

       /* password.textProperty().addListener((observable, oldValue, newValue) -> checking(".{6,20}",newValue, passEx1, "Длина пароля должна быть больше 5 и меньше 21"));

        passwordRepeat.textProperty().addListener((observable, oldValue, newValue) -> checking(".{6,20}",newValue, passEx2, "Длина пароля должна быть больше 5 и меньше 21"));
*/

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                openNewWindow("hello-view.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addUser.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try{
                socket=new Socket("127.0.0.1",8888);
                inOStream=new ObjectInputStream(socket.getInputStream());
                outOStream=new ObjectOutputStream(socket.getOutputStream());
                outOStream.flush();
                br=new BufferedReader(new InputStreamReader(System.in));
            }catch(Exception e){
                System.out.println(e);
            }
            try{
                clientMessage = "";
                clientMessage=lastname.getText();
                clientMessage+="|";
                clientMessage+=name.getText();
                clientMessage+="|";
                clientMessage+=phone.getText();
                clientMessage+="|";
                if (male.isSelected()) clientMessage+="Мужской";
                else if (female.isSelected()) clientMessage+="Женский";
                else clientMessage+="Не определён";
                clientMessage+="|";
                clientMessage+=email.getText();
                clientMessage+="|";
                clientMessage+=bytesToHex(encodingPassword(password.getText()));
                clientMessage+="|";
                if (!password.getText().equals(passwordRepeat.getText())) {
                    showAlertWithHeaderText();
                    return;
                }
                outOStream.writeUTF("registration");
                outOStream.flush();
                outOStream.writeUTF(clientMessage);
                outOStream.flush();
                serverMessage=inOStream.readUTF();
                showAlert(serverMessage);
            outOStream.close();
            outOStream.close();
            socket.close();
            } catch(Exception e){
                System.out.println(e);
            }
        });
    }

    private void openNewWindow(String window) throws IOException {
        back.getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Шуточки-Маршруточки");
        stage.getIcons().add(new Image("/iconPNG.png"));
        stage.show();
    }

    private void showAlertWithHeaderText() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setContentText("Пароли не совпадают!");
        alert.showAndWait();
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Результат");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    @Override
    public byte[] encodingPassword(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedhash = digest.digest( password.getText().getBytes(StandardCharsets.UTF_8));
        return encodedhash;
    }
}
