package com.realestate.search.model;

public class SearchCriteria {
    private String location;
    private Double minPrice;
    private Double maxPrice;
    private String propertyType;

    public SearchCriteria() {}

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }
    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }
    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    @Override
    public String toString() {
        return (location != null ? location : "") + "|" +
               (minPrice != null ? minPrice : "") + "|" +
               (maxPrice != null ? maxPrice : "") + "|" +
               (propertyType != null ? propertyType : "");
    }

    public static SearchCriteria fromString(String str) {
        SearchCriteria criteria = new SearchCriteria();
        if (str == null || str.isEmpty()) return criteria;
        String[] parts = str.split("\\|", -1);
        if (parts.length > 0 && !parts[0].isEmpty()) criteria.setLocation(parts[0]);
        if (parts.length > 1 && !parts[1].isEmpty()) criteria.setMinPrice(Double.parseDouble(parts[1]));
        if (parts.length > 2 && !parts[2].isEmpty()) criteria.setMaxPrice(Double.parseDouble(parts[2]));
        if (parts.length > 3 && !parts[3].isEmpty()) criteria.setPropertyType(parts[3]);
        return criteria;
    }
}
