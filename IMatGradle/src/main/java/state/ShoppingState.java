package state;

import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.ShoppingItem;
import util.BillingInformation;
import util.PaymentMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingState implements Observable {

    private List<Observer> observers = new ArrayList<>();
    //State fields
    private List<ShoppingItem> products = new ArrayList<>();
    private Customer customer;
    private boolean authenticated = false;
    private BillingInformation billingInformation;
    private PaymentMethod paymentMethod;
    private State state = State.CHECKOUT;


    @Override
    public void notifyObservers() {
        observers.forEach(o -> o.update(this));
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeSubscriber(Observer observer) {
        observers.remove(observer);
    }

    public List<Observer> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    public List<ShoppingItem> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void addProducts(List<ShoppingItem> products) {
        this.products.addAll(products);
        notifyObservers();
    }

    public Customer getCustomer() {
        return customer;
    }

    public State getCurrentState() {
        return state;
    }

    public void setCurrentState(State state) {
        this.state = state;
    }

    public void nextState() {
        switch (state) {
            case CHECKOUT:
                if(products.size() > -1)
                    state = State.BILLING_INFORMATION;
                break;
            case BILLING_INFORMATION:
            if(billingInformation != null && billingInformation.isValid())
                    state = State.PAYMENT_METHOD;
                break;
            case PAYMENT_METHOD:
                if(paymentMethod != null && paymentMethod.isValid())
                    state = State.SHIPPING_METHOD;
                break;
            case SHIPPING_METHOD:
                state = State.CHECKOUT;
                break;
            case CONFIRMATION:
                break;
        }
        notifyObservers();
    }

    public void previousState() {
        switch (state) {
            case BILLING_INFORMATION:
                state = State.CHECKOUT;
                break;
            case PAYMENT_METHOD:
                state = State.BILLING_INFORMATION;
                break;
            case SHIPPING_METHOD:
                state = State.PAYMENT_METHOD;
                break;
            case CONFIRMATION:
                state = State.SHIPPING_METHOD;
                break;
        }
        notifyObservers();
    }

    public BillingInformation getBillingInformation() {
        return new BillingInformation(billingInformation.getEmail(), billingInformation.getPhone(), billingInformation.getSsn(), billingInformation.getZip(),
                billingInformation.getFirstName(), billingInformation.getLastName(), billingInformation.getAddress());
    }

    public void setBillingInformation(BillingInformation billingInformation) {
        this.billingInformation = billingInformation;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public enum State {
        CHECKOUT, BILLING_INFORMATION, PAYMENT_METHOD, SHIPPING_METHOD, CONFIRMATION
    }
}
