package components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import util.CreditCard;
import util.Invoice;
import util.PaymentMethod;

import javax.smartcardio.Card;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static validators.FormValidators.*;

public class PaymentPane extends AnchorPane implements Extractable<PaymentMethod> {

    @FXML
    private RadioButton invoiceRadioButton, cardRadioButton;

    @FXML
    private TextField ssnTextField, cardNumberTextField, cvcTextField;

    @FXML
    private DatePicker validDatePicker;

    @FXML
    private ComboBox<CreditCard.CardType> cardDealerComboBox;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private static PaymentPane ourInstance = new PaymentPane();

    private FXMLLoader root;

    private PaymentPane() {
        root = new FXMLLoader(getClass().getResource("/fxml/payment.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            cardDealerComboBox.getItems().addAll(CreditCard.CardType.MASTER_CARD, CreditCard.CardType.VISA);
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

    private void validateSSNTextField() {
        if(!validSSN(ssnTextField.getText().replace("-", ""))) {
            ssnTextField.getStyleClass().add("invalid");
        }
        else {
            ssnTextField.getStyleClass().remove("invalid");
        }
    }

    private void validateCVCField() {
        if(!validCVC(cvcTextField.getText())) {
            cvcTextField.getStyleClass().add("invalid");
        }
        else {
            cvcTextField.getStyleClass().remove("invalid");
        }
    }

    private void validateCardNumberField() {
        if(!validCardNumber(cardNumberTextField.getText().replace(" ", ""))) {
            cardNumberTextField.getStyleClass().add("invalid");
        }
        else {
            cardNumberTextField.getStyleClass().remove("invalid");
        }
    }

    private void validateCardDate() {
        if(validDatePicker.getValue() != null && !validFutureDate(validDatePicker.getValue())) {
            validDatePicker.getStyleClass().add("invalid");
        }
        else {
            validDatePicker.getStyleClass().remove("invalid");
        }
    }

    private void validateCardDealerBox() {
        if(cardDealerComboBox.getValue() == null) {
            cardDealerComboBox.getStyleClass().add("invalid");
        }
        else {
            cardDealerComboBox.getStyleClass().remove("invalid");
        }
    }

    private boolean validateInvoiceForm() {
        validateSSNTextField();
        return validSSN(ssnTextField.getText());
    }

    private boolean validateCardForm() {
        validateCardNumberField();
        validateCVCField();
        validateCardDate();
        validateCardDealerBox();
        return validDatePicker.getValue() != null && validCVC(cvcTextField.getText()) && validCardNumber(cardNumberTextField.getText()) && validFutureDate(validDatePicker.getValue()) && cardDealerComboBox.getValue() != null;
    }

    @Override
    public PaymentMethod extract() {
        if(invoiceRadioButton.isSelected() && validateInvoiceForm()) return new Invoice(ssnTextField.getText());
        else if (validateCardForm()) return new CreditCard(cardDealerComboBox.getValue(), "test", validDatePicker.getValue().getMonthValue(),
                validDatePicker.getValue().getYear(), cardNumberTextField.getText(), Integer.valueOf(cvcTextField.getText()));
        else return null;
    }
}
