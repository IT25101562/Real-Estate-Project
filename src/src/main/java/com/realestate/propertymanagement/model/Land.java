package com.realestate.propertymanagement.model;

public class Land extends Property{

    private double landSize;
    private String landType;

    public Land() {
    }

    public Land(String propertyId, String title, String location,
                double price, String propertyType, String description,
                double landSize, String landType) {

        super(propertyId, title, location, price, propertyType, description);

        this.landSize = landSize;
        this.landType = landType;
    }

    public double getLandSize() {
        return landSize;
    }

    public void setLandSize(double landSize) {
        this.landSize = landSize;
    }

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    @Override
    public String displayDetails() {
        return super.displayDetails()
                + " | Land Size: " + landSize
                + " perches | Land Type: " + landType;
    }

    @Override
    public String toFileString() {
        return super.toFileString()
                + "," + landSize
                + "," + landType;
    }

}
