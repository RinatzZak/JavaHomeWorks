package com.spring.spring_boot.dao;

import com.spring.spring_boot.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    public List<Employee> getAllEmployees();

    public void saveEmployee(Employee employee);

   public Employee getAllEmployee(int id);

   public void deleteEmployee(int id);
}
