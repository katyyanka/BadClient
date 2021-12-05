package com.example.client;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AdminController implements Encoding {

    public Label email;

    @FXML
    private Button newAdminFromClient;
    @FXML
    private Label name;
    @FXML
    private Button exit;
    @FXML
    private TableView clientTable;
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
    private ChoiceBox<String> editClientEmail;
    @FXML
    private ChoiceBox<String> deleteClientEmail;
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
    Label emailClientError;
    @FXML
    Label lastnameClientError;
    @FXML
    Label nameClientError;
    @FXML
    Label phoneClientError;
    @FXML
    Label passwordClientError;
    @FXML
    Button newAdmin;
    @FXML
    Button newClient;
    @FXML
    Button deleteAdmin;
    @FXML
    Button AdminFiltr;
    @FXML
    private TextField newClientEmail;
    @FXML
    private TextField newClientLastname;
    @FXML
    private TextField newClientName;
    @FXML
    private TextField newClientPhone;
    @FXML
    private TextField newClientPassword;
    @FXML
    private ComboBox<String> clientGender;
    @FXML
    private TableView<Car> carTable;
    @FXML
    ChoiceBox<Integer> clientOrder;
    @FXML
    Button watchClientsOrder;
    @FXML
    TextField carName;
    @FXML
    Slider numberOfSets;
    @FXML
    ChoiceBox editClientField;
    @FXML
    Button deleteClient;
    @FXML
    ChoiceBox<Integer> carDeleteID;
    @FXML
    ChoiceBox<Integer> editIDCar;
    @FXML
    Button newCar;
    @FXML
    Button editCar;
    @FXML
    Button deleteCar;
    @FXML
    Button newOrder;
    @FXML
    DatePicker dateOrder;
    @FXML
    ChoiceBox<Integer> idCarOrder;
    @FXML
    Slider hourOrder;
    @FXML
    Slider minuteOrder;
    @FXML
    Slider priceOrder;
    @FXML
    TableView<Order> orderTable;
    @FXML
    ChoiceBox<Integer> idOrderDelete;
    @FXML
    Button deleteOrder;
    @FXML
    PieChart pieChart;
    @FXML
    AnchorPane anchorPane;
    /*@FXML
    Button countMargin;
    @FXML
    ChoiceBox<Integer> idMargin;*/

    private class Order {
        IntegerProperty id;
        IntegerProperty carid;
        FloatProperty price;
        StringProperty time;
        StringProperty date;
        IntegerProperty freePlaces;

        public Order(IntegerProperty id, IntegerProperty carid, FloatProperty price, StringProperty time, StringProperty date, IntegerProperty freePlaces) {
            this.id = id;
            this.carid = carid;
            this.price = price;
            this.time = time;
            this.date = date;
            this.freePlaces = freePlaces;
        }


        public Order() {
            this.carid = new SimpleIntegerProperty();
            this.price = new SimpleFloatProperty();
            this.time = new SimpleStringProperty();
            this.date = new SimpleStringProperty();
            this.freePlaces = new SimpleIntegerProperty();
        }

        public int getCarid() {
            return carid.get();
        }

        public IntegerProperty caridProperty() {
            return carid;
        }

        public float getPrice() {
            return price.get();
        }

        public FloatProperty priceProperty() {
            return price;
        }

        public String getTime() {
            return time.get();
        }

        public StringProperty timeProperty() {
            return time;
        }

        public String getDate() {
            return date.get();
        }

        public StringProperty dateProperty() {
            return date;
        }

        public int getFreePlaces() {
            return freePlaces.get();
        }

        public IntegerProperty freePlacesProperty() {
            return freePlaces;
        }
    }

    public String userEmail;
    Socket socket = null;
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
        newClientPhone.textProperty().addListener((observable, oldValue, newValue) -> checking("[+][0-9]{12,13}", newValue, phoneClientError));
        newClientLastname.textProperty().addListener((observable, oldValue, newValue) -> checking("[a-zA-Zа-яА-ЯёЁ -]+", newValue, lastnameClientError));
        newClientName.textProperty().addListener((observable, oldValue, newValue) -> checking("[a-zA-Zа-яА-ЯёЁ -]+", newValue, nameClientError));
        newClientEmail.textProperty().addListener((observable, oldValue, newValue) -> checking("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", newValue, emailClientError));
        newClientPassword.textProperty().addListener((observable, oldValue, newValue) -> checking(".{6,20}", newValue, passwordClientError));
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
            creatingClientTable(inOStream.readUTF());
            creatingCarTable(inOStream.readUTF());
            initializeCarFields(inOStream.readUTF());
            creatingOrderTable(inOStream.readUTF());
            initializeOrderFields(inOStream.readUTF());
            System.out.println(1);
            createPieChart(inOStream.readUTF(),inOStream.readUTF(),inOStream.readUTF());
            System.out.println(2);

        } catch (Exception e) {
            //showAlert("Нет соединения с сервером!");
        }

        deleteClient.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            deleteClient();
        });
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                outOStream.close();
                outOStream.close();
                socket.close();
            } catch (IOException e) {
            }
            finally {
                try {
                    openNewWindow("hello-view.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        newClient.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            addClient();
        });
        newCar.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            addCar();
        });
        editCar.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            editCar();
        });
        deleteCar.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            deleteCar();
        });
        newOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            newOrder();
        });
        deleteOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            deleteOrder();
        });
      /*  countMargin.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            countMargin();
        });*/
    }

    private void countMargin() {
    /*    try {
            outOStream.writeUTF("Count Margin");
            outOStream.flush();
            outOStream.writeUTF(idMargin.getValue().toString());
            outOStream.flush();
            showAlert(inOStream.readUTF());
        } catch (Exception e) {
            showAlert("Нет соединения с сервером!");
        }*/
    }

    private void deleteOrder(){
        try {
            outOStream.writeUTF("Delete Order");
            outOStream.flush();
            outOStream.writeUTF(String.valueOf(idOrderDelete.getValue()));
            outOStream.flush();
            showAlert(inOStream.readUTF());
            creatingOrderTable(inOStream.readUTF());
            initializeOrderFields(inOStream.readUTF());
        } catch (Exception e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void newOrder() {
        String information = idCarOrder.getValue() + "|"
                + priceOrder.getValue() + "|"
                + (int)hourOrder.getValue() + ":" +
                (int) minuteOrder.getValue() + "|" +
                dateOrder.getValue() + "|";
        try{
        outOStream.writeUTF("New Order");
        outOStream.flush();
        outOStream.writeUTF(information);
        outOStream.flush();
        showAlert(inOStream.readUTF());
        creatingOrderTable(inOStream.readUTF());
        initializeOrderFields(inOStream.readUTF());
        }catch(Exception e){
            showAlert("Нет соединения с сервером!");
        }
    }

    private void creatingOrderTable(String orders) {
        orderTable.getColumns().clear();
        TableColumn<Order, Integer> number = new TableColumn<>("№ Рейса");
        TableColumn<Order, Integer> carName = new TableColumn<>("№ Автомобиля");
        TableColumn<Order, Float> price = new TableColumn<>("Цена");
        TableColumn<Order, String> time = new TableColumn<>("Время");
        TableColumn<Order, String> date = new TableColumn<>("Дата");
        TableColumn<Order, Integer> sets = new TableColumn<>("Свободно мест");
        orderTable.setPlaceholder(new Label("Нет информации для отображения"));

        number.setCellValueFactory(cellData -> (cellData.getValue().id.asObject()));
        carName.setCellValueFactory(cellData -> (cellData.getValue().caridProperty().asObject()));
        price.setCellValueFactory(cellData -> (cellData.getValue().priceProperty().asObject()));
        time.setCellValueFactory(cellData -> (cellData.getValue().timeProperty()));
        date.setCellValueFactory(cellData -> (cellData.getValue().dateProperty()));
        sets.setCellValueFactory(cellData -> (cellData.getValue().freePlacesProperty().asObject()));

        ObservableList<Order> list = getOrderList(orders);
        orderTable.getItems().clear();
        orderTable.setItems(list);
        orderTable.getColumns().addAll(number, carName, date, time, price, sets);

    }

    private void deleteAdmin() {
        try {
            outOStream.writeUTF("Delete Admin");
            outOStream.flush();
            outOStream.writeUTF(deleteAdminEmail.getValue());
            outOStream.flush();
            showAlert(inOStream.readUTF());
            if (deleteAdminEmail.getValue().equals(email.getText())) {
                inOStream.readUTF();
                inOStream.readUTF();
                inOStream.readUTF();
                openNewWindow("hello-view.fxml");
            } else {
                creatingAdminTable(Integer.parseInt(inOStream.readUTF()), inOStream.readUTF());
                initAdminsFields(inOStream.readUTF());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteClient() {
        try {
            outOStream.writeUTF("Delete Client");
            outOStream.flush();
            outOStream.writeUTF(deleteClientEmail.getValue());
            outOStream.flush();
            showAlert(inOStream.readUTF());
            creatingClientTable(inOStream.readUTF());
            initClientsFields(inOStream.readUTF());
        } catch (Exception e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void filtrAdmin() {
        try {
            outOStream.writeUTF("Filtr Admin");
            outOStream.flush();
            outOStream.writeUTF(genderFiltr.getValue());
            outOStream.flush();
            creatingAdminTable(inOStream.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void creatingAdminTable(String readUTF) {
        System.out.println(readUTF);
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
        ObservableList<User> list = getUserList(readUTF);
        adminTable.getItems().clear();
        adminTable.setItems(list);
        adminTable.getColumns().addAll(emailCol, fullNameCol, genderCol, phoneCol);
    }

    private void addAdminFromClient() {
        try {
            outOStream.writeUTF("New Admin From Client");
            outOStream.flush();
            outOStream.writeUTF(ClientsAdminEmails.getValue());
            outOStream.flush();
            showAlert(inOStream.readUTF());
            creatingAdminTable(Integer.parseInt(inOStream.readUTF()), inOStream.readUTF());
            initAdminsFields(inOStream.readUTF());
            initClientsFields(inOStream.readUTF());
            creatingClientTable(inOStream.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAdmin() {
        if (emailAdminError.getText().equals("") &&
                passwordAdminError.getText().equals("") &&
                lastnameAdminError.getText().equals("") &&
                nameAdminError.getText().equals("") &&
                phoneAdminError.getText().equals("")) {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else showAlert("Проверьте введённые данные!");
    }

    private void addClient() {
        if (emailClientError.getText().equals("") &&
                passwordClientError.getText().equals("") &&
                lastnameClientError.getText().equals("") &&
                nameClientError.getText().equals("") &&
                phoneClientError.getText().equals("")) {
            try {
                outOStream.writeUTF("New Client");
                outOStream.flush();
                outOStream.writeUTF(newClientLastname.getText() + "|" +
                        newClientName.getText() + "|" + newClientPhone.getText() + "|" +
                        clientGender.getValue() + "|" + newClientEmail.getText() + "|" +
                        bytesToHex(encodingPassword(newClientPassword.getText())) + "|");
                outOStream.flush();
                showAlert(inOStream.readUTF());
                creatingClientTable((inOStream.readUTF()));
                initClientsFields(inOStream.readUTF());
            } catch (Exception e) {
                showAlert("Нет соединения с сервером!");
            }

        } else showAlert("Проверьте введённые данные!");
    }

    private void initializeStaticFields() {
        ObservableList<String> genders = FXCollections.observableArrayList("Женский", "Мужской", "Не определён");
        adminGender.setItems(genders);
        adminGender.setValue("Женский"); // устанавливаем выбранный элемент по умолчанию
        ObservableList<String> gendersFilter = FXCollections.observableArrayList("Женский", "Мужской", "Не определён", "ВСЕ");
        genderFiltr.setItems(gendersFilter);
        genderFiltr.setValue("Женский");

        clientGender.setItems(genders);
        clientGender.setValue("Женский");

        ObservableList<String> fieldsAdmin = FXCollections.observableArrayList("Имя", "Фамилия", "Телефон");
        fieldForEditAdmin.setItems(fieldsAdmin);
        fieldForEditAdmin.setValue("Имя");

        editClientField.setItems(fieldsAdmin);
        editClientField.setValue("Имя");
    }

    private void initializeDynamicFields(String adminsEmails, String clientsEmails) {
        initAdminsFields(adminsEmails);
        initClientsFields(clientsEmails);
    }

    private void initializeCarFields(String cars) {

        String[] numbersString = cars.split("\\|");
        Integer[] numbers = new Integer[numbersString.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(numbersString[i]);
        }
        ObservableList<Integer> ids = FXCollections.observableArrayList(numbers);
        editIDCar.getItems().clear();
        editIDCar.setItems(ids);
        carDeleteID.getItems().clear();
        carDeleteID.setItems(ids);
        idCarOrder.getItems().clear();
        idCarOrder.setItems(ids);
        if (numbers.length > 0) {
            carDeleteID.setValue(numbers[0]);
            editIDCar.setValue(numbers[0]);
            idCarOrder.setValue(numbers[0]);
        }// устанавливаем выбранный элемент по умолчанию
    }

    private void initializeOrderFields(String orders) {
        dateOrder.setValue(LocalDate.now());
        String[] numbersString = orders.split("\\|");
        Integer[] numbers = new Integer[numbersString.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(numbersString[i]);
        }
        ObservableList<Integer> ids = FXCollections.observableArrayList(numbers);

        idOrderDelete.getItems().clear();
        idOrderDelete.setItems(ids);
//        idMargin.getItems().clear();
//        idMargin.setItems(ids);
        clientOrder.getItems().clear();
        clientOrder.setItems(ids);
        if (numbers.length > 0) {
            clientOrder.setValue(numbers[0]);
            idOrderDelete.setValue(numbers[0]);
//            idMargin.setValue(numbers[0]);
        }
    }

    private void initClientsFields(String s) {
        ClientsAdminEmails.getItems().clear();
        String[] editAdminEmails = s.split("\\|");
        ObservableList<String> emails = FXCollections.observableArrayList(editAdminEmails);
        ClientsAdminEmails.setItems(emails);
        editClientEmail.setItems(emails);
        deleteClientEmail.setItems(emails);
        if (editAdminEmails.length > 0) {
            editClientEmail.setValue(editAdminEmails[0]);
            deleteClientEmail.setValue(editAdminEmails[0]);
            ClientsAdminEmails.setValue(editAdminEmails[0]);
        }// устанавливаем выбранный элемент по умолчанию
    }

    private void initAdminsFields(String s) {
        editAdminEmail.getItems().clear();
        String[] editAdminEmails = s.split("\\|");
        ObservableList<String> genders = FXCollections.observableArrayList(editAdminEmails);
        editAdminEmail.setItems(genders);
        deleteAdminEmail.getItems().clear();
        deleteAdminEmail.setItems(genders);
        if (editAdminEmails.length > 0) {
            editAdminEmail.setValue(editAdminEmails[0]); // устанавливаем выбранный элемент по умолчанию
            deleteAdminEmail.setValue(editAdminEmails[0]);
        }
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

    private void creatingClientTable(String info) {
        clientTable.getColumns().clear();
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
        clientTable.setPlaceholder(new Label("Нет информации для отображения"));

        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastnameProperty());
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        genderCol.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());

        // Display row data
        ObservableList<User> list = getUserList(info);
        clientTable.getItems().clear();
        clientTable.setItems(list);
        clientTable.getColumns().addAll(emailCol, fullNameCol, genderCol, phoneCol);

    }

    private void creatingCarTable(String cars) {
        carTable.getColumns().clear();
        TableColumn<Car, Integer> number//
                = new TableColumn<>("№");
        TableColumn<Car, String> name //
                = new TableColumn<>("Наименование");
        TableColumn<Car, Integer> sets //
                = new TableColumn<>("Количество мест");
        carTable.setPlaceholder(new Label("Нет информации для отображения"));
        number.setCellValueFactory(cellData -> (cellData.getValue().idProperty().asObject()));
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        sets.setCellValueFactory(cellData -> cellData.getValue().numberOfPlacesProperty().asObject());
        ObservableList<Car> list = getCarList(cars);
        carTable.getItems().clear();
        carTable.setItems(list);
        carTable.getColumns().addAll(number, name, sets);

    }

    private ObservableList<User> getUserList(int count, String allUsers) {

        ObservableList<User> users = FXCollections.observableArrayList();
        String[] usersCharacteristics = allUsers.split("\\|");
        int NUMBER_OF_USER_CHARACTERISTICS = 5;
        for (int i = 0; i < count; i++) {
            users.add(new User(new SimpleStringProperty(usersCharacteristics[i * NUMBER_OF_USER_CHARACTERISTICS]),
                    new SimpleStringProperty(usersCharacteristics[i * NUMBER_OF_USER_CHARACTERISTICS + 1]),
                    new SimpleStringProperty(usersCharacteristics[i * NUMBER_OF_USER_CHARACTERISTICS + 2]),
                    new SimpleStringProperty(usersCharacteristics[i * NUMBER_OF_USER_CHARACTERISTICS + 3]),
                    new SimpleStringProperty(usersCharacteristics[i * NUMBER_OF_USER_CHARACTERISTICS + 4])));
        }
        return users;
    }

    private ObservableList<User> getUserList(String allUsers) {

        ObservableList<User> users = FXCollections.observableArrayList();
        String[] usersCharacteristics = allUsers.split("\\|");

        for (int i = 0; i < usersCharacteristics.length / 5; i++) {
            users.add(new User(new SimpleStringProperty(usersCharacteristics[i * 5]),
                    new SimpleStringProperty(usersCharacteristics[i * 5 + 1]),
                    new SimpleStringProperty(usersCharacteristics[i * 5 + 2]),
                    new SimpleStringProperty(usersCharacteristics[i * 5 + 3]),
                    new SimpleStringProperty(usersCharacteristics[i * 5 + 4])));
        }

        return users;
    }

    private class Car {
        IntegerProperty id;
        StringProperty name;
        IntegerProperty numberOfPlaces;

        public int getId() {
            return id.get();
        }

        public IntegerProperty idProperty() {
            return id;
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public int getNumberOfPlaces() {
            return numberOfPlaces.get();
        }

        public IntegerProperty numberOfPlacesProperty() {
            return numberOfPlaces;
        }

        public Car(IntegerProperty id, IntegerProperty numberOfPlaces, StringProperty name) {
            this.id = id;
            this.name = name;
            this.numberOfPlaces = numberOfPlaces;
        }
    }

    private ObservableList<Car> getCarList(String allCars) {
        ObservableList<Car> cars = FXCollections.observableArrayList();
        String[] carsCharacteristics = allCars.split("\\|");
        int NUMBER_OF_CAR_CHARACTERISTICS = 3;

        for (int i = 0; i < carsCharacteristics.length / NUMBER_OF_CAR_CHARACTERISTICS; i++) {
            cars.add(new Car(new SimpleIntegerProperty(Integer.parseInt(carsCharacteristics[i * NUMBER_OF_CAR_CHARACTERISTICS])),
                    new SimpleIntegerProperty(Integer.parseInt(carsCharacteristics[i * NUMBER_OF_CAR_CHARACTERISTICS + 1])),
                    new SimpleStringProperty(carsCharacteristics[i * NUMBER_OF_CAR_CHARACTERISTICS + 2])));
        }
        return cars;
    }

    private ObservableList<Order> getOrderList(String allOrders) {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String[] ordersCharacteristics = allOrders.split("\\|");
        int NUMBER_OF_ORDER_CHARACTERISTICS = 6;

        for (int i = 0; i < ordersCharacteristics.length / NUMBER_OF_ORDER_CHARACTERISTICS; i++) {
            orders.add(new Order(
                    new SimpleIntegerProperty(Integer.parseInt(ordersCharacteristics[i * NUMBER_OF_ORDER_CHARACTERISTICS])),
                    new SimpleIntegerProperty(Integer.parseInt(ordersCharacteristics[i * NUMBER_OF_ORDER_CHARACTERISTICS + 1])),
                    new SimpleFloatProperty(Float.parseFloat(ordersCharacteristics[i * NUMBER_OF_ORDER_CHARACTERISTICS + 2])),
                    new SimpleStringProperty(ordersCharacteristics[i * NUMBER_OF_ORDER_CHARACTERISTICS + 3]),
                    new SimpleStringProperty(ordersCharacteristics[i * NUMBER_OF_ORDER_CHARACTERISTICS + 4]),
                    new SimpleIntegerProperty(Integer.parseInt(ordersCharacteristics[i * NUMBER_OF_ORDER_CHARACTERISTICS + 5]))));
        }
        return orders;
    }

    private void addCar() {
        try {
            outOStream.writeUTF("New Car");
            outOStream.flush();
            outOStream.writeUTF(carName.getText() + "|" +
                    (int) numberOfSets.getValue() + "|");
            outOStream.flush();
            showAlert(inOStream.readUTF());
            creatingCarTable(inOStream.readUTF());
            initializeCarFields(inOStream.readUTF());
        } catch (Exception e) {
            showAlert("Нет соединени с сервером!");
        }
    }

    private void deleteCar() {
        try {
            outOStream.writeUTF("Delete Car");
            outOStream.flush();
            outOStream.writeUTF(String.valueOf(carDeleteID.getValue()));
            outOStream.flush();
            showAlert(inOStream.readUTF());
            creatingCarTable(inOStream.readUTF());
            initializeCarFields(inOStream.readUTF());
            creatingOrderTable(inOStream.readUTF());
            System.out.println(2);
            initializeOrderFields(inOStream.readUTF());
            System.out.println(3);
        } catch (Exception e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void editCar() {}

    private void createPieChart(String countWomen, String countMen, String countNotIdent){

ObservableList<PieChart.Data> slices =FXCollections.observableArrayList(
        new PieChart.Data("Женский", Integer.parseInt(countWomen)),
        new PieChart.Data("Мужской", Integer.parseInt(countMen)),
        new PieChart.Data("Не определён", Integer.parseInt(countNotIdent)));

        pieChart.setData(slices);
        pieChart.setLegendSide(Side.LEFT);
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
