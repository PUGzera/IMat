package imat.components;

import imat.entities.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class ConfirmationPane extends AnchorPane {

    @FXML
    private TextField emailTextField, phoneTextField, ssnTextField, fnTextField, lnTextField, addressTextField, zipTextField;

    @FXML
    private Label paymentMethodLabel;

    @FXML
    private TextFlow paymentTextFlow;

    private static ConfirmationPane ourInstance = null;

    private FXMLLoader root;

    private ConfirmationPane() {
        root = new FXMLLoader(getClass().getResource("/fxml/confirmation.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfirmationPane getInstance() {
        if(ourInstance == null) ourInstance = new ConfirmationPane();
        return ourInstance;
    }

    public void setBillingFields(BillingInformation billingInformation) {
        emailTextField.setText(billingInformation.getEmail());
        phoneTextField.setText(billingInformation.getPhone());
        ssnTextField.setText(billingInformation.getSsn());
        fnTextField.setText(billingInformation.getFirstName());
        lnTextField.setText(billingInformation.getLastName());
        addressTextField.setText(billingInformation.getAddress());
        zipTextField.setText(billingInformation.getZip());
    }

    public void setPaymentFields(PaymentMethod paymentMethod, ShippingInformation shippingInformation) {
        paymentTextFlow.getChildren().clear();
        if (paymentMethod instanceof CreditCard) {
            paymentMethodLabel.setText("Kort Betalning");
            CreditCard creditCard = (CreditCard) paymentMethod;
            Text text = new Text("Kortnummer: " + creditCard.getCardNumber() + "\n" +
                    "CVC: " + creditCard.getVerificationCode() + "\n" +
                    "Leverantör: " + creditCard.getCardType() + "\n" +
                    "Kort innehavare: " + creditCard.getHoldersName() + "\n" +
                    "Leverans Datum: " + shippingInformation.getDate() + "\n" +
                    "Leveransmetod: "+ shippingInformation.getDeliveryMethod());
            text.setFont(Font.font(14));
            paymentTextFlow.getChildren().add(text);
        } else if (paymentMethod instanceof Invoice) {
            paymentMethodLabel.setText("Faktura");
            Invoice invoice = (Invoice) paymentMethod;
            Text text = new Text("Personnummer: " + invoice.getSsn() + "\n" +
                    "Betalnings Datum: " + invoice.getDate() + "\n" +
                    "Kund Namn: " + invoice.getName() + "\n" +
                    "Leverans Datum: " + shippingInformation.getDate() + "\n" +
                    "Leveransmetod: " + shippingInformation.getDeliveryMethod());
            text.setFont(Font.font(14));
            paymentTextFlow.getChildren().add(text);
        }
    }

}
