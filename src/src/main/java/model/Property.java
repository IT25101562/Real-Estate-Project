package com.realestate.search.model;

public class Property {
    private String id;
    private String title;
    private String location;
    private double price;
    private String propertyType;
    private int bedrooms;

    public Property() {}

    public Property(String id, String title, String location, double price, String propertyType, int bedrooms) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.price = price;
        this.propertyType = propertyType;
        this.bedrooms = bedrooms;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }
    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }
}
