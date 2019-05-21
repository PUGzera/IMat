package imat.controller;

import imat.components.CustomerDataHandler;
import imat.components.ShoppingCart;
import imat.components.Wizard;
import imat.entities.OrderRepo;
import imat.entities.ProductRepo;
import imat.entities.ShoppingItem;
import imat.util.OrderHistoryHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import imat.state.ShoppingState;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class MainController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    private ShoppingState observable;

    @FXML
    private StackPane mainPane;

    @FXML
    private AnchorPane shoppingCartPane;

    @FXML
    private ScrollPane dynamicPane;

    private Wizard wizard;

    private CustomerDataHandler customerDataHandler;

    private ShoppingCart shoppingCart;

    private Page page = Page.HOME;

    @FXML
    private void initialize() throws IOException {
        observable = new ShoppingState(orderRepo);
        customerDataHandler = new CustomerDataHandler(observable);
        wizard = Wizard.getInstance(observable, this);
        shoppingCart = ShoppingCart.getInstance(observable);
        shoppingCartPane.getChildren().add(shoppingCart);
        observable.addProducts(StreamSupport.stream(productRepo
                .findByNameLike("bröd").spliterator(), false)
                .map(p -> new ShoppingItem(p, 1))
                .collect(Collectors.toList()));
    }

    private void loadPage() {
        shoppingCartPane.setVisible(true);
        switch (page) {
            case HOME:
                dynamicPane.setContent(new AnchorPane());
                break;
            case WIZARD:
                shoppingCartPane.setVisible(false);
                dynamicPane.setContent(wizard);
                break;
            case MY_PAGE:
                dynamicPane.setContent(customerDataHandler);
                break;
        }
    }

    @FXML
    private void toCheckout() {
        page = Page.WIZARD;
        loadPage();
    }

    @FXML
    public void goHome() {
        page = Page.HOME;
        loadPage();
    }

    @FXML
    public void enableMyPage(){
        page = Page.MY_PAGE;
        loadPage();
        /*if(!mainPane.getChildren().contains(customerDataHandler)){
            mainPane.getChildren().add(customerDataHandler);
        }
        else{
            int index = mainPane.getChildren().indexOf(customerDataHandler);
            mainPane.getChildren().get(index).toFront();
        }*/
    }

    public void resetWizard() {
        wizard = Wizard.getInstance(observable, this);
    }

    public enum Page {
        WIZARD, MY_PAGE, PRODUCT_VIEW, HOME
    }

}
