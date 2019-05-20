package imat.components;

import imat.entities.Order;
import imat.entities.ShoppingItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.List;

public class ReceiptPane extends AnchorPane {

    @FXML
    private Label orderIdLabel;

    @FXML
    private TextFlow billingInfoTextFlow;

    @FXML
    private ListView<CartItem> productsListView;

    @FXML
    private Button doneButton;

    private static ReceiptPane ourInstance = null;

    private FXMLLoader root;

    private Wizard wizard;

    private ReceiptPane(Order order, Wizard wizard) {
        root = new FXMLLoader(getClass().getResource("/fxml/receipt.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            this.wizard = wizard;
            orderIdLabel.setText("Order #"+order.getId());
            order.getMap().forEach((k, v) -> productsListView.getItems().add(new CartItem(new ShoppingItem(k, v))));
            doneButton.onActionProperty().setValue(e -> wizard.resetWizard());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ReceiptPane getInstance(Order order, Wizard wizard) {
        if(ourInstance == null) ourInstance = new ReceiptPane(order, wizard);
        return ourInstance;
    }
}
