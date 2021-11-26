package com.example.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class AdminController implements Encoding {

    public Label email;

    @FXML
    private Button newAdminFromClient;
    @FXML
    private Label name;
    @FXML
    private Button exit;
    @FXML
    private TableView adminTable;
    @FXML
    private TextField newAdminEmail;
    @FXML
    private TextField newAdminLastname;
    @FXML
    private TextField newAdminName;
    @FXML
    private TextField newAdminPhone;
    @FXML
    private TextField newAdminPassword;
    @FXML
    private ComboBox<String> adminGender;
    @FXML
    private ChoiceBox<String> genderFiltr;
    @FXML
    private ChoiceBox<String> fieldForEditAdmin;
    @FXML
    private ChoiceBox<String> editAdminEmail;
    @FXML
    private ChoiceBox<String> ClientsAdminEmails;
    @FXML
    private ChoiceBox<String> deleteAdminEmail;
    @FXML
    Label emailAdminError;
    @FXML
    Label nameAdminError;
    @FXML
    Label lastnameAdminError;
    @FXML
    Label phoneAdminError;
    @FXML
    Label passwordAdminError;
    @FXML
    Button newAdmin;
    @FXML
    Button deleteAdmin;
    @FXML
    Button AdminFiltr;
//

    public String userEmail;
    // String clientMessage = "", serverMessage = "";
    Socket socket;
    ObjectInputStream inOStream;
    ObjectOutputStream outOStream;
    BufferedReader br;

    @Override
    public byte[] encodingPassword(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedhash = digest.digest(newAdminPassword.getText().getBytes(StandardCharsets.UTF_8));
        return encodedhash;
    }

    static class User {
        StringProperty lastname;
        StringProperty name;
        StringProperty email;
        StringProperty gender;
        StringProperty phone;

        public User() {
        }

        public String getGender() {
            return gender.get();
        }

        public StringProperty genderProperty() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender.set(gender);
        }

        public String getPhone() {
            return phone.get();
        }

        public StringProperty phoneProperty() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone.set(phone);
        }

        public User(StringProperty lastname, StringProperty name, StringProperty email, StringProperty gender, StringProperty phone) {
            this.lastname = lastname;
            this.name = name;
            this.email = email;
            this.gender = gender;
            this.phone = phone;
        }

        public String getLastname() {
            return lastname.get();
        }

        public StringProperty lastnameProperty() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname.set(lastname);
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getEmail() {
            return email.get();
        }

        public StringProperty emailProperty() {
            return email;
        }

        public void setEmail(String email) {
            this.email.set(email);
        }
    }

    public AdminController(String info) {
        userEmail = info;
    }

    private void openNewWindow(String window) throws IOException {
        exit.getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Шуточки-Маршруточки");
        stage.getIcons().add(new Image("/iconPNG.png"));
        stage.show();
    }

    void checking(String condition, String newValue, Label mistake) {
        if (!newValue.matches(condition)) {
            mistake.setText("Некорректный формат");
        } else {
            mistake.setText("");
        }
    }

    void checking(String condition, String newValue, Label mistake, String ex) {
        if (!newValue.matches(condition)) {
            mistake.setText(ex);
        } else {
            mistake.setText("");
        }
    }


    @FXML
    public void initialize() {
        newAdminPhone.textProperty().addListener((observable, oldValue, newValue) -> checking("[+][0-9]{12,13}", newValue, phoneAdminError));
        newAdminLastname.textProperty().addListener((observable, oldValue, newValue) -> checking("[a-zA-Zа-яА-ЯёЁ -]+", newValue, lastnameAdminError));
        newAdminName.textProperty().addListener((observable, oldValue, newValue) -> checking("[a-zA-Zа-яА-ЯёЁ -]+", newValue, nameAdminError));
        newAdminEmail.textProperty().addListener((observable, oldValue, newValue) -> checking("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", newValue, emailAdminError));
        newAdminPassword.textProperty().addListener((observable, oldValue, newValue) -> checking(".{6,20}", newValue, passwordAdminError));
        try {
            socket = new Socket("127.0.0.1", 8888);
            inOStream = new ObjectInputStream(socket.getInputStream());
            outOStream = new ObjectOutputStream(socket.getOutputStream());
            outOStream.flush();
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outOStream.writeUTF("Admin First Window");
            outOStream.flush();
            outOStream.writeUTF(userEmail);
            outOStream.flush();
            name.setText(inOStream.readUTF());

            creatingAdminTable(Integer.parseInt(inOStream.readUTF()), inOStream.readUTF());
            initializeStaticFields();
            initializeDynamicFields(inOStream.readUTF(), inOStream.readUTF());

            outOStream.close();
            outOStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                openNewWindow("hello-view.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        newAdmin.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            addAdmin();
        });
        deleteAdmin.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            deleteAdmin();
        });
        AdminFiltr.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            filtrAdmin();
        });
        newAdminFromClient.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            addAdminFromClient();
        });
        email.setText(userEmail);
    }


    private void deleteAdmin(){
            try {
                socket = new Socket("127.0.0.1", 8888);
                inOStream = new ObjectInputStream(socket.getInputStream());
                outOStream = new ObjectOutputStream(socket.getOutputStream());
                outOStream.flush();
                br = new BufferedReader(new InputStreamReader(System.in));
                outOStream.writeUTF("Delete Admin");
                outOStream.flush();
                outOStream.writeUTF(deleteAdminEmail.getValue());
                outOStream.flush();
                showAlert(inOStream.readUTF());
                if (deleteAdminEmail.getValue().equals(email.getText())){
                    inOStream.readUTF();inOStream.readUTF();inOStream.readUTF();
                    openNewWindow("hello-view.fxml");
                }else {
                    creatingAdminTable(Integer.parseInt(inOStream.readUTF()), inOStream.readUTF());
                    initAdminsFields(inOStream.readUTF());
                }
                outOStream.close();
                outOStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void filtrAdmin(){
        try {
            socket = new Socket("127.0.0.1", 8888);
            inOStream = new ObjectInputStream(socket.getInputStream());
            outOStream = new ObjectOutputStream(socket.getOutputStream());
            outOStream.flush();
            br = new BufferedReader(new InputStreamReader(System.in));
            outOStream.writeUTF("Filtr Admin");
            outOStream.flush();
            outOStream.writeUTF(genderFiltr.getValue());
            outOStream.flush();
            creatingAdminTable(Integer.parseInt(inOStream.readUTF()), inOStream.readUTF());
            outOStream.close();
            outOStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAdminFromClient(){
        try {
            socket = new Socket("127.0.0.1", 8888);
            inOStream = new ObjectInputStream(socket.getInputStream());
            outOStream = new ObjectOutputStream(socket.getOutputStream());
            outOStream.flush();
            br = new BufferedReader(new InputStreamReader(System.in));
            outOStream.writeUTF("New Admin From Client");
            outOStream.flush();
            outOStream.writeUTF(ClientsAdminEmails.getValue());
            outOStream.flush();
            showAlert(inOStream.readUTF());
            creatingAdminTable(Integer.parseInt(inOStream.readUTF()), inOStream.readUTF());
            initAdminsFields(inOStream.readUTF());
            initClientsFields(inOStream.readUTF());

            outOStream.close();
            outOStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAdmin(){
        if (emailAdminError.getText().equals("") &&
                passwordAdminError.getText().equals("") &&
                lastnameAdminError.getText().equals("") &&
                nameAdminError.getText().equals("") &&
                phoneAdminError.getText().equals("")) {
            try {
                socket = new Socket("127.0.0.1", 8888);
                inOStream = new ObjectInputStream(socket.getInputStream());
                outOStream = new ObjectOutputStream(socket.getOutputStream());
                outOStream.flush();
                br = new BufferedReader(new InputStreamReader(System.in));
                outOStream.writeUTF("New Admin");
                outOStream.flush();
                outOStream.writeUTF(newAdminLastname.getText() + "|" +
                        newAdminName.getText() + "|" + newAdminPhone.getText() + "|" +
                        adminGender.getValue() + "|" + newAdminEmail.getText() + "|" +
                        bytesToHex(encodingPassword(newAdminPassword.getText())) + "|");
                outOStream.flush();
                showAlert(inOStream.readUTF());
                creatingAdminTable(Integer.parseInt(inOStream.readUTF()), inOStream.readUTF());
                initAdminsFields(inOStream.readUTF());

                outOStream.close();
                outOStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else showAlert("Проверьте введённые данные!");
    }

    private void initializeStaticFields() {
        ObservableList<String> genders = FXCollections.observableArrayList("Женский", "Мужской", "Не определён");
        adminGender.setItems(genders);
        adminGender.setValue("Женский"); // устанавливаем выбранный элемент по умолчанию

        genderFiltr.setItems(genders);
        genderFiltr.setValue("Женский");

        ObservableList<String> fieldsAdmin = FXCollections.observableArrayList("Имя", "Фамилия", "Телефон");
        fieldForEditAdmin.setItems(fieldsAdmin);
        fieldForEditAdmin.setValue("Имя");
    }

    private void initializeDynamicFields(String adminsEmails, String clientsEmails) {

        initAdminsFields(adminsEmails);
        initClientsFields(clientsEmails);
    }

    private void initClientsFields(String s) {
        ClientsAdminEmails.getItems().clear();
        String[] editAdminEmails = s.split("\\|");
        ObservableList<String> genders = FXCollections.observableArrayList(editAdminEmails);
        ClientsAdminEmails.setItems(genders);
        ClientsAdminEmails.setValue(editAdminEmails[0]); // устанавливаем выбранный элемент по умолчанию
    }

    private void initAdminsFields(String s) {
        editAdminEmail.getItems().clear();
        String[] editAdminEmails = s.split("\\|");
        ObservableList<String> genders = FXCollections.observableArrayList(editAdminEmails);
        editAdminEmail.setItems(genders);
        editAdminEmail.setValue(editAdminEmails[0]); // устанавливаем выбранный элемент по умолчанию

        deleteAdminEmail.getItems().clear();
        deleteAdminEmail.setItems(genders);
        deleteAdminEmail.setValue(editAdminEmails[0]);
    }

    private void creatingAdminTable(int count, String info) {
        adminTable.getColumns().clear();
        TableColumn<User, String> fullNameCol//
                = new TableColumn<>("ФИО");
        TableColumn<User, String> firstNameCol //
                = new TableColumn<>("Имя");
        TableColumn<User, String> lastNameCol //
                = new TableColumn<>("Фамилия");
        fullNameCol.getColumns().addAll(firstNameCol, lastNameCol);
        TableColumn<User, String> emailCol//
                = new TableColumn<>("Email");
        TableColumn<User, String> genderCol//
                = new TableColumn<>("Пол");
        TableColumn<User, String> phoneCol//
                = new TableColumn<>("Телефон");
        adminTable.setPlaceholder(new Label("Нет информации для отображения"));

        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastnameProperty());
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        genderCol.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        // Display row data
        ObservableList<User> list = getUserList(count, info);
        adminTable.getItems().clear();
        adminTable.setItems(list);

        adminTable.getColumns().addAll(emailCol, fullNameCol, genderCol, phoneCol);

    }

    private ObservableList<User> getUserList(int count, String allUsers) {

        ObservableList<User> users = FXCollections.observableArrayList();
        String[] usersCharacteristics = allUsers.split("\\|");

        for (int i = 0; i < count; i++) {
            users.add(new User(new SimpleStringProperty(usersCharacteristics[i * 5]),
                    new SimpleStringProperty(usersCharacteristics[i * 5 + 1]),
                    new SimpleStringProperty(usersCharacteristics[i * 5 + 2]),
                    new SimpleStringProperty(usersCharacteristics[i * 5 + 3]),
                    new SimpleStringProperty(usersCharacteristics[i * 5 + 4])));
        }

        return users;
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ощибка");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

}
