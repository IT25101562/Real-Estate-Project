package com.realestate.propertymanagement.model;

public class Property {

    private String propertyId;
    private String title;
    private String location;
    private double price;
    private String propertyType;
    private String description;

    public Property() {
    }

    public Property(String propertyId, String title, String location, double price,
                    String propertyType, String description) {
        this.propertyId = propertyId;
        this.title = title;
        this.location = location;
        this.price = price;
        this.propertyType = propertyType;
        this.description = description;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String displayDetails() {
        return propertyId + " - " + title + " - " + location + " - " + price;
    }

    public String toFileString() {
        return propertyId + "," + propertyType + "," + title + "," + location + "," + price + "," + description;
    }
    
}
