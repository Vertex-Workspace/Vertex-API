package com.vertex.vertex.task_property.controller;

import com.vertex.vertex.task.model.entity.TaskProperty;
import com.vertex.vertex.task_property.service.TaskPropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/task-property")
public class TaskPropertyController {

    private final TaskPropertyService taskPropertyService;

//    @PostMapping
//    public ResponseEntity<TaskProperty> save(@RequestBody TaskProperty taskProperty){
//        try{
//            return new ResponseEntity<>(taskPropertyService.save(taskProperty), HttpStatus.CREATED);
//        }catch(Exception e){
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//    }

    @GetMapping
    public ResponseEntity<List<TaskProperty>> findAll(){
        try{
            return new ResponseEntity<>(taskPropertyService.findAll(), HttpStatus.FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskProperty> findById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(taskPropertyService.findById(id), HttpStatus.FOUND);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskProperty> delete(@PathVariable Long id){
        try{
            taskPropertyService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }




}
