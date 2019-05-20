package imat.entities;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Date;

import static imat.validators.FormValidators.validSSN;
import static imat.validators.FormValidators.validateNames;

public class Invoice implements PaymentMethod {
    private LocalDate date = LocalDate.now().plusDays(30);
    private String ssn = "";
    private String name = "";

    public LocalDate getDate() {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        if (validSSN(ssn)) this.ssn = ssn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (validateNames(name))this.name = name;
    }

    @Override
    public boolean isValid() {
        return !ssn.isEmpty() && date != null && !name.isEmpty();
    }
}
