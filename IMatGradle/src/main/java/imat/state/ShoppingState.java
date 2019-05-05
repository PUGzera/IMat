package imat.state;

import imat.entities.Product;
import imat.user.User;
import imat.user.UserRepo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.ShoppingItem;
import imat.util.BillingInformation;
import imat.util.PaymentMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static imat.user.User.PASSWORD_ENCODER;

public class ShoppingState implements Observable {

    private UserRepo userRepo;

    private List<Observer> observers = new ArrayList<>();
    //State fields
    private List<Product> products = new ArrayList<>();
    private Customer customer;
    private BillingInformation billingInformation;
    private PaymentMethod paymentMethod;
    private State state = State.CHECKOUT;

    public ShoppingState(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

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

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void addProducts(List<Product> products) {
        this.products.addAll(products);
        notifyObservers();
    }

    public Customer getCustomer() {
        return customer;
    }

    public State getCurrentState() {
        return state;
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
        notifyObservers();
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        notifyObservers();
    }

    public enum State {
        CHECKOUT, BILLING_INFORMATION, PAYMENT_METHOD, SHIPPING_METHOD, CONFIRMATION
    }

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    public void authenticate(String username, String password) throws UsernameNotFoundException  {
        User user = userRepo.findByEmail(username);
        if(user == null) throw new UsernameNotFoundException("User not found");
        if(!PASSWORD_ENCODER.matches(password, user.getPassword())) throw new UsernameNotFoundException("User not found");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole())));
        notifyObservers();
    }

    public void unAuthenticate() {
        SecurityContextHolder.clearContext();
        notifyObservers();
    }

    public Optional<User> getAuthenticatedUser() {
        System.out.println(isAuthenticated());
        if (isAuthenticated()) return Optional.of(userRepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        else return Optional.empty();
    }
}
