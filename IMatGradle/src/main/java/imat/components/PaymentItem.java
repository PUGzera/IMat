package imat.components;

import imat.entities.CreditCard;
import imat.entities.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PaymentItem extends AnchorPane {
    @FXML
    private Label dealerLabel, numberLabel, cvcLabel, validLabel;

    private CreditCard creditCard;

    private FXMLLoader root;

    public PaymentItem(CreditCard creditCard) {
        root = new FXMLLoader(getClass().getResource("/fxml/payment-item.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            this.creditCard = creditCard;
            dealerLabel.setText(creditCard.getCardType().toString());
            numberLabel.setText(creditCard.getCardNumber());
            cvcLabel.setText(String.valueOf(creditCard.getVerificationCode()));
            validLabel.setText(creditCard.getValidMonth() + "/" + creditCard.getValidYear());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }
}
