package imat.components;

import imat.entities.Product;
import imat.entities.ShoppingItem;
import imat.state.Observer;
import imat.state.ShoppingState;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static imat.util.Translator.translate;


public class GridView extends AnchorPane implements Observer {

    private static GridView ourInstance = null;

    @FXML private GridPane gridPane;

    private ShoppingState shoppingState;

    private GridView(ShoppingState shoppingState) {
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

    public static GridView getInstance(ShoppingState shoppingState) {
        if(ourInstance == null) ourInstance = new GridView(shoppingState);
        return ourInstance;
    }

    public void search(String key){
        List<Product> products = ((List<Product>)shoppingState.getProductRepo().findByNameContainingIgnoreCase(key));
        products.addAll((List<Product>)shoppingState.getProductRepo().findAllByCategoryIgnoreCase(translate(key)));
        generateProductViews(products, this);
    }

    public void searchByCategory(String category) {
        generateProductViews((List<Product>) shoppingState.getProductRepo().findAllByCategoryIgnoreCase(category), this);
    }

    private void generateProductViews(List<Product> products, GridView gridView){
        gridPane.getChildren().clear();
        Task<Void> generateProductViewsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                products.stream()
                        .map(p -> {
                            int i = shoppingState.getProducts().indexOf(new ShoppingItem(p, 1));
                            if(i > 0) {
                                ShoppingItem s = shoppingState.getProducts().get(i);
                                return new ProductViewItem(p, s.getAmount(), gridView);
                            }
                            else return new ProductViewItem(p, 0, gridView);
                        })
                        .forEach(p ->
                                Platform.runLater(() ->
                                        gridPane
                                                .add(p
                                                        , gridPane.getChildren().size() % 4
                                                        , gridPane.getChildren().size()/4))
                        );
                return null;
            }
        };
        generateProductViewsTask.setOnSucceeded(e -> {
            System.out.println();
            System.out.println();
            System.out.println();
        });
        new Thread(generateProductViewsTask).start();

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

