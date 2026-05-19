
        return "search";
    }

    @PostMapping("/favorites/add")
    public String addFavorite(@RequestParam String propertyId, @RequestParam String title) {
        fileStorageService.saveFavorite(propertyId, title);
        return "redirect:/favorites";
    }package com.realestate.search.controller;

import com.realestate.search.model.Property;
import com.realestate.search.model.SearchCriteria;
import com.realestate.search.service.FileStorageService;
import com.realestate.search.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @Controller
    public class PropertySearchController {

        @Autowired
        private PropertyService propertyService;

        @Autowired
        private FileStorageService fileStorageService;

        @GetMapping("/")
        public String showSearchPage(Model model) {
            model.addAttribute("criteria", new SearchCriteria());
            model.addAttribute("results", propertyService.search(new SearchCriteria()));
            return "search";
        }

        @PostMapping("/search")
        public String performSearch(@ModelAttribute SearchCriteria criteria, Model model) {
            List<Property> results = propertyService.search(criteria);
            model.addAttribute("criteria", criteria);
            model.addAttribute("results", results);

    @PostMapping("/preferences/save")
    public String savePreference(@ModelAttribute SearchCriteria criteria) {
        fileStorageService.savePreference(criteria);
        return "redirect:/favorites";
    }

    @GetMapping("/favorites")
    public String showFavoritesPage(Model model) {
        model.addAttribute("favorites", fileStorageService.getFavorites());
        model.addAttribute("preferences", fileStorageService.getPreferences());
        return "favorites";
    }

    @PostMapping("/favorites/delete")
    public String deleteFavorite(@RequestParam String type, @RequestParam String id) {
        fileStorageService.deleteEntry(type, id);
        return "redirect:/favorites";
    }

    @PostMapping("/preferences/update")
    public String updatePreference(@RequestParam String id, @ModelAttribute SearchCriteria criteria) {
        fileStorageService.updatePreference(id, criteria);
        return "redirect:/favorites";
    }
}
