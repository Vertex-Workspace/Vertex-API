package com.vertex.vertex.property.controller;

import com.vertex.vertex.property.model.DTO.PropertyListDTO;
import com.vertex.vertex.property.model.DTO.PropertyRegisterDTO;
import com.vertex.vertex.property.model.entity.Property;
import com.vertex.vertex.property.model.entity.PropertyList;
import com.vertex.vertex.property.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/property")
public class PropertyController {

    private final PropertyService propertyService;


    //Properties
    @PostMapping("/project/{projectID}")
    public ResponseEntity<?> saveProperty(@PathVariable Long projectID, @RequestBody Property property) {
        try {
            return new ResponseEntity<>(propertyService.save(projectID, property), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("{propertyID}/project/{projectID}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long projectID, @PathVariable Long propertyID) {
        try {
            return new ResponseEntity<>(propertyService.delete(projectID, propertyID), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    //PropertyList
    @DeleteMapping("/{propertyID}/property-list/{propertyListID}")
    public ResponseEntity<?> deletePropertyList(@PathVariable Long propertyID, @PathVariable Long propertyListID) {
        try {
            return new ResponseEntity<>(propertyService.deletePropertyList(propertyID, propertyListID), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/property-list-color")
    public ResponseEntity<?> saveProperty(@RequestBody PropertyList propertyList) {
        try {
            return new ResponseEntity<>(propertyService.changePropertyListColor(propertyList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}