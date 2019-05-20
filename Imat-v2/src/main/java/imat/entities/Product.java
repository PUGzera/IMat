package imat.entities;

import com.google.gson.Gson;
import com.sun.istack.NotNull;
import se.chalmers.cse.dat216.project.ProductCategory;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private int productId;
    @NotNull
    private String category;
    @NotNull
    private String name;
    @NotNull
    private Boolean isEcological;
    @NotNull
    private double price;
    @NotNull
    private String unit;

    protected Product() {
    }

    public Product(int productId, String category, String name, Boolean isEcological, double price, String unit) {
        this.productId = productId;
        this.category = category;
        this.name = name;
        this.isEcological = isEcological;
        this.price = price;
        this.unit = unit;
    }

    public int getProductId() {
        return productId;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isEcological() {
        return this.isEcological;
    }

    public void setIsEcological(Boolean isEco) {
        this.isEcological = isEco;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return this.unit;
    }

    public String getUnitSuffix() {
        String[] tokens = this.getUnit().split("/");
        return tokens.length == 2 ? tokens[1] : "";
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String toString() {
        return this.id + " - " + this.name;
    }

}
