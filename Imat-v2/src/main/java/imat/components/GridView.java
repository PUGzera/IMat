package imat.components;

import imat.entities.Product;
import imat.entities.ShoppingItem;
import imat.state.Observer;
import imat.state.ShoppingState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class GridView extends AnchorPane implements Observer {

    @FXML GridPane gridPane;

    private ShoppingState shoppingState;

    public GridView(ShoppingState shoppingState) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/grid_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.shoppingState = shoppingState;
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search(String key){
        gridPane.getChildren().clear();
        StreamSupport.stream(shoppingState.getProductRepo().findByNameLike(key).spliterator(), false)
                .map(p -> new ProductViewItem(p, this))
                .forEach(p -> gridPane.add(p, gridPane.getChildren().size() % 4, gridPane.getChildren().size()/4));

        /*int col = 0;
        int row = 0;
        for(int i = 0; i < items.size(); i++) {
            gridPane.add(items.get(i), col, row);
            col++;
            if(i%3 == 0 && i != 0) {
                row++;
                col = 0;
            }
        }*/
    }

    public void addProduct(ProductViewItem productViewItem){
        shoppingState.addProducts(Collections.singletonList(new ShoppingItem(productViewItem.getProduct(), 1)));
    }

    public void removeProduct(ProductViewItem productViewItem){
        shoppingState.removeProducts(Collections.singletonList(new ShoppingItem(productViewItem.getProduct(), 1)));
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
        //vad som nu ska göras när staten uppdateras
    }
}

