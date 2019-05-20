package imat.components;

import imat.controller.MainController;
import imat.entities.BillingInformation;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import imat.state.Observer;
import imat.state.ShoppingState;

import java.io.IOException;
import java.util.*;

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

    @FXML
    private AnchorPane dynamicPane;

    private Map<ShoppingState.State, Circle> progressCircles = new LinkedHashMap<>();

    private Map<ShoppingState.State, Line> progressLines = new LinkedHashMap<>();

    private ShoppingState observable;

    private BillingPane billingPane;

    private CheckoutPane checkoutPane;

    private PaymentPane paymentPane;

    private ShippingPane shippingPane;

    private ConfirmationPane confirmationPane;

    private ReceiptPane receiptPane;

    private FXMLLoader root;

    private MainController controller;

    private Wizard(ShoppingState observable, MainController controller) {
        this.observable = observable;
        this.controller = controller;
        subscribe();
        root = new FXMLLoader(getClass().getResource("/fxml/wizard.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            initPanes();
            initProgressMaps();
            progressOneCircle.setFill(Color.LIGHTGREEN);
            nextButton.onActionProperty().setValue(e -> nextTask());
            prevButton.onActionProperty().setValue(e -> prevTask());
            this.getChildren().add(checkoutPane);
            checkoutPane.setProducts(this.observable.getProducts());
            checkTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initProgressMaps() {
        progressCircles.put(ShoppingState.State.CHECKOUT, progressOneCircle);
        progressCircles.put(ShoppingState.State.BILLING_INFORMATION, progressTwoCircle);
        progressCircles.put(ShoppingState.State.PAYMENT_METHOD, progressThreeCircle);
        progressCircles.put(ShoppingState.State.SHIPPING_METHOD, progressFourCircle);
        progressCircles.put(ShoppingState.State.CONFIRMATION, progressFiveCircle);
        progressLines.put(ShoppingState.State.CHECKOUT, progressOneLine);
        progressLines.put(ShoppingState.State.BILLING_INFORMATION, progressOneLine);
        progressLines.put(ShoppingState.State.PAYMENT_METHOD, progressTwoLine);
        progressLines.put(ShoppingState.State.SHIPPING_METHOD, progressThreeLine);
        progressLines.put(ShoppingState.State.CONFIRMATION, progressFourLine);
    }

    private void initPanes() {
        checkoutPane = CheckoutPane.getInstance();
        paymentPane = PaymentPane.getInstance(observable.getCachedPaymentInfo());
        billingPane = BillingPane.getInstance(observable.getCachedBillingInfo());
        shippingPane = ShippingPane.getInstance();
        confirmationPane = ConfirmationPane.getInstance();
    }

    private void progressStyling(ShoppingState.State state) {
        progressLines.entrySet().stream().takeWhile(e -> e.getKey().compareTo(state) <= 0)
                .forEach(e -> e.getValue().setStroke(Color.LIGHTGREEN));
        progressCircles.entrySet().stream().takeWhile(e -> e.getKey().compareTo(state) <= 0)
                .forEach(e -> e.getValue().setFill(Color.LIGHTGREEN));
        progressLines.entrySet().stream().dropWhile(e -> e.getKey().compareTo(state) <= 0)
                .forEach(e -> e.getValue().setStroke(Color.BLACK));
        progressCircles.entrySet().stream().dropWhile(e -> e.getKey().compareTo(state) <= 0)
                .forEach(e -> e.getValue().setFill(Color.rgb(238, 238, 238)));
    }

    public static Wizard getInstance(ShoppingState observable, MainController controller) {
        if (instance == null) instance = new Wizard(observable, controller);
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
        task.setOnSucceeded(e -> {
            observable.nextState();
            this.setDisable(false);
        });
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
                System.out.println("checkout");
                prevButton.setVisible(false);
                saveProductsCheckBox.setVisible(true);
                onTaskChecked(checkoutPane);
                break;
            case BILLING_INFORMATION:
                prevButton.setVisible(true);
                saveProductsCheckBox.setVisible(false);
                onTaskChecked(billingPane);
                break;
            case PAYMENT_METHOD:
                prevButton.setVisible(true);
                onTaskChecked(paymentPane);
                break;
            case SHIPPING_METHOD:
                prevButton.setVisible(true);
                onTaskChecked(shippingPane);
                break;
            case CONFIRMATION:
                prevButton.setVisible(true);
                confirmationPane.setBillingFields(observable.getBillingInformation());
                confirmationPane.setPaymentFields(observable.getPaymentMethod(), observable.getShippingInformation());
                onTaskChecked(confirmationPane);
                break;
            case DONE:
                receiptPane = ReceiptPane.getInstance(observable.getOrder(), this);
                this.getChildren().clear();
                this.getChildren().add(receiptPane);
                break;
        }
    }

    public void resetWizard() {
        observable.clear();
        instance = new Wizard(observable, controller);
        controller.resetWizard();
        controller.goHome();
    }

    private void onTaskChecked(AnchorPane pane) {
        progressStyling(observable.getCurrentState());
        dynamicPane.getChildren().clear();
        this.getChildren().remove(receiptPane);
        dynamicPane.getChildren().add(pane);
    }

    @Override
    public void subscribe() {
        observable.addObserver(this);
    }

    @Override
    public void unsubscribe() {
        observable.removeObserver(this);
    }

    @Override
    public void update() {
        checkTask();
        checkoutPane.setProducts(observable.getProducts());
    }

}
