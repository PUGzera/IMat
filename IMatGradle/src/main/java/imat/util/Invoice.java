package imat.util;

import java.util.Date;

public class Invoice implements PaymentMethod {
    private Date date;
    private String ssn;

    public Invoice(String ssn) {
        date = new Date(new Date().getTime()/1000 + 3600*24*30);
        this.ssn = ssn;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    public boolean isValid() {
        return !ssn.isEmpty() && date != null;
    }
}
