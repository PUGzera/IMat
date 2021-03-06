package imat.entities;


public class ShoppingItem {
    private Product product;
    private int amount;

    public ShoppingItem(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void incAmount() {
        amount++;
    }

    public void decAmount () {
        if(amount > 1) amount--;
    }

}
