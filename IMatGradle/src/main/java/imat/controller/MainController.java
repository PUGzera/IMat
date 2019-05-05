package imat.controller;

import imat.components.Header;
import imat.components.Wizard;
import imat.user.UserRepo;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import imat.state.ShoppingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Component
public class MainController {

    @Autowired
    private UserRepo userRepo;

    private ShoppingState observable;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void initialize() throws IOException {
        observable = new ShoppingState(userRepo);
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
