package imat.entities;

import javax.persistence.Entity;
import java.time.LocalDate;

import static imat.validators.FormValidators.validFutureDate;

public class ShippingInformation {

    private LocalDate date;
    private DeliveryMethod deliveryMethod;

    public ShippingInformation(LocalDate date, DeliveryMethod deliveryMethod) {
        this.date = date;
        this.deliveryMethod = deliveryMethod;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if(validFutureDate(date))this.date = date;
        this.date = LocalDate.now().plusDays(2);
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public enum DeliveryMethod {
        HEM_LEVERANS, HÄMTA_I_BUTIK;

        @Override
        public String toString() {
            switch (this) {
                case HEM_LEVERANS: return "Hem Leverans";
                case HÄMTA_I_BUTIK: return "Hämta i Butik";
                default: return "Hem Leverans";
            }
        }
    }

    public boolean isValid() {
        return validFutureDate(date);
    }
}
