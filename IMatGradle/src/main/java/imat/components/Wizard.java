package imat.components;

import imat.entities.BillingInformation;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import imat.state.Observer;
import imat.state.ShoppingState;

import java.io.IOException;
import java.util.Random;

public class Wizard extends AnchorPane implements Observer {

    private static Wizard instance = null;

    @FXML
    private CheckBox saveProductsCheckBox;

    @FXML
    private Label taskOneLabel, taskTwoLabel, taskThreeLabel, taskFourLabel, taskFiveLabel;

    @FXML
    private Circle progressOneCircle, progressTwoCircle, progressThreeCircle, progressFourCircle, progressFiveCircle;

    @FXML
    private Line progressOneLine, progressTwoLine, progressThreeLine, progressFourLine;

    @FXML
    private Button nextButton, prevButton;

    @FXML
    private ProgressIndicator checkoutProgressIndicator;

    private ShoppingState observable;

    private BillingPane billingPane;

    private CheckoutPane checkoutPane;

    private PaymentPane paymentPane;

    private ShippingPane shippingPane;

    private ConfirmationPane confirmationPane;

    private FXMLLoader root;

    private Wizard(ShoppingState observable) {
        this.observable = observable;
        subscribe();
        root = new FXMLLoader(getClass().getResource("/fxml/wizard.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            checkoutPane = CheckoutPane.getInstance();
            paymentPane = PaymentPane.getInstance(observable.getCachedPaymentInfo());
            billingPane = BillingPane.getInstance(observable.getCachedBillingInfo());
            shippingPane = ShippingPane.getInstance();
            confirmationPane = ConfirmationPane.getInstance();
            progressOneCircle.setFill(Color.LIGHTGREEN);
            nextButton.onActionProperty().setValue(e -> nextTask());
            prevButton.onActionProperty().setValue(e -> prevTask());
            checkoutPane.setLayoutY(75);
            billingPane.setLayoutY(75);
            paymentPane.setLayoutY(75);
            shippingPane.setLayoutY(75);
            confirmationPane.setLayoutY(75);
            this.getChildren().add(checkoutPane);
            checkoutPane.setProducts(this.observable.getProducts());

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
                if(saveProductsCheckBox.isSelected()) observable.cacheProducts();
                observable.setProducts(checkoutPane.getProducts());
                observable.nextState();
                break;
            case BILLING_INFORMATION:
                billingPane.extract().ifPresentOrElse(b -> observable.setBillingInformation(b)
                        , () -> observable.setBillingInformation(new BillingInformation()));
                observable.cacheBillingInfo();
                observable.nextState();
                break;
            case PAYMENT_METHOD:
                paymentPane.extract().ifPresent(p -> observable.setPaymentMethod(p));
                observable.cachePaymentInfo();
                observable.nextState();
                break;
            case SHIPPING_METHOD:
                shippingPane.extract().ifPresent(s -> observable.setShippingInformation(s));
                observable.nextState();
                break;
            case CONFIRMATION:
                processPurchase();
                break;
        }

    }

    private void processPurchase(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(5000);
                return null;
            }
        };
        task.setOnRunning(e -> {
            this.setDisable(true);
            checkoutProgressIndicator.requestFocus();
            checkoutProgressIndicator.setVisible(true);
            checkoutProgressIndicator.progressProperty().bind(task.progressProperty());
        });
        task.setOnSucceeded(e -> observable.nextState());
        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void prevTask() {
        observable.previousState();
    }

    @FXML
    private void checkTask(){
        switch (observable.getCurrentState()) {
            case CHECKOUT:
                progressOneCircle.setFill(Color.LIGHTGREEN);
                saveProductsCheckBox.setVisible(true);
                this.getChildren().removeAll(checkoutPane, billingPane, paymentPane, shippingPane);
                this.getChildren().add(checkoutPane);
                break;
            case BILLING_INFORMATION:
                progressTwoCircle.setFill(Color.LIGHTGREEN);
                progressOneLine.setStroke(Color.LIGHTGREEN);
                saveProductsCheckBox.setVisible(false);
                this.getChildren().removeAll(checkoutPane, billingPane, paymentPane, shippingPane, confirmationPane);
                this.getChildren().add(billingPane);
                break;
            case PAYMENT_METHOD:
                progressThreeCircle.setFill(Color.LIGHTGREEN);
                progressTwoLine.setStroke(Color.LIGHTGREEN);
                this.getChildren().removeAll(checkoutPane, billingPane, paymentPane, shippingPane, confirmationPane);
                this.getChildren().add(paymentPane);
                break;
            case SHIPPING_METHOD:
                progressFourCircle.setFill(Color.LIGHTGREEN);
                progressThreeLine.setStroke(Color.LIGHTGREEN);
                this.getChildren().removeAll(checkoutPane, billingPane, paymentPane, shippingPane, confirmationPane);
                this.getChildren().add(shippingPane);
                break;
            case CONFIRMATION:
                progressFiveCircle.setFill(Color.LIGHTGREEN);
                progressFourLine.setStroke(Color.LIGHTGREEN);
                this.getChildren().removeAll(checkoutPane, billingPane, paymentPane, shippingPane, confirmationPane);
                confirmationPane.setBillingFields(observable.getBillingInformation());
                confirmationPane.setPaymentFields(observable.getPaymentMethod(), observable.getShippingInformation());
                this.getChildren().add(confirmationPane);
                break;
            case DONE:
                this.getChildren().clear();
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
    public void update() {
        checkTask();
        checkoutPane.setProducts(observable.getProducts());
    }

}
