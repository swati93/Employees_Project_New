package com.rakuten.fullstackrecruitmenttest.repository;

import com.rakuten.fullstackrecruitmenttest.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}