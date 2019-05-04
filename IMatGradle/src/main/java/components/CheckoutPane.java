package components;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CheckoutPane extends AnchorPane {
    private static CheckoutPane ourInstance = new CheckoutPane();

    private FXMLLoader root;

    private CheckoutPane() {
        root = new FXMLLoader(getClass().getResource("/fxml/checkout.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CheckoutPane getInstance() {
        return ourInstance;
    }
}
