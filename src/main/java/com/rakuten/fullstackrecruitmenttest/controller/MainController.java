package com.rakuten.fullstackrecruitmenttest.controller;

import com.rakuten.fullstackrecruitmenttest.exception.EmployeeException;
import com.rakuten.fullstackrecruitmenttest.model.Employee;
import com.rakuten.fullstackrecruitmenttest.repository.EmployeeRepository;
import com.rakuten.fullstackrecruitmenttest.utils.Validation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.rakuten.fullstackrecruitmenttest.utils.Constants.LOG_ERROR_FILE_NAME;
import static com.rakuten.fullstackrecruitmenttest.utils.Constants.UNKNOWN_FILE_EXCEPTION_MESSAGE;

@RestController
public class MainController {

    @GetMapping("/test")
    @CrossOrigin(origins="*")
    public String testMethod() {
      return "test";
    }

    @Autowired
    private Validation validation;
    private EmployeeRepository repository;

    MainController(EmployeeRepository repository)
    {
        this.repository = repository;
    }

    @CrossOrigin
    @GetMapping("/employees")
    List<Employee> all() {
      return repository.findAll();
    }

    @CrossOrigin
    @PostMapping("/employees")
    public List<Employee> newEmployee(@RequestBody List<Employee> employees) throws EmployeeException {
        String errorMessage = validation.checkEmployeeInfoCompliance(employees);
        if (StringUtils.isBlank(errorMessage))
        {
            for(Employee employee: employees) {
                repository.save(employee);
            }
        }
        else
            throw new EmployeeException(errorMessage);
        return employees;
    }

    @CrossOrigin
    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws EmployeeException
    {
      List<Employee> employees = new ArrayList<Employee>();
      employees.add(newEmployee);
      String errorMessage = validation.checkEmployeeInfoCompliance(employees);
      if (StringUtils.isBlank(errorMessage)) {
        return repository.findById(id)
                .map(employee -> {
                  employee.setName(newEmployee.getName());
                  employee.setDepartment(newEmployee.getDepartment());
                  employee.setDesignation(newEmployee.getDesignation());
                  employee.setSalary(newEmployee.getSalary());
                  ;
                  return repository.save(employee);
                })
                .orElseGet(() -> {
                  newEmployee.setId(id);
                  return repository.save(newEmployee);
                });
      }
      else
        throw new EmployeeException(errorMessage);
    }

    @GetMapping(value = "/download")
    @CrossOrigin(origins = "*")
    public byte[] downloadLogError() throws Exception {
      File file = new File(LOG_ERROR_FILE_NAME);
      if (file.exists()) {
        InputStream targetStream = new FileInputStream(file);
        return IOUtils.toByteArray(targetStream);
      } else {
        throw new Exception(UNKNOWN_FILE_EXCEPTION_MESSAGE);
      }
    }
}
