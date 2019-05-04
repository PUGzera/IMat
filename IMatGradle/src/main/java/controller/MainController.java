package controller;

import components.Header;
import components.Wizard;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import state.Observable;
import state.ShoppingState;

import java.io.IOException;

public class MainController {

    private ShoppingState observable;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void initialize() throws IOException {
        observable = new ShoppingState();
        Header header = new Header();
        header.setLayoutX(0);
        header.setLayoutY(0);
        mainPane.getChildren().add(header);
        Wizard wizard = Wizard.getInstance(observable);
        wizard.setLayoutX(280);
        wizard.setLayoutY(75);
        mainPane.getChildren().add(wizard);
    }

}
