package com.realestate.propertymanagement.model;

public class Apartment extends Property {

    private int floorNumber;
    private String unitNumber;

    public Apartment() {
    }

    public Apartment(String propertyId, String title, String location,
                     double price, String propertyType, String description,
                     int floorNumber, String unitNumber) {

        super(propertyId, title, location, price, propertyType, description);

        this.floorNumber = floorNumber;
        this.unitNumber = unitNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    @Override
    public String displayDetails() {
        return super.displayDetails()
                + " | Floor: " + floorNumber
                + " | Unit: " + unitNumber;
    }

    @Override
    public String toFileString() {
        return super.toFileString()
                + "," + floorNumber
                + "," + unitNumber;
    }

}
