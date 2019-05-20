package imat.components;

import imat.entities.Product;
import imat.entities.ShoppingItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ProductItem extends AnchorPane {

    @FXML
    private Label totalPriceLabel, priceLabel, productNameLabel;

    @FXML
    private Button addButton, removeButton, deleteButton;

    @FXML
    private TextField amountTextField;

    @FXML
    private ImageView productImage;

    private ShoppingItem shoppingItem;

    private FXMLLoader root;

    public ProductItem(ShoppingItem shoppingItem, CheckoutPane checkoutPane) {
        root = new FXMLLoader(getClass().getResource("/fxml/product-item.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
            this.shoppingItem = shoppingItem;
            productNameLabel.setText(shoppingItem.getProduct().getName());
            priceLabel.setText("Pris per st: " + shoppingItem.getProduct().getPrice());
            totalPriceLabel.setText("Totalt pris: " + shoppingItem.getProduct().getPrice() * shoppingItem.getAmount());
            amountTextField.setText(String.valueOf(shoppingItem.getAmount()));
            addButton.onActionProperty().setValue(e -> {
                shoppingItem.incAmount();
                totalPriceLabel.setText("Totalt pris: " + shoppingItem.getProduct().getPrice() * shoppingItem.getAmount());
                amountTextField.setText(String.valueOf(shoppingItem.getAmount()));
            });
            removeButton.onActionProperty().setValue(e -> {
                shoppingItem.decAmount();
                totalPriceLabel.setText("Totalt pris: " + shoppingItem.getProduct().getPrice() * shoppingItem.getAmount());
                amountTextField.setText(String.valueOf(shoppingItem.getAmount()));
            });
            deleteButton.onActionProperty().setValue(e -> checkoutPane.removeProductItem(this));
            productImage.setImage(new Image(getClass().getResource("/images/product_"+shoppingItem.getProduct().getProductId()+".jpg").toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }
}
