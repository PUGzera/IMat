package imat.components;

import imat.entities.ShoppingItem;
import imat.state.Observer;
import imat.state.ShoppingState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class SavedList extends AnchorPane {

    private ShoppingState shoppingState;
    @FXML private TitledPane titledPane;

    public SavedList(ShoppingState shoppingState) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/saved_list.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.shoppingState = shoppingState;
        fillTitledPane();
    }

    public void fillTitledPane(){
        titledPane.setText("Test")  ;
    }

}
