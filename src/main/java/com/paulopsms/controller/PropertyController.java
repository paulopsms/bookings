package com.paulopsms.controller;

import com.paulopsms.domain.entity.PropertyEntity;
import com.paulopsms.domain.model.Property;
import com.paulopsms.domain.model.Property;
import com.paulopsms.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/properties")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Property createProperty(@RequestBody Property property) {
        return this.propertyService.saveProperty(property);
    }

    @GetMapping("/properties")
    public @ResponseBody List<Property> createProperty() {
        return this.propertyService.listProperties();
    }

    @GetMapping("/properties/{id}")
    public @ResponseBody Property findProperty(@PathVariable("id") Long propertyId) {
        return this.propertyService.findPropertyById(propertyId);
    }

    @DeleteMapping("/properties/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Property removeProperty(@PathVariable("id") Long propertyId) {
        return this.propertyService.removeProperty(propertyId);
    }
}
