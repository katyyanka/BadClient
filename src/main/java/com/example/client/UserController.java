package com.example.client;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;

public class UserController {

    private String userEmail = null;
    @FXML
    Label email;
    @FXML
    TableView<Order> allOrdersTable;
    @FXML
    ChoiceBox<Integer> idOrdersForReserve;
    @FXML
    Button reserve;
    @FXML
    TableView<Order> usersOrderTable;
    @FXML
    ChoiceBox<Integer> idUserOrders;
    @FXML
    ChoiceBox<Integer> idHowManyFreePlaces;
    @FXML
    Button getCountOfFreePlaces;
    @FXML
    Slider priceForSearch;
    @FXML
    Button searchWithPrice;
    @FXML
    TextField idForSearch;
    @FXML
    Button searchWithID;
    @FXML
    Slider minPriceFilter;
    @FXML
    Slider maxPriceFilter;
    @FXML
    Button priceFilter;
    @FXML
    Button allOrders;
    @FXML
    DatePicker minDate;
    @FXML
    DatePicker maxDate;
    @FXML
    Button filterDate;
    @FXML
    Button    revoke;
    @FXML
    BarChart<String,Number> statistic;
    @FXML
    Button howManyMoneySpent;
    @FXML
    public Button exit;


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

    Socket socket = null;
    ObjectInputStream inOStream;
    ObjectOutputStream outOStream;
    BufferedReader br;

