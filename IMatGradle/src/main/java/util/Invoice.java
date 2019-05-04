package util;

import se.chalmers.cse.dat216.project.Customer;

import java.util.Date;

public class Invoice implements PaymentMethod {
    private Date date;
    private Customer customer;

    public Invoice(Customer customer) {
        date = new Date(new Date().getTime()/1000 + 3600*24*30);
        this.customer = customer;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean isValid() {
        return customer != null && date != null;
    }
}
