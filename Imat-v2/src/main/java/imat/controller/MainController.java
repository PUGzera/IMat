package imat.controller;

import imat.components.CustomerDataHandler;
import imat.components.GridView;
import imat.components.ShoppingCart;
import imat.components.Wizard;
import imat.entities.OrderRepo;
import imat.entities.Product;
import imat.entities.ProductRepo;
import imat.entities.ShoppingItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import imat.state.ShoppingState;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static imat.util.Translator.getCategories;
import static imat.util.Translator.translate;

@Component
public class MainController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    private ShoppingState observable;

    @FXML
    private StackPane mainPane;

    @FXML
    private AnchorPane shoppingCartPane;

    @FXML
    private ScrollPane dynamicPane;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Text> categoryListView;

    private Wizard wizard;

    private CustomerDataHandler customerDataHandler;

    private GridView gridView;

    private ShoppingCart shoppingCart;

    private Page page = Page.HOME;

    List<String> categories = new ArrayList<>();

    @FXML
    private void initialize() throws IOException {
        observable = new ShoppingState(orderRepo, productRepo);
        customerDataHandler = CustomerDataHandler.getInstance(observable);
        wizard = Wizard.getInstance(observable, this);
        gridView = GridView.getInstance(observable);
        shoppingCart = ShoppingCart.getInstance(observable);
        shoppingCartPane.getChildren().add(shoppingCart);
        observable.addProducts(StreamSupport.stream(productRepo
                .findByNameContainingIgnoreCase("bröd").spliterator(), false)
                .map(p -> new ShoppingItem(p, 1))
                .collect(Collectors.toList()));
        initCategoryTitledPane();
        loadPage();
    }

    private void initCategoryTitledPane(){
        getCategories().stream()
                .map(c -> {
                    Text text = new Text(c);
                    text.setFont(Font.font(16));
                    return text;
                })
                .forEach(categoryListView.getItems()::add);
        categoryListView.getSelectionModel().selectedItemProperty()
                .addListener((o, ov, nv) -> {
                    gridView.searchByCategory(translate(nv.getText()).toUpperCase());
                    page = Page.PRODUCT_VIEW;
                    loadPage();
                });
    }

    private void loadPage() {
        shoppingCartPane.setVisible(true);
        switch (page) {
            case HOME:
                gridView.search("");
                dynamicPane.setContent(gridView);
                break;
            case WIZARD:
                shoppingCartPane.setVisible(false);
                dynamicPane.setContent(wizard);
                break;
            case MY_PAGE:
                dynamicPane.setContent(customerDataHandler);
                break;
            case PRODUCT_VIEW:
                dynamicPane.setContent(gridView);
                break;
        }
    }

    @FXML
    private void toCheckout() {
        page = Page.WIZARD;
        loadPage();
    }

    @FXML
    public void toHome() {
        page = Page.PRODUCT_VIEW;
        loadPage();
    }

    @FXML
    public void toMyPage(){
        page = Page.MY_PAGE;
        loadPage();
        /*if(!mainPane.getChildren().contains(customerDataHandler)){
            mainPane.getChildren().add(customerDataHandler);
        }
        else{
            int index = mainPane.getChildren().indexOf(customerDataHandler);
            mainPane.getChildren().get(index).toFront();
        }*/
    }

    @FXML
    private void toProductView() {
        gridView.search(searchField.getText());
        page = Page.PRODUCT_VIEW;
        loadPage();
    }

    public void resetWizard() {
        wizard = Wizard.getInstance(observable, this);
    }

    public enum Page {
        WIZARD, MY_PAGE, PRODUCT_VIEW, HOME
    }

}
