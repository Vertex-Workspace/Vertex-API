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

@RestController
@RequestMapping("/property")
@AllArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/{id}")
    public ResponseEntity<Property> findById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(propertyService.findById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    @GetMapping
    public ResponseEntity<List<Property>> findAll(){
        try{
            return new ResponseEntity<>(propertyService.findAll(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<Property> save(@RequestBody PropertyRegisterDTO propertyRegisterDTO){
        try{
            return new ResponseEntity<>(propertyService.save(propertyRegisterDTO), HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PatchMapping
    public ResponseEntity<Property> save(@RequestBody PropertyListDTO propertyListDTO){
        try{
            return new ResponseEntity<>(propertyService.save(propertyListDTO), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Property> delete(@PathVariable Long id){
        try{
            propertyService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
