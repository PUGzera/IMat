package imat.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import imat.util.BillingInformation;

import java.io.IOException;

import static imat.validators.FormValidators.*;

public class BillingPane extends AnchorPane implements Extractable<BillingInformation> {

    @FXML
    private TextField emailTextField, phoneTextField, ssnTextField, fnTextField, lnTextField, addressTextField, zipTextField;

    private static BillingPane ourInstance = null;

    private FXMLLoader root;

    private BillingPane() {
        root = new FXMLLoader(getClass().getResource("/fxml/billing.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BillingPane(BillingInformation billingInformation) {
        root = new FXMLLoader(getClass().getResource("/fxml/billing.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            emailTextField.setText(billingInformation.getEmail());
            phoneTextField.setText(billingInformation.getPhone());
            ssnTextField.setText(billingInformation.getSsn());
            fnTextField.setText(billingInformation.getFirstName());
            lnTextField.setText(billingInformation.getLastName());
            addressTextField.setText(billingInformation.getAddress());
            zipTextField.setText(billingInformation.getZip());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateEmailTextField() {
        if(!validEmail(emailTextField.getText())) {
            emailTextField.getStyleClass().add("invalid");
        }
        else {
            emailTextField.getStyleClass().remove("invalid");
        }
    }

    private void validatePhoneTextField(){
        if(!validatePhoneNumber(phoneTextField.getText().replace("-", "").replace(" ", ""))) {
            phoneTextField.getStyleClass().add("invalid");
        }
        else {
            phoneTextField.getStyleClass().remove("invalid");
        }
    }

    private void validateSSNTextField() {
        if(!validSSN(ssnTextField.getText().replace("-", ""))) {
            ssnTextField.getStyleClass().add("invalid");
        }
        else {
            ssnTextField.getStyleClass().remove("invalid");
        }
    }

    private void validateZipTextField() {
        if(!validZipCode(zipTextField.getText().replace(" ", ""))) {
            zipTextField.getStyleClass().add("invalid");
        }
        else {
            zipTextField.getStyleClass().remove("invalid");
        }
    }

    private void validateNameTextField(TextField textField) {
        if(!validateNames(textField.getText())) {
            textField.getStyleClass().add("invalid");
        }
        else {
            textField.getStyleClass().remove("invalid");
        }
    }

    private boolean validateForm() {
        validateNameTextField(fnTextField);
        validateNameTextField(lnTextField);
        validateNameTextField(addressTextField);
        validateZipTextField();
        validatePhoneTextField();
        validateSSNTextField();
        validateEmailTextField();
        return validateNames(fnTextField.getText()) &&
                validateNames(lnTextField.getText()) &&
                validateNames(addressTextField.getText()) &&
                validZipCode(zipTextField.getText()) &&
                validSSN(ssnTextField.getText()) &&
                validatePhoneNumber(phoneTextField.getText()) &&
                validEmail(emailTextField.getText());
    }

    public static BillingPane getInstance() {
        if (ourInstance == null) ourInstance = new BillingPane();
        return ourInstance;
    }

    public static BillingPane getInstance(BillingInformation billingInformation) {
        if (ourInstance == null) ourInstance = new BillingPane(billingInformation);
        return ourInstance;
    }

    @Override
    public BillingInformation extract() {
        if(validateForm()) return new BillingInformation(emailTextField.getText(), phoneTextField.getText(), ssnTextField.getText(), zipTextField.getText(),
                fnTextField.getText(), lnTextField.getText(), addressTextField.getText());
        else return null;
    }
}
