package imat.components;

import imat.entities.ShippingInformation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static imat.validators.FormValidators.validFutureDate;

public class ShippingPane extends AnchorPane implements Extractable<ShippingInformation> {

    @FXML
    private ComboBox<ShippingInformation.DeliveryMethod> deliveryComboBox;

    @FXML
    private BorderPane calendarPane;

    private DatePicker deliveryDatePicker = new DatePicker(LocalDate.now());

    private DatePickerSkin datePickerSkin = new DatePickerSkin(deliveryDatePicker);

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
            datePickerSkin.getPopupContent().prefHeight(435);
            calendarPane.setTop(datePickerSkin.getPopupContent());
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
            datePickerSkin.getPopupContent().getStyleClass().add("invalid");
        }
        else {
            datePickerSkin.getPopupContent().getStyleClass().remove("invalid");
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
