package imat.components;

import imat.entities.ShoppingItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CartItem extends AnchorPane {

    @FXML
    private Label productQuantityLabel, productPriceLabel, productNameLabel;

    @FXML
    private ImageView productImage, deleteImage;

    private ShoppingCart shoppingCart;

    private FXMLLoader root;

    private ShoppingItem shoppingItem;

    public CartItem(ShoppingItem shoppingItem, ShoppingCart shoppingCart) {
        root = new FXMLLoader(getClass().getResource("/fxml/cart-item.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            this.shoppingCart = shoppingCart;
            this.shoppingItem = shoppingItem;
            productNameLabel.setText(shoppingItem.getProduct().getName());
            productQuantityLabel.setText("Antal: " + shoppingItem.getAmount());
            productPriceLabel.setText("Pris: " + shoppingItem.getAmount() * shoppingItem.getProduct().getPrice()+"kr");
            productImage.setImage(new Image(getClass().getResource("/images/product_"+shoppingItem.getProduct().getProductId()+".jpg").toString()));
            deleteImage.setOnMouseClicked(e -> delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CartItem(ShoppingItem shoppingItem) {
        root = new FXMLLoader(getClass().getResource("/fxml/cart-item.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            this.shoppingItem = shoppingItem;
            productNameLabel.setText(shoppingItem.getProduct().getName());
            productQuantityLabel.setText("Antal: " + shoppingItem.getAmount());
            productPriceLabel.setText("Pris: " + shoppingItem.getAmount() * shoppingItem.getProduct().getPrice()+"kr");
            productImage.setImage(new Image(getClass().getResource("/images/product_"+shoppingItem.getProduct().getProductId()+".jpg").toString()));
            deleteImage.getParent().setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void delete() {
        shoppingCart.deleteCartItem(this);
    }

    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }
}
