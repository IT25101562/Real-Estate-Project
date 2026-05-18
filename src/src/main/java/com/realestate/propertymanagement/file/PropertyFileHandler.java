package com.realestate.propertymanagement.file;

import com.realestate.propertymanagement.model.Apartment;
import com.realestate.propertymanagement.model.House;
import com.realestate.propertymanagement.model.Land;
import com.realestate.propertymanagement.model.Property;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyFileHandler {

    private final String filePath = "data/properties.txt";

    public List<Property> loadProperties() {
        List<Property> properties = new ArrayList<>();

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                return properties;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                Property property = convertLineToProperty(line);

                if (property != null) {
                    properties.add(property);
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading property file: " + e.getMessage());
        }

        return properties;
    }

    public void saveProperties(List<Property> properties) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (Property property : properties) {
                writer.write(property.toFileString());
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error writing property file: " + e.getMessage());
        }
    }

    private Property convertLineToProperty(String line) {
        try {
            String[] data = line.split(",");

            String propertyId = data[0];
            String propertyType = data[1];
            String title = data[2];
            String location = data[3];
            double price = Double.parseDouble(data[4]);
            String description = data[5];

            if (propertyType.equalsIgnoreCase("House")) {
                int bedrooms = Integer.parseInt(data[6]);
                int bathrooms = Integer.parseInt(data[7]);

                return new House(propertyId, title, location, price,
                        propertyType, description, bedrooms, bathrooms);
            }

            if (propertyType.equalsIgnoreCase("Apartment")) {
                int floorNumber = Integer.parseInt(data[6]);
                String unitNumber = data[7];

                return new Apartment(propertyId, title, location, price,
                        propertyType, description, floorNumber, unitNumber);
            }

            if (propertyType.equalsIgnoreCase("Land")) {
                double landSize = Double.parseDouble(data[6]);
                String landType = data[7];

                return new Land(propertyId, title, location, price,
                        propertyType, description, landSize, landType);
            }

        } catch (Exception e) {
            System.out.println("Error converting line to property: " + e.getMessage());
        }

        return null;
    }

}
