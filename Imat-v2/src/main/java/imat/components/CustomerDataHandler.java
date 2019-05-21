package imat.components;


import imat.state.Observer;
import imat.state.ShoppingState;
import imat.util.OrderHistoryHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CustomerDataHandler extends AnchorPane implements Observer {

    @FXML Button shoppingListBtn;
    @FXML Button orderHistoryBtn;
    @FXML Button customerDataBtn;

    @FXML AnchorPane shoppingListPane;
    @FXML AnchorPane customerDataPane;
    @FXML AnchorPane orderHistoryPane;
    @FXML AnchorPane customerDataHandlingPane;
    @FXML VBox innerShopingVBox;
    @FXML VBox currentOrderPane;

    private OrderHistoryHandler orderHistoryHandler;

    private ShoppingState shoppingState;

    public CustomerDataHandler(ShoppingState shoppingState) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/customer_data.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.shoppingState = shoppingState;
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        customerDataPaneToFront();
    }


    private void fillOrderHistoryData(){
        orderHistoryHandler = new OrderHistoryHandler(shoppingState);
        currentOrderPane.getChildren().addAll(orderHistoryHandler.getOrderHistories());
    }


    /**
     * Se till att ladda in den nya Modellen direkt.
     */
    @FXML
    public void customerDataPaneToFront(){
        customerDataPane.getChildren().add(new CustomerContactInfoHandler(shoppingState));
        customerDataPane.toFront();

    }

    @FXML
    public void orderHistoryToFront(){
        if(orderHistoryHandler==null){
            fillOrderHistoryData();
        }
        orderHistoryPane.toFront();
    }

    @FXML
    public void shoppingListToFront(){
        shoppingListPane.toFront();
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
        // vad du nu vill göra när staten ändras
    }
}
