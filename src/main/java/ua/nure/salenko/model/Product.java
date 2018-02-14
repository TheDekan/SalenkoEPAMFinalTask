package ua.nure.salenko.model;

import org.apache.log4j.Logger;

public class Product {

    private static final Logger LOGGER = Logger.getLogger(Product.class);

    private Long id;
    private String model;
    private String dealer;
    // fridge, stove, washer
    private String type;
    private Long worth;
    private Long length;
    private Long width;
    private Long height;
    // volume, count of burners or weight load
    private Long specialParameter1;
    // no frost,drop frost, manual || electric, gas || vertical, front
    private String specialParameter2;

    private String description;
    private String imageURL1;
    private String imageURL2;

    public Product() {
        LOGGER.info("Product#constructor");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getWorth() {
        return worth;
    }

    public void setWorth(Long worth) {
        this.worth = worth;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getSpecialParameter1() {
        return specialParameter1;
    }

    public void setSpecialParameter1(Long specialParameter1) {
        this.specialParameter1 = specialParameter1;
    }

    public String getSpecialParameter2() {
        return specialParameter2;
    }

    public void setSpecialParameter2(String specialParameter2) {
        this.specialParameter2 = specialParameter2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL1() {
        return imageURL1;
    }

    public void setImageURL1(String imageURL1) {
        this.imageURL1 = imageURL1;
    }

    public String getImageURL2() {
        return imageURL2;
    }

    public void setImageURL2(String imageURL2) {
        this.imageURL2 = imageURL2;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", model=" + model + ", dealer=" + dealer + ", type=" + type + ", worth=" + worth
                + ", length=" + length + ", width=" + width + ", height=" + height + ", specialParameter1="
                + specialParameter1 + ", specialParameter2=" + specialParameter2 + ", description=" + description
                + ", imageURL1=" + imageURL1 + ", imageURL2=" + imageURL2 + "]";
    }

}
