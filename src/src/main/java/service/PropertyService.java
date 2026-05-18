package com.realestate.search.service;

import com.realestate.search.filter.PropertyFilter;
import com.realestate.search.model.Property;
import com.realestate.search.model.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {

    private final List<Property> properties = new ArrayList<>();
    private final List<PropertyFilter> filters;

    @Autowired
    public PropertyService(List<PropertyFilter> filters) {
        this.filters = filters;
        properties.add(new Property("1", "Luxury Villa", "Los Angeles", 1500000, "Villa", 4));
        properties.add(new Property("2", "Modern Apartment", "New York", 850000, "Apartment", 2));
        properties.add(new Property("3", "Cozy Cottage", "London", 600000, "House", 3));
        properties.add(new Property("4", "Downtown Condo", "New York", 950000, "Apartment", 3));
        properties.add(new Property("5", "Suburban House", "Chicago", 550000, "House", 4));
        properties.add(new Property("6", "Beachfront Property", "Miami", 2500000, "House", 5));
        properties.add(new Property("7", "Studio Apartment", "Los Angeles", 450000, "Apartment", 1));
    }

    public List<Property> search(SearchCriteria criteria) {
        List<Property> results = new ArrayList<>();
        for (Property property : properties) {
            boolean matchesAll = true;
            for (PropertyFilter filter : filters) {
                if (!filter.matches(property, criteria)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                results.add(property);
            }
        }
        return results;
    }
    
    public Property getPropertyById(String id) {
        return properties.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
}
