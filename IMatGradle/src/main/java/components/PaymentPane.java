package components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PaymentPane extends AnchorPane {

    @FXML
    private RadioButton invoiceRadioButton, cardRadioButton;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private static PaymentPane ourInstance = new PaymentPane();

    private FXMLLoader root;

    private PaymentPane() {
        root = new FXMLLoader(getClass().getResource("/fxml/payment.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            invoiceRadioButton.setToggleGroup(toggleGroup);
            cardRadioButton.setToggleGroup(toggleGroup);
            invoiceRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> onToggleChange());
            cardRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> onToggleChange());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onToggleChange() {
        toggleGroup.getToggles().forEach(t -> {
            if(t.isSelected()) ((RadioButton) t).getParent().getStyleClass().add("selected");
            else ((RadioButton) t).getParent().getStyleClass().remove("selected");
        });
    }

    public static PaymentPane getInstance() {
        return ourInstance;
    }

}
