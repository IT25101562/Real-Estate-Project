package com.realestate.propertymanagement.model;

public class House extends Property {

    private int bedrooms;
    private int bathrooms;

    public House() {
    }

    public House(String propertyId, String title, String location,
                 double price, String propertyType, String description,
                 int bedrooms, int bathrooms) {

        super(propertyId, title, location, price, propertyType, description);

        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    @Override
    public String displayDetails() {
        return super.displayDetails()
                + " | Bedrooms: " + bedrooms
                + " | Bathrooms: " + bathrooms;
    }

    @Override
    public String toFileString() {
        return super.toFileString()
                + "," + bedrooms
                + "," + bathrooms;
    }

}