    public UserController(String email) {
        this.userEmail = email;
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        email.setText(userEmail);
        minDate.setValue(LocalDate.now());
        maxDate.setValue(LocalDate.now());
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
        reserve.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            reserve();
        });
        getCountOfFreePlaces.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            getCountOfFreePlaces();
        });
        searchWithPrice.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            searchWithPrice();
        });
        searchWithID.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            searchWithID();
        });
        priceFilter.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            priceFilter();
        });
        allOrders.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            allOrders();
        });
        filterDate.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            filterDate();
        });
        revoke.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            revoke();
        });
        howManyMoneySpent.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            howManyMoneySpent();
        });
        try {
            socket = new Socket("127.0.0.1", 8888);
            inOStream = new ObjectInputStream(socket.getInputStream());
            outOStream = new ObjectOutputStream(socket.getOutputStream());
            outOStream.flush();
            br = new BufferedReader(new InputStreamReader(System.in));
            outOStream.writeUTF("User First Window");
            outOStream.flush();
            outOStream.writeUTF(userEmail);
            outOStream.flush();
            creatingOrderTable(inOStream.readUTF());
            initializeOrderFields(inOStream.readUTF());
            creatingUsersOrderTable(inOStream.readUTF());
            initializeUserOrderFields(inOStream.readUTF());
            setStatistic(inOStream.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void howManyMoneySpent() {
        try {
            outOStream.writeUTF("howManyMoneySpent");
            outOStream.flush();
            outOStream.writeUTF(userEmail);
            outOStream.flush();
           showAlert("Вы уже потратили " + inOStream.readUTF() + " рублей с Шуточки-Маршурточки! Спасибо, что доверяете нам ♥");
        } catch (IOException e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void filterDate() {
        try {
            outOStream.writeUTF("filterDate");
            outOStream.flush();
            outOStream.writeUTF(String.valueOf(minDate.getValue()));
            outOStream.flush();
            outOStream.writeUTF(String.valueOf(maxDate.getValue()));
            outOStream.flush();
            creatingOrderTable(inOStream.readUTF());
        } catch (IOException e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void priceFilter() {
        try {
            outOStream.writeUTF("priceFilter");
            outOStream.flush();
            outOStream.writeUTF(String.valueOf(minPriceFilter.getValue()));
            outOStream.flush();
            outOStream.writeUTF(String.valueOf(maxPriceFilter.getValue()));
            outOStream.flush();
            creatingOrderTable(inOStream.readUTF());
        } catch (IOException e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void searchWithPrice() {
        try {
            outOStream.writeUTF("searchWithPrice");
            outOStream.flush();
            outOStream.writeUTF(String.valueOf(priceForSearch.getValue()));
            outOStream.flush();
            creatingOrderTable(inOStream.readUTF());
        } catch (IOException e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void searchWithID() {
        try {
            outOStream.writeUTF("searchWithID");
            outOStream.flush();
            outOStream.writeUTF(idForSearch.getText());
            outOStream.flush();
            creatingOrderTable(inOStream.readUTF());
        } catch (IOException e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void allOrders() {
        try {
            outOStream.writeUTF("allOrders");
            outOStream.flush();
            creatingOrderTable(inOStream.readUTF());
        } catch (IOException e) {
            showAlert("Нет соединения с сервером!");
        }
    }

    private void reserve() {
        if(idOrdersForReserve.getValue()==null) showAlert("Нет данных!");
        else {
            try {
                outOStream.writeUTF("Reserve");
                outOStream.flush();
                outOStream.writeUTF(userEmail);
                outOStream.flush();
                outOStream.writeUTF(String.valueOf(idOrdersForReserve.getValue()));
                outOStream.flush();
                showAlert(inOStream.readUTF());
                creatingOrderTable(inOStream.readUTF());
                creatingUsersOrderTable(inOStream.readUTF());
                initializeUserOrderFields(inOStream.readUTF());
            } catch (IOException e) {
                showAlert("Нет соединения с сервером!");
            }
        }
    }
    private void revoke() {
        if(idUserOrders.getValue()==null) showAlert("Нет данных!");
        else {
            try {
                outOStream.writeUTF("revoke");
                outOStream.flush();
                outOStream.writeUTF(userEmail);
                outOStream.flush();
                outOStream.writeUTF(String.valueOf(idUserOrders.getValue()));
                outOStream.flush();
                showAlert(inOStream.readUTF());
                creatingOrderTable(inOStream.readUTF());
                creatingUsersOrderTable(inOStream.readUTF());
                initializeUserOrderFields(inOStream.readUTF());
            } catch (IOException e) {
                showAlert("Нет соединения с сервером!");
            }
        }
    }

    private void initializeOrderFields(String orders) {
        String[] numbersString = orders.split("\\|");
        Integer[] numbers = new Integer[numbersString.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(numbersString[i]);
        }
        ObservableList<Integer> ids = FXCollections.observableArrayList(numbers);

        idOrdersForReserve.getItems().clear();
        idOrdersForReserve.setItems(ids);
        idHowManyFreePlaces.getItems().clear();
        idHowManyFreePlaces.setItems(ids);
        if (numbers.length > 0) {
            idOrdersForReserve.setValue(numbers[0]);
            idHowManyFreePlaces.setValue(numbers[0]);
        }
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

    private void creatingOrderTable(String orders) {
        allOrdersTable.getColumns().clear();
        TableColumn<Order, Integer> number = new TableColumn<>("№ Рейса");
        TableColumn<Order, Integer> carName = new TableColumn<>("№ Автомобиля");
        TableColumn<Order, Float> price = new TableColumn<>("Цена");
        TableColumn<Order, String> time = new TableColumn<>("Время");
        TableColumn<Order, String> date = new TableColumn<>("Дата");
        TableColumn<Order, Integer> sets = new TableColumn<>("Свободно мест");
        allOrdersTable.setPlaceholder(new Label("Нет информации для отображения"));

        number.setCellValueFactory(cellData -> (cellData.getValue().id.asObject()));
        carName.setCellValueFactory(cellData -> (cellData.getValue().caridProperty().asObject()));
        price.setCellValueFactory(cellData -> (cellData.getValue().priceProperty().asObject()));
        time.setCellValueFactory(cellData -> (cellData.getValue().timeProperty()));
        date.setCellValueFactory(cellData -> (cellData.getValue().dateProperty()));
        sets.setCellValueFactory(cellData -> (cellData.getValue().freePlacesProperty().asObject()));

        ObservableList<Order> list = getOrderList(orders);
        allOrdersTable.getItems().clear();
        allOrdersTable.setItems(list);
        allOrdersTable.getColumns().addAll(number, carName, date, time, price, sets);

    }

    private void creatingUsersOrderTable(String orders) {
        usersOrderTable.getColumns().clear();
        TableColumn<Order, Integer> number = new TableColumn<>("№ Рейса");
        TableColumn<Order, Integer> carName = new TableColumn<>("№ Автомобиля");
        TableColumn<Order, Float> price = new TableColumn<>("Цена");
        TableColumn<Order, String> time = new TableColumn<>("Время");
        TableColumn<Order, String> date = new TableColumn<>("Дата");
        TableColumn<Order, Integer> sets = new TableColumn<>("Свободно мест");
        usersOrderTable.setPlaceholder(new Label("Увас пока нет забронированных рейсов"));

        number.setCellValueFactory(cellData -> (cellData.getValue().id.asObject()));
        carName.setCellValueFactory(cellData -> (cellData.getValue().caridProperty().asObject()));
        price.setCellValueFactory(cellData -> (cellData.getValue().priceProperty().asObject()));
        time.setCellValueFactory(cellData -> (cellData.getValue().timeProperty()));
        date.setCellValueFactory(cellData -> (cellData.getValue().dateProperty()));
        sets.setCellValueFactory(cellData -> (cellData.getValue().freePlacesProperty().asObject()));

        ObservableList<Order> list = getOrderList(orders);
        usersOrderTable.getItems().clear();
        usersOrderTable.setItems(list);
        usersOrderTable.getColumns().addAll(number, carName, date, time, price, sets);

    }

    private void initializeUserOrderFields(String orders) {
        String[] numbersString = orders.split("\\|");
        Integer[] numbers = new Integer[numbersString.length];
        try {
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = Integer.parseInt(numbersString[i]);
            }

            ObservableList<Integer> ids = FXCollections.observableArrayList(numbers);

            idUserOrders.getItems().clear();
            idUserOrders.setItems(ids);
            if (numbers.length > 0) {
                idUserOrders.setValue(numbers[0]);
            }
        } catch (NumberFormatException e){

        }
    }

    private void getCountOfFreePlaces() {
        if (idHowManyFreePlaces.getValue()==null) showAlert("Нет данных!");
        else {
            try {
                outOStream.writeUTF("Free Places");
                outOStream.flush();
                outOStream.writeUTF(String.valueOf(idHowManyFreePlaces.getValue()));
                outOStream.flush();
                showAlert(inOStream.readUTF());
            } catch (IOException e) {
                showAlert("Нет соединения с сервером!");
            }
        }
    }

    private void setStatistic(String idWithPrice){
        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>();
        String[] orders = idWithPrice.split("\\|");
        try{
        for (int i = 0; i < orders.length; i++) {
            dataSeries1.getData().add(new XYChart.Data<>(orders[i], Double.parseDouble(orders[++i])));
        }
        statistic.getData().add(dataSeries1);}catch (NumberFormatException e) {}
    }

}
