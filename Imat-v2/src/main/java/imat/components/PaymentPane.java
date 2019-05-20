package imat.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import imat.entities.CreditCard;
import imat.entities.Invoice;
import imat.entities.PaymentMethod;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static imat.validators.FormValidators.*;

public class PaymentPane extends AnchorPane implements Extractable<PaymentMethod> {

    @FXML
    private RadioButton invoiceRadioButton, cardRadioButton;

    @FXML
    private TextField cardNumberTextField, cvcTextField;

    @FXML
    private ListView<PaymentItem> cardsListView;

    @FXML
    private ComboBox<CreditCard.CardType> cardDealerComboBox;

    @FXML
    private ComboBox<Integer> monthComboBox, yearComboBox;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private static PaymentPane ourInstance = null;

    private FXMLLoader root;

    private PaymentPane(List<CreditCard> cardList) {
        root = new FXMLLoader(getClass().getResource("/fxml/payment.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            cardDealerComboBox.getItems().addAll(CreditCard.CardType.MASTER_CARD, CreditCard.CardType.VISA);
            monthComboBox.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12);
            yearComboBox.getItems().addAll(LocalDate.now().getYear(), LocalDate.now().getYear() + 1, LocalDate.now().getYear() + 2
            , LocalDate.now().getYear() + 3, LocalDate.now().getYear() + 4, LocalDate.now().getYear() + 5);
            invoiceRadioButton.setToggleGroup(toggleGroup);
            cardRadioButton.setToggleGroup(toggleGroup);
            invoiceRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                cardsListView.setDisable(true);
                onToggleChange();
            });
            cardRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                cardsListView.setDisable(false);
                onToggleChange();
            });
            cardList.forEach(c -> cardsListView.getItems().add(new PaymentItem((CreditCard) c)));
            cardsListView.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
                CreditCard creditCard = nv.getCreditCard();
                cardNumberTextField.setText(creditCard.getCardNumber());
                cvcTextField.setText(String.valueOf(creditCard.getVerificationCode()));
                cardDealerComboBox.getSelectionModel().select(creditCard.getCardType());
                monthComboBox.getSelectionModel().select(new Integer(creditCard.getValidMonth()));
                yearComboBox.getSelectionModel().select(new Integer(creditCard.getValidYear()));
            });
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

    public static PaymentPane getInstance(List<CreditCard> creditCards) {
        if (ourInstance == null) ourInstance = new PaymentPane(creditCards);
        return ourInstance;
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
        if(monthComboBox.getValue() == null || yearComboBox.getValue() == null
            || !validFutureDate(LocalDate.of(yearComboBox.getValue(), monthComboBox.getValue(), 1))) {
            monthComboBox.getStyleClass().add("invalid");
            yearComboBox.getStyleClass().add("invalid");
        }
        else {
            monthComboBox.getStyleClass().remove("invalid");
            yearComboBox.getStyleClass().remove("invalid");
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

    private boolean validateCardForm() {
        validateCardNumberField();
        validateCVCField();
        validateCardDate();
        validateCardDealerBox();
        return monthComboBox.getValue() != null
                && yearComboBox.getValue() != null
                && validCVC(cvcTextField.getText())
                && validCardNumber(cardNumberTextField.getText().replace(" ", ""))
                && validFutureDate(LocalDate.of(yearComboBox.getValue(), monthComboBox.getValue(), 1))
                && cardDealerComboBox.getValue() != null;
    }

    @Override
    public Optional<PaymentMethod> extract() {
        System.out.println(validateCardForm());
        if(invoiceRadioButton.isSelected()) return Optional.of(new Invoice());
        else if (validateCardForm()) return Optional.of(new CreditCard(cardDealerComboBox.getValue(), "", monthComboBox.getValue(),
                yearComboBox.getValue(), cardNumberTextField.getText(), Integer.valueOf(cvcTextField.getText())));
        else return Optional.empty();
    }
}
