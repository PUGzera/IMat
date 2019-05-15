package imat.controller;

import imat.components.Wizard;
import imat.entities.ProductRepo;
import imat.entities.ShoppingItem;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import imat.state.ShoppingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class MainController {

    @Autowired
    private ProductRepo productRepo;

    private ShoppingState observable;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void initialize() throws IOException {
        observable = new ShoppingState();
        observable.addProducts(StreamSupport.stream(productRepo
                .findAllByCategory("CABBAGE").spliterator(), false)
                .map(p -> new ShoppingItem(p, 1))
                .collect(Collectors.toList()));
        Wizard wizard = Wizard.getInstance(observable);
        mainPane.getChildren().add(wizard);
    }

}
