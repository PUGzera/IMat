package imat.components;

import imat.entities.Product;
import imat.entities.ShoppingItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;

public class ProductViewItem extends AnchorPane {

    @FXML private Label productName;
    @FXML private ImageView productImageListView;
    @FXML private Label price;

    @FXML private Button plusButtonListView;
    @FXML private Button minusButtonListView;
    @FXML private TextField quantityFieldListView;

    private GridView gridView;

    private Product product;

    public ProductViewItem(Product product, int amount, GridView gridView){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/product_view_item.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.product = product;
        this.gridView = gridView;
        try {
            fxmlLoader.load();
            productName.setText(product.getName());
            productImageListView.setImage(new Image(getClass().getResource("/images/product_"+product.getProductId()+".jpg").toString()));
            price.setText(product.getPrice() +product.getUnit());
            plusButtonListView.onActionProperty().setValue(e -> {
                if(Integer.valueOf(quantityFieldListView.getText()) < 21) {
                    gridView.addProduct(this);
                    quantityFieldListView.setText(String.valueOf(Integer.valueOf(quantityFieldListView.getText()) + 1));
                }
            });
            minusButtonListView.onActionProperty().setValue(e -> {
                if(Integer.valueOf(quantityFieldListView.getText()) > 0) {
                    gridView.removeProduct(this);
                    quantityFieldListView.setText(String.valueOf(Integer.valueOf(quantityFieldListView.getText()) - 1));
                }
            });
            quantityFieldListView.setText(String.valueOf(amount));
            quantityFieldListView.textProperty().addListener((o, ov, nv) -> {
                if (!nv.matches("\\d*")
                || Integer.valueOf(nv) < 0
                || Integer.valueOf(nv) > 21)
                    quantityFieldListView.setText(nv.replaceAll(nv, "1"));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Product getProduct() {
        return product;
    }
}
