package imat.entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private LocalDate localDate;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String address;
    @NotNull
    private Double price;
    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Product, Integer> map;

    protected Order() {
    }

    public Order(LocalDate localDate, String firstName, String lastName, String address, Double price, Map<Product, Integer> map) {
        this.localDate = localDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.price = price;
        this.map = map;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public Double getPrice() {
        return price;
    }

    public Map<Product, Integer> getMap() {
        return map;
    }
}
