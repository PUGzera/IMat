package imat.components;

import imat.entities.ShippingInformation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static imat.validators.FormValidators.validFutureDate;

public class ShippingPane extends AnchorPane implements Extractable<ShippingInformation> {

    @FXML
    private ComboBox<ShippingInformation.DeliveryMethod> deliveryComboBox;

    @FXML
    private DatePicker deliveryDatePicker;

    private static ShippingPane ourInstance = new ShippingPane();

    private FXMLLoader root;

    private ShippingPane() {
        root = new FXMLLoader(getClass().getResource("/fxml/shipping.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            deliveryComboBox.getItems().addAll(ShippingInformation.DeliveryMethod.HEM_LEVERANS
                    , ShippingInformation.DeliveryMethod.HÄMTA_I_BUTIK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateCardDealerBox() {
        if(deliveryComboBox.getValue() == null) {
            deliveryComboBox.getStyleClass().add("invalid");
        }
        else {
            deliveryComboBox.getStyleClass().remove("invalid");
        }
    }

    private void validateDate() {
        if(deliveryDatePicker.getValue() == null || !validFutureDate(deliveryDatePicker.getValue())) {
            deliveryDatePicker.getStyleClass().add("invalid-1");
        }
        else {
            deliveryDatePicker.getStyleClass().remove("invalid-1");
        }
    }

    private boolean validateForm() {
        validateDate();
        validateCardDealerBox();
        return deliveryDatePicker.getValue() != null && validFutureDate(deliveryDatePicker.getValue()) && deliveryComboBox.getValue() != null;
    }

    public static ShippingPane getInstance() {
        return ourInstance;
    }

    @Override
    public Optional<ShippingInformation> extract() {
        return validateForm() ?
            Optional.of(new ShippingInformation(deliveryDatePicker.getValue(), deliveryComboBox.getValue()))
                :
                Optional.empty();
    }
}
