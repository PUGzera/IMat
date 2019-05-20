package imat.controller;

import imat.components.ShoppingCart;
import imat.components.Wizard;
import imat.entities.OrderRepo;
import imat.entities.ProductRepo;
import imat.entities.ShoppingItem;
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

    private ShoppingCart shoppingCart;

    @FXML
    private void initialize() throws IOException {
        observable = new ShoppingState(orderRepo);
        wizard = Wizard.getInstance(observable, this);
        shoppingCart = ShoppingCart.getInstance(observable);
        shoppingCartPane.getChildren().add(shoppingCart);
        observable.addProducts(StreamSupport.stream(productRepo
                .findByNameLike("bröd").spliterator(), false)
                .map(p -> new ShoppingItem(p, 1))
                .collect(Collectors.toList()));
    }

    @FXML
    private void toCheckout() {
        dynamicPane.setContent(wizard);
        shoppingCartPane.setVisible(false);
    }

    @FXML
    public void goHome() {
        dynamicPane.setContent(new AnchorPane());
        shoppingCartPane.setVisible(true);
    }

    public void resetWizard() {
        wizard = Wizard.getInstance(observable, this);
    }

}
