package com.realestate.propertymanagement.controller;

import com.realestate.propertymanagement.model.Apartment;
import com.realestate.propertymanagement.model.Land; //give access to controller can create Land objects.
import com.realestate.propertymanagement.model.House;
import com.realestate.propertymanagement.model.Property;
import com.realestate.propertymanagement.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PropertyController {

    private PropertyService propertyService = new PropertyService();

    @GetMapping("/")
    public String home() {
        return "property-list";
    }

    @GetMapping("/properties")
    public String viewProperties(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        return "property-list";
    }

    @GetMapping("/properties/add")
    public String showAddPropertyForm() {
        return "property-add";
    }

    //controller can create: House Apartment Land based on the selected propertyType.
    @PostMapping("/properties/update")
    public String updateProperty(@RequestParam String propertyId,
                                 @RequestParam String propertyType,
                                 @RequestParam String title,
                                 @RequestParam String location,
                                 @RequestParam double price,
                                 @RequestParam String description,

                                 @RequestParam(required = false, defaultValue = "0") int bedrooms,
                                 @RequestParam(required = false, defaultValue = "0") int bathrooms,

                                 @RequestParam(required = false, defaultValue = "0") int floorNumber,
                                 @RequestParam(required = false, defaultValue = "") String unitNumber,

                                 @RequestParam(required = false, defaultValue = "0") double landSize,
                                 @RequestParam(required = false, defaultValue = "") String landType) {

        Property updatedProperty;

        if (propertyType.equalsIgnoreCase("House")) {
            updatedProperty = new House(
                    propertyId, title, location, price,
                    propertyType, description, bedrooms, bathrooms
            );

        } else if (propertyType.equalsIgnoreCase("Apartment")) {
            updatedProperty = new Apartment(
                    propertyId, title, location, price,
                    propertyType, description, floorNumber, unitNumber
            );

        } else {
            updatedProperty = new Land(
                    propertyId, title, location, price,
                    propertyType, description, landSize, landType
            );
        }

        propertyService.updateProperty(updatedProperty);

        return "redirect:/properties";
    }

    @GetMapping("/properties/search")
    public String searchProperties(@RequestParam(required = false) String keyword,
                                   Model model) {

        if (keyword == null || keyword.isEmpty()) {
            model.addAttribute("properties", propertyService.getAllProperties());
        } else {
            model.addAttribute("properties", propertyService.searchProperties(keyword));
        }

        return "property-search";
    }

    // Add Update/Edit feature
    @GetMapping("/properties/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Property property = propertyService.findById(id);

        if (property == null) {
            return "redirect:/properties";
        }

        model.addAttribute("property", property);
        return "property-edit";
    }

    //
    @GetMapping("/properties/details/{id}")
    public String viewPropertyDetails(@PathVariable String id, Model model) {
        Property property = propertyService.findById(id);

        if (property == null) {
            return "redirect:/properties";
        }

        model.addAttribute("property", property);
        return "property-details";
    }

    @GetMapping("/properties/delete/{id}")
    public String deleteProperty(@PathVariable String id) {
        propertyService.deleteProperty(id);
        return "redirect:/properties";
    }

    @PostMapping("/properties/add")
    public String addProperty(@RequestParam String propertyId,
                              @RequestParam String propertyType,
                              @RequestParam String title,
                              @RequestParam String location,
                              @RequestParam double price,
                              @RequestParam String description,

                              @RequestParam(required = false, defaultValue = "0") int bedrooms,
                              @RequestParam(required = false, defaultValue = "0") int bathrooms,

                              @RequestParam(required = false, defaultValue = "0") int floorNumber,
                              @RequestParam(required = false, defaultValue = "") String unitNumber,

                              @RequestParam(required = false, defaultValue = "0") double landSize,
                              @RequestParam(required = false, defaultValue = "") String landType,

                              Model model) {

        Property property;

        if (propertyType.equalsIgnoreCase("House")) {
            property = new House(propertyId, title, location, price,
                    propertyType, description, bedrooms, bathrooms);

        } else if (propertyType.equalsIgnoreCase("Apartment")) {
            property = new Apartment(propertyId, title, location, price,
                    propertyType, description, floorNumber, unitNumber);

        } else {
            property = new Land(propertyId, title, location, price,
                    propertyType, description, landSize, landType);
        }

        boolean added = propertyService.addProperty(property);

        if (!added) {
            model.addAttribute("errorMessage", "Property ID already exists.");
            return "property-add";
        }

        return "redirect:/properties";
    }

}
