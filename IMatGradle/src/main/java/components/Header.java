package components;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Header extends AnchorPane {

    private FXMLLoader root;

    public Header() throws IOException {
        root = new FXMLLoader(getClass().getResource("/fxml/header.fxml"));
        root.setRoot(this);
        root.load();
    }
}
