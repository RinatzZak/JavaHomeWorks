package com.spring.spring_boot.controller;

import com.spring.spring_boot.entity.Employee;
import com.spring.spring_boot.service.ImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRestController {
    @Autowired
    private ImplService implService;

    @GetMapping("/employees")
    public List<Employee> showAllEmployees(){
        List<Employee> allEmployees = implService.getAllEmployees();
        return allEmployees;
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id){
        Employee employee = implService.getEmployee(id);


        return employee;
    }


    @PostMapping("/employees")
    public Employee addNewEmployee(@RequestBody Employee employee){
        implService.saveEmployee(employee);
        return employee;
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee){
        implService.saveEmployee(employee);
        return employee;
    }
    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable int id){
        Employee employee = implService.getEmployee(id);
        implService.deleteEmployee(id);
        return "Employee with id = " + id + " was deleted!";
    }

}
