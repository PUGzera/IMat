package imat.components;


import imat.entities.Order;
import imat.state.Observer;
import imat.state.ShoppingState;
import imat.util.OrderHistoryHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static imat.validators.FormValidators.validFutureDate;

public class CustomerDataHandler extends AnchorPane implements Observer {

    private static CustomerDataHandler ourInstance = null;

    @FXML Button shoppingListBtn;
    @FXML Button orderHistoryBtn;
    @FXML Button customerDataBtn;

//    @FXML AnchorPane shoppingListPane;
    @FXML AnchorPane customerDataPane;
    @FXML AnchorPane orderHistoryPane;
    @FXML AnchorPane customerDataHandlingPane;
    @FXML VBox innerShopingVBox;
    @FXML VBox currentOrderPane;

    @FXML
    private Label activeOrderLabel;

    private OrderHistoryHandler orderHistoryHandler;

    private CustomerContactInfoHandler customerContactInfoHandler;

    private ShoppingState shoppingState;

    private CustomerDataHandler(ShoppingState shoppingState) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/customer_data.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.shoppingState = shoppingState;
        this.customerContactInfoHandler = new CustomerContactInfoHandler(shoppingState);
        try {
            fxmlLoader.load();
            updateActiveOrderLabel();
            customerDataPane.getChildren().add(customerContactInfoHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        customerDataPaneToFront();
    }

    public static CustomerDataHandler getInstance(ShoppingState shoppingState) {
        if(ourInstance == null) ourInstance = new CustomerDataHandler(shoppingState);
        return ourInstance;
    }


    private void fillOrderHistoryData(){
        orderHistoryHandler = new OrderHistoryHandler(getActiveOrders());
        Platform.runLater( () -> currentOrderPane.getChildren().addAll(orderHistoryHandler.getOrderHistories()));
    }

    public void fillOrderTask(){
        Thread backgroundThread = new Thread(this::fillOrderHistoryData);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }


    /**
     * Se till att ladda in den nya Modellen direkt.
     */
    @FXML
    public void customerDataPaneToFront(){
        customerDataPane.toFront();
    }

    @FXML
    public void orderHistoryToFront(){
        if(orderHistoryHandler==null){
            fillOrderTask();
        }
        orderHistoryPane.toFront();
    }

//    @FXML
//    public void shoppingListToFront(){
////        innerShopingVBox.getChildren().add(new SavedList(this.shoppingState));
//        shoppingListPane.toFront();
//    }


    private void updateActiveOrderLabel(){
        long activeOrderCount = getActiveOrders().size();
        activeOrderLabel.setText("Aktiva ordrar ( "+activeOrderCount+" st )");
    }

    public List<Order> getActiveOrders() {
        return StreamSupport.stream(shoppingState.getOrderRepo().findAllByOrderByLocalDate().spliterator(), false)
                .filter(o -> validFutureDate(o.getLocalDate()))
                .collect(Collectors.toList());
    }




    @Override
    public void subscribe() {
        shoppingState.addObserver(this);
    }

    @Override
    public void unsubscribe() {
        shoppingState.removeObserver(this);
    }

    @Override
    public void update() {
        updateActiveOrderLabel();
        fillOrderHistoryData();
        customerContactInfoHandler.update();
    }
}
