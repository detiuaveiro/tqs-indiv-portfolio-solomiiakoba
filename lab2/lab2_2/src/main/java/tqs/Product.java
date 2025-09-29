package tqs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    public int id;
    public String name;
    public String description;
    public double price;
    public String title;
    public String category;

    public Product(int id, String name, String description, double price, String title, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.title = title;
        this.category = category;
    }

    public Product() {

    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
}