package components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import state.Observable;
import state.Observer;
import state.ShoppingState;

import java.io.IOException;

public class Wizard extends AnchorPane implements Observer {

    private static Wizard instance = null;

    @FXML
    private Label taskOneLabel, taskTwoLabel, taskThreeLabel, taskFourLabel, taskFiveLabel;

    @FXML
    private Circle progressOneCircle, progressTwoCircle, progressThreeCircle, progressFourCircle, progressFiveCircle;

    @FXML
    private Line progressOneLine, progressTwoLine, progressThreeLine, progressFourLine;

    @FXML
    private Button nextButton;

    private ShoppingState observable;

    private BillingPane billingPane = BillingPane.getInstance();

    private CheckoutPane checkoutPane = CheckoutPane.getInstance();

    private PaymentPane paymentPane = PaymentPane.getInstance();

    private FXMLLoader root;

    private Wizard(ShoppingState observable) {
        this.observable = observable;
        subscribe();
        root = new FXMLLoader(getClass().getResource("/fxml/wizard.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            progressOneCircle.setFill(Color.LIGHTGREEN);
            nextButton.onActionProperty().setValue(e -> nextTask());
            checkoutPane.setLayoutY(75);
            billingPane.setLayoutY(75);
            paymentPane.setLayoutY(75);
            this.getChildren().add(checkoutPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Wizard getInstance(ShoppingState observable) {
        if (instance == null) instance = new Wizard(observable);
        return instance;
    }

    @FXML
    private void nextTask(){
        switch (observable.getCurrentState()) {
            case CHECKOUT:
                observable.nextState();
                break;
            case BILLING_INFORMATION:
                this.getChildren().remove(billingPane);
                observable.setBillingInformation(billingPane.extract());
                observable.nextState();
                break;
            case PAYMENT_METHOD:
                this.getChildren().remove(paymentPane);
                observable.setPaymentMethod(paymentPane.extract());
                observable.nextState();
                break;
            case SHIPPING_METHOD:
                observable.nextState();
                break;
        }

    }

    @FXML
    private void checkTask(){
        switch (observable.getCurrentState()) {
            case CHECKOUT:
                progressOneCircle.setFill(Color.LIGHTGREEN);
                break;
            case BILLING_INFORMATION:
                progressTwoCircle.setFill(Color.LIGHTGREEN);
                progressOneLine.setStroke(Color.LIGHTGREEN);
                if(!this.getChildren().contains(billingPane))
                    this.getChildren().add(billingPane);
                break;
            case PAYMENT_METHOD:
                progressThreeCircle.setFill(Color.LIGHTGREEN);
                progressTwoLine.setStroke(Color.LIGHTGREEN);
                if(!this.getChildren().contains(paymentPane))
                    this.getChildren().add(paymentPane);
                break;
            case SHIPPING_METHOD:
                progressFourCircle.setFill(Color.LIGHTGREEN);
                progressThreeLine.setStroke(Color.LIGHTGREEN);
                break;
            case CONFIRMATION:
                progressFiveCircle.setFill(Color.LIGHTGREEN);
                progressFourLine.setStroke(Color.LIGHTGREEN);
                break;
        }
    }

    @Override
    public void subscribe() {
        observable.addObserver(this);
    }

    @Override
    public void unsubscribe() {
        observable.removeSubscriber(this);
    }

    @Override
    public void update(Observable observable) {
        checkTask();
    }

}
