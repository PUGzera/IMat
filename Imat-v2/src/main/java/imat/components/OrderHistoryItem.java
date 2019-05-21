package imat.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class OrderHistoryItem extends AnchorPane {

    @FXML Label productNameLabel;
    @FXML Label unitPriceLabel;
    @FXML Label amountLabel;
    @FXML Label totalSumLabel;


    public OrderHistoryItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/order_history_item.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProductNameLabel(String productNameLabel) {
        this.productNameLabel.setText(productNameLabel);
    }

    public void setUnitPriceLabel(String unitPriceLabel) {
        this.unitPriceLabel.setText(unitPriceLabel);
    }

    public void setAmountLabel(String amountLabel) {
        this.amountLabel.setText(amountLabel);
    }

    public void setTotalSumLabel(String totalSumLabel) {
        this.totalSumLabel.setText(totalSumLabel);
    }

    public Label getProductNameLabel() {
        return productNameLabel;
    }
}
