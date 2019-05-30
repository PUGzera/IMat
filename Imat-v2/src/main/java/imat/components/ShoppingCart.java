package imat.components;

import imat.entities.ShoppingItem;
import imat.state.Observer;
import imat.state.ShoppingState;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart extends ListView<CartItem> implements Observer {

    private static ShoppingCart ourInstance = null;

    private FXMLLoader root;

    private ShoppingState shoppingState;


    private ShoppingCart(ShoppingState shoppingState) {
        this.shoppingState = shoppingState;
        subscribe();
        root = new FXMLLoader(getClass().getResource("/fxml/shopping-cart.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ShoppingCart getInstance(ShoppingState shoppingState) {
        if(ourInstance == null) ourInstance = new ShoppingCart(shoppingState);
        return ourInstance;
    }

    @Override
    public void subscribe() {
        shoppingState.addObserver(this);
    }

    @Override
    public void unsubscribe() {
        shoppingState.removeObserver(this);
    }

    @Override
    public void update() {
        this.getItems().clear();
        List<ShoppingItem> products = new ArrayList<>(shoppingState.getProducts());
        Collections.reverse(products);
        products.forEach(p -> this.getItems().add(new CartItem(p, this)));
    }

    public void deleteCartItem(CartItem cartItem) {
        shoppingState.removeProducts(Collections.singletonList(cartItem.getShoppingItem()));
    }
}
