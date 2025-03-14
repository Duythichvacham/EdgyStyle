/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.Products;

/**
 *
 * @author nttu2
 */
public class ProductDTO {

    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String color;
    private String brand;
<<<<<<< HEAD
    private String size;
    private int ct_id;
    private int quantity;
=======
    private int ct_id;
>>>>>>> 883bc3bece04c0ce3713fec7b32f26942bd2bf27

    public ProductDTO() {
    }

    public ProductDTO(int id, String name, String description, double price, String imageUrl, String color, String brand, int ct_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.color = color;
        this.brand = brand;
        this.ct_id = ct_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCt_id() {
        return ct_id;
    }

    public void setCt_id(int ct_id) {
        this.ct_id = ct_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
