package imat.components;

import imat.entities.BillingInformation;
import imat.entities.CreditCard;
import imat.state.ShoppingState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class CustomerContactInfoHandler extends AnchorPane {

    @FXML TextField pNumField;
    @FXML TextField fNameField;
    @FXML TextField sNameField;
    @FXML TextField adressField;
    @FXML TextField zipField;
    @FXML TextField zipState;
    @FXML TextField mailField;
    @FXML TextField mailField2;
    @FXML TextField phoneField;
    @FXML Button saveCustomerDataBtn;
    @FXML RadioButton homeDeliveryBtn;
    @FXML RadioButton storeDeliveryButton;

    @FXML TextField firstCardNameField;
    @FXML TextField firstCardNumberField;
    @FXML TextField firstMMField;
    @FXML TextField firstYYField;
    @FXML TextField firstCVVField;

    @FXML ChoiceBox<CreditCard> carcChoiceBox;
    @FXML TextField secondCardNameField;
    @FXML TextField secondCardNumberField;
    @FXML TextField secondMMField;
    @FXML TextField secondYYField;
    @FXML TextField secondCVVField;

    private BillingInformation billingInformation;

    private ShoppingState shoppingState;

    public CustomerContactInfoHandler(ShoppingState shoppingState) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/customer_contact_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.shoppingState = shoppingState;
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        billingInformation = shoppingState.getCachedBillingInfo();
        fillContactFields();
        fillCCField();
        fillChoiceBox();
    }

    @FXML
    public void saveCustomerData(){
        BillingInformation billingInformation = new BillingInformation(
                mailField.getText(), phoneField.getText(),
                pNumField.getText(), zipField.getText(), fNameField.getText(),
                sNameField.getText(), adressField.getText());
        shoppingState.cacheBillingInfo(billingInformation);
        this.billingInformation = billingInformation;
        fillContactFields();
    }

    public void fillContactFields(){
        if(billingInformation!=null){
            pNumField.setText(billingInformation.getSsn());
            fNameField.setText(billingInformation.getFirstName());
            sNameField.setText(billingInformation.getLastName());
            adressField.setText(billingInformation.getAddress());
            zipField.setText(billingInformation.getZip());
            //zipState.setText(billingInformation.getState());
            mailField.setText(billingInformation.getEmail());
            mailField2.setText(billingInformation.getEmail());
            phoneField.setText(billingInformation.getPhone());
            /*if(billingInformation.isHomeDelivery()){
                homeDeliveryBtn.selectedProperty().setValue(true);
            }
            else{
                storeDeliveryButton.selectedProperty().setValue(true);
            }*/
        }
    }

    public void fillCCField(){
        List<CreditCard> creditCards = shoppingState.getCachedPaymentInfo();
        if(creditCards.size() > 0) {
            CreditCard primaryCard = creditCards.get(0);
            secondCardNameField.setText(primaryCard.getHoldersName());
            secondCardNumberField.setText(primaryCard.getCardNumber());
            secondMMField.setText(String.valueOf(primaryCard.getValidMonth()));
            secondYYField.setText(String.valueOf(primaryCard.getValidYear()));
            secondCVVField.setText(String.valueOf(primaryCard.getVerificationCode()));
        }
    }

    @FXML
    public void saveCardData(){
        System.out.println("Trying to Save CC");
        CreditCard.CardType type = firstCardNumberField.getText().charAt(0)==5? CreditCard.CardType.MASTER_CARD : CreditCard.CardType.VISA;
        shoppingState.cachePaymentInfo(new CreditCard(
                type, firstCardNameField.getText(),
                Integer.valueOf(firstMMField.getText()), Integer.valueOf(firstYYField.getText()),
                firstCardNumberField.getText(), Integer.valueOf(firstCVVField.getText())));
        fillCCField();
        fillChoiceBox();
        System.out.println("Saved CreditCardInfo");
    }

    public void fillChoiceBox(){
        carcChoiceBox.getItems().clear();
        carcChoiceBox.getItems().addAll(shoppingState.getCachedPaymentInfo());
        carcChoiceBox.getSelectionModel().selectFirst();
        carcChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            secondCardNameField.setText(newValue.getHoldersName());
            secondCardNumberField.setText(newValue.getCardNumber());
            secondCVVField.setText(String.valueOf(newValue.getVerificationCode()));
            secondMMField.setText(String.valueOf(newValue.getValidMonth()));
            secondYYField.setText(String.valueOf(newValue.getValidYear()));
        });
    }

    public boolean numberVerification(){
        if(firstCardNumberField.getText().charAt(0)!=4 || firstCardNumberField.getText().charAt(0)!=5){
            // Här kan man visa en text för att de är felaktigt Input
            return false;
        }
        return true;
    }


}
