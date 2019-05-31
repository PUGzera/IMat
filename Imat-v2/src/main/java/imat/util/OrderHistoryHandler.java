package imat.util;


import imat.components.CustomerDataHandler;
import imat.components.OrderHistory;
import imat.components.OrderHistoryItem;
import imat.entities.Order;
import imat.state.ShoppingState;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderHistoryHandler {

    private List<Order> orders;

    public OrderHistoryHandler(List<Order> orders) {
        this.orders = orders;
    }

    public List<OrderHistory> getOrderHistories(){
        List<OrderHistory> orderHistories = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.getOrderHistoryTitledPane().setText(createTitle(i));
            orderHistory.getOrderHistoryPaneToAddTo().getChildren().addAll(createOrderHistoryItemList(i));
            orderHistories.add(orderHistory);
        }
        return orderHistories;
    }

    public String createTitle(int index){
        StringBuilder sb = new StringBuilder();
        sb.append(orders.get(index).getLocalDate());
        sb.append(emptySpace());

        sb.append(orders.get(index).getId());
        sb.append(emptySpace());

        sb.append("                            ");

        sb.append(getAmountOfProducts(index));
        sb.append(emptySpace());
        sb.append("               ");

        sb.append(getTotalForShoppingList(index)).append("kr");
        return sb.toString();
    }

    public String emptySpace(){
        return "                                        ";
    }

    public Double getTotalForShoppingList(int index){
        return orders.get(index)
                .getMap().entrySet().stream().mapToDouble(e -> e.getValue()*e.getKey().getPrice()).sum();
    }

    public List<OrderHistoryItem> createOrderHistoryItemList(int index){
        Order order = orders.get(index);
        List<OrderHistoryItem> orderItems = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.00");

        OrderHistoryItem detailLabel = new OrderHistoryItem();
        detailLabel.getProductNameLabel().setStyle("-fx-font-weight: bold");
        detailLabel.setProductNameLabel("Produkt");
        detailLabel.setUnitPriceLabel("Enhetspris");
        detailLabel.setAmountLabel("Antal produkter");
        detailLabel.setTotalSumLabel("Kostnad");
        orderItems.add(detailLabel);

        order.getMap().forEach((k,v) -> {
            String price = df.format(v * k.getPrice());
            OrderHistoryItem orderHistoryItem = new OrderHistoryItem();
            orderHistoryItem.setProductNameLabel(k.getName());
            orderHistoryItem.setUnitPriceLabel(k.getPrice() + k.getUnit());
            orderHistoryItem.setAmountLabel("          " + v);
            orderHistoryItem.setTotalSumLabel(price + "kr");
            orderItems.add(orderHistoryItem);
        });
        return orderItems;
    }

    public int getAmountOfProducts(int index){
        return orders.get(index).getMap().size();
    }

}