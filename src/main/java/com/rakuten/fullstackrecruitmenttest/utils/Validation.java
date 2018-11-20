package com.rakuten.fullstackrecruitmenttest.utils;

import com.rakuten.fullstackrecruitmenttest.exception.EmployeeException;
import com.rakuten.fullstackrecruitmenttest.model.Employee;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.rakuten.fullstackrecruitmenttest.utils.Constants.DEPARTMENT_PATTERN;
import static com.rakuten.fullstackrecruitmenttest.utils.Constants.LOG_ERROR_FILE_NAME;
import static com.rakuten.fullstackrecruitmenttest.utils.Constants.UNKNOWN_FILE_EXCEPTION_MESSAGE;

@Service
public class Validation {


    public String checkEmployeeInfoCompliance(List<Employee> employeesList){
        String exceptionMessage = "";
        Long line = 0l;
        for(Employee employee : employeesList) {
            try {
                line++;
                verify(employee, line);
            } catch (EmployeeException e) {
                try {
                    createOrUpdateErrorFile(e.getMessage());
                    exceptionMessage += "\n" + e.getMessage();
                } catch (IOException e1) {
                    System.out.println(UNKNOWN_FILE_EXCEPTION_MESSAGE);
                }
            }
        }
        return exceptionMessage;
    }

    public void verify(Employee employee, Long line) throws EmployeeException {
        if (!StringUtils.isAlpha(employee.getName())) {
            throw new EmployeeException("Not an alphabet name:\"" + employee.getName() + "\" at Line:" + line);
        } else if (!employee.getDepartment().matches(DEPARTMENT_PATTERN)) {
            throw new EmployeeException("Not an alphanumeric department :\"" + employee.getDepartment() + "\"at Line:" + line);
        } else if (!Designation.exist(employee.getDesignation())) {
            throw new EmployeeException("Not a valid designation :\"" + employee.getDesignation() + "\" at Line:" + line);
        } else if (!NumberUtils.isCreatable(employee.getSalary())) {
            throw new EmployeeException("Not an numeric salary :\"" + employee.getSalary() + "\" at Line:" + line);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate.parse(employee.getJoiningDate(), formatter);
            } catch (DateTimeParseException e) {
                throw new EmployeeException("Not a valid date :\"" + employee.getJoiningDate() + "\" at Line:" + line);
            }
        }
    }

    public void createOrUpdateErrorFile(String message) throws IOException {
        File file = new File(LOG_ERROR_FILE_NAME);
        FileWriter writer = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(message);
        bw.newLine();
        bw.close();
    }
}
