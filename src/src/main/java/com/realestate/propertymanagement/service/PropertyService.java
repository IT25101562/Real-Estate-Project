package com.realestate.propertymanagement.service;

import com.realestate.propertymanagement.file.PropertyFileHandler;
import com.realestate.propertymanagement.model.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyService {

    private PropertyFileHandler fileHandler = new PropertyFileHandler();

    public boolean addProperty(Property property) {
        List<Property> properties = fileHandler.loadProperties();

        for (Property existingProperty : properties) {
            if (existingProperty.getPropertyId().equalsIgnoreCase(property.getPropertyId())) {
                return false;
            }
        }

        properties.add(property);
        fileHandler.saveProperties(properties);

        return true;
    }

    public List<Property> getAllProperties() {
        return fileHandler.loadProperties();
    }

    public Property findById(String propertyId) {
        List<Property> properties = fileHandler.loadProperties();

        for (Property property : properties) {
            if (property.getPropertyId().equalsIgnoreCase(propertyId)) {
                return property;
            }
        }

        return null;
    }

    public boolean deleteProperty(String propertyId) {
        List<Property> properties = fileHandler.loadProperties();

        boolean removed = properties.removeIf(
                property -> property.getPropertyId().equalsIgnoreCase(propertyId)
        );

        if (removed) {
            fileHandler.saveProperties(properties);
        }

        return removed;
    }

    public boolean updateProperty(Property updatedProperty) {
        List<Property> properties = fileHandler.loadProperties();

        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getPropertyId().equalsIgnoreCase(updatedProperty.getPropertyId())) {
                properties.set(i, updatedProperty);
                fileHandler.saveProperties(properties);
                return true;
            }
        }

        return false;
    }

    public List<Property> searchByLocation(String location) {
        List<Property> properties = fileHandler.loadProperties();
        List<Property> results = new ArrayList<>();

        for (Property property : properties) {
            if (property.getLocation().equalsIgnoreCase(location)) {
                results.add(property);
            }
        }

        return results;
    }

    public List<Property> searchByType(String type) {
        List<Property> properties = fileHandler.loadProperties();
        List<Property> results = new ArrayList<>();

        for (Property property : properties) {
            if (property.getPropertyType().equalsIgnoreCase(type)) {
                results.add(property);
            }
        }

        return results;
    }

    public List<Property> searchProperties(String keyword) {
        List<Property> properties = fileHandler.loadProperties();
        List<Property> results = new ArrayList<>();

        String[] searchTerms = keyword.split(",");

        for (Property property : properties) {
            boolean matchesAllTerms = true;

            for (String term : searchTerms) {
                String cleanTerm = term.trim().toLowerCase();

                boolean matchesThisTerm =
                        property.getPropertyId().toLowerCase().contains(cleanTerm)
                                || property.getLocation().toLowerCase().contains(cleanTerm)
                                || property.getPropertyType().toLowerCase().contains(cleanTerm)
                                || property.getTitle().toLowerCase().contains(cleanTerm);

                if (!matchesThisTerm) {
                    matchesAllTerms = false;
                    break;
                }
            }

            if (matchesAllTerms) {
                results.add(property);
            }
        }

        return results;
    }

}
