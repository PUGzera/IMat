package imat.components;

import imat.entities.Product;
import imat.entities.ShoppingItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutPane extends AnchorPane {

    @FXML
    private VBox productVBox;

    private List<ShoppingItem> products = new ArrayList<>();

    private static CheckoutPane ourInstance = new CheckoutPane();

    private FXMLLoader root;

    private CheckoutPane() {
        root = new FXMLLoader(getClass().getResource("/fxml/checkout.fxml"));
        root.setRoot(this);
        root.setController(this);
        try {
            root.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CheckoutPane getInstance() {
        return ourInstance;
    }

    public void setProducts(List<ShoppingItem> products) {
        this.products = new ArrayList<>(products);
        productVBox.getChildren().clear();
        for (ShoppingItem product : products) {
            ProductItem productItem = new ProductItem(product, this);
            productVBox.getChildren().add(productItem);
        }
    }

    public void removeProductItem(ProductItem productItem) {
        products.remove(productItem.getShoppingItem());
        productVBox.getChildren().remove(productItem);
    }

    public List<ShoppingItem> getProducts() {
        return products;
    }
}
