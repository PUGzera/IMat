package imat.state;

import com.google.gson.Gson;
import imat.entities.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

import static imat.util.FileSystem.initFileSystem;

public class ShoppingState implements Observable {

    private List<Observer> observers = new ArrayList<>();
    //State fields
    private List<ShoppingItem> products = new ArrayList<>();
    private BillingInformation billingInformation;
    private PaymentMethod paymentMethod;
    private State state = State.CHECKOUT;
    private ShippingInformation shippingInformation;
    private Order order;

    private OrderRepo orderRepo;
    private ProductRepo productRepo;

    public ShoppingState(OrderRepo orderRepo, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public Order getOrder() {
        return order;
    }

    public List<Observer> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    public List<ShoppingItem> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void addProducts(List<ShoppingItem> products) {
        this.products.stream()
                .filter(products::contains)
                .forEach(ShoppingItem::incAmount);
        products.stream()
                .filter(p -> !this.products.contains(p))
                .forEach(p -> this.products.add(p));
        notifyObservers();
    }

    public void addProduct(ShoppingItem shoppingItem) {
        products.remove(shoppingItem);
        if(shoppingItem.getAmount() > 0)
            products.add(shoppingItem);
        notifyObservers();
    }

    public void removeProducts(List<ShoppingItem> products) {
        products.stream()
                .filter(p -> this.products.contains(p))
                .forEach(p -> { if(this.products.get(this.products.indexOf(p)).getAmount() == 1) this.products.remove(p); });
        this.products.stream()
                .filter(products::contains)
                .forEach(ShoppingItem::decAmount);
        notifyObservers();
    }

    public void setProducts(List<ShoppingItem> products) {
        this.products = products;
    }

    public State getCurrentState() {
        return state;
    }

    public void nextState() {
        switch (state) {
            case CHECKOUT:
                if(products.size() > 0)
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
                if(shippingInformation != null && shippingInformation.isValid())
                    state = State.CONFIRMATION;
                break;
            case CONFIRMATION:
                Order order = generateOrder();
                this.order = order;
                orderRepo.save(order);
                state = State.DONE;
                break;
        }
        notifyObservers();
    }

    public void clear(){
        products.clear();
        billingInformation = null;
        paymentMethod = null;
        shippingInformation = null;
        order = null;
        state = State.CHECKOUT;
        notifyObservers();
    }

    private Order generateOrder() {
        Map<Product, Integer> map = new HashMap<>();
        products.forEach(p -> map.put(p.getProduct(), p.getAmount()));
        return new Order(shippingInformation.getDate(), billingInformation.getFirstName(), billingInformation.getLastName()
                , billingInformation.getAddress(), products.stream().mapToDouble(s -> s.getAmount() * s.getProduct().getPrice()).sum()
                , map);
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
        if(paymentMethod instanceof Invoice) {
            ((Invoice) paymentMethod).setSsn(billingInformation.getSsn());
            ((Invoice) paymentMethod).setName(billingInformation.getFirstName() + " " + billingInformation.getLastName());
            this.paymentMethod = paymentMethod;
        }
        else {
            ((CreditCard) paymentMethod).setHoldersName(billingInformation.getFirstName() + " " + billingInformation.getLastName());
            this.paymentMethod = paymentMethod;
        }
        notifyObservers();
    }

    public ShippingInformation getShippingInformation() {
        return new ShippingInformation(shippingInformation.getDate(), shippingInformation.getDeliveryMethod());
    }

    public void setShippingInformation(ShippingInformation shippingInformation) {
        this.shippingInformation = shippingInformation;
    }

    public OrderRepo getOrderRepo() {
        return orderRepo;
    }

    public ProductRepo getProductRepo() {
        return productRepo;
    }

    public enum State {
        CHECKOUT, BILLING_INFORMATION, PAYMENT_METHOD, SHIPPING_METHOD, CONFIRMATION, DONE
    }

    public void cacheProducts() {
        try {
            URI uri = getClass().getClassLoader().getResource("cache/shopping-items.json").toURI();
            FileSystem zipfs = initFileSystem(uri);
            Files.write(Paths.get(uri)
                    , Collections.singleton(new Gson().toJson(getProducts())), StandardOpenOption.APPEND);
            zipfs.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void cachePaymentInfo() {
        if (paymentMethod instanceof CreditCard && !getCachedPaymentInfo().contains(paymentMethod)) {
            try {
                URI uri = getClass().getClassLoader().getResource("cache/payment-info.json").toURI();
                FileSystem zipfs = initFileSystem(uri);
                Files.write(Paths.get(uri)
                        , Collections.singleton(new Gson().toJson(getPaymentMethod())), StandardOpenOption.APPEND);
                zipfs.close();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void cacheBillingInfo() {
        try {
            URI uri = getClass().getClassLoader().getResource("cache/billing-info.json").toURI();
            FileSystem zipfs = initFileSystem(uri);
            Files.write(Paths.get(uri)
                    , Collections.singleton(new Gson().toJson(getBillingInformation())));
            zipfs.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void cachePaymentInfo(PaymentMethod paymentMethod) {
        if (paymentMethod instanceof CreditCard && !getCachedPaymentInfo().contains(paymentMethod)) {
            try {
                URI uri = getClass().getClassLoader().getResource("cache/payment-info.json").toURI();
                FileSystem zipfs = initFileSystem(uri);
                Files.write(Paths.get(uri)
                        , Collections.singleton(new Gson().toJson(paymentMethod)), StandardOpenOption.APPEND);
                zipfs.close();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void cacheBillingInfo(BillingInformation billingInformation) {
        if(billingInformation.isValid()) {
            try {
                URI uri = getClass().getClassLoader().getResource("cache/billing-info.json").toURI();
                FileSystem zipfs = initFileSystem(uri);
                Files.write(Paths.get(uri)
                        , Collections.singleton(new Gson().toJson(billingInformation)));
                zipfs.close();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public List<List<ShoppingItem>> getCachedProducts() {
        try {
            URI uri = getClass().getClassLoader().getResource("cache/shopping-items.json").toURI();
            FileSystem zipfs = initFileSystem(uri);
            List<List<ShoppingItem>> lists = Files.readAllLines(Paths.get(uri))
                    .stream()
                    .map(p -> new Gson().fromJson(p, ShoppingItem[].class))
                    .map(Arrays::asList)
                    .collect(Collectors.toList());
            zipfs.close();
            return lists;
        } catch (IOException | URISyntaxException e) {
            return Collections.emptyList();
        }
    }

    public BillingInformation getCachedBillingInfo() {
        try {
            URI uri = getClass().getClassLoader().getResource("cache/billing-info.json").toURI();
            FileSystem zipfs = initFileSystem(uri);
            List<String> billingInfo = Files.readAllLines(Paths.get(uri));
            zipfs.close();
            if(billingInfo.size() > 0) {
                return new Gson().fromJson(
                        billingInfo.get(0)
                        , BillingInformation.class
                );
            }
            else return new BillingInformation();
        } catch (IOException | URISyntaxException e) {
            return new BillingInformation();
        }
    }

    public List<CreditCard> getCachedPaymentInfo() {
        try {
            URI uri = getClass().getClassLoader().getResource("cache/payment-info.json").toURI();
            FileSystem zipfs = initFileSystem(uri);
            List<CreditCard> creditCards = Files
                    .readAllLines(Paths.get(uri)).stream()
                    .map(c -> new Gson().fromJson(c, CreditCard.class))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            zipfs.close();
            return creditCards;
        } catch (IOException | URISyntaxException e) {
            return Collections.emptyList();
        }
    }

}
