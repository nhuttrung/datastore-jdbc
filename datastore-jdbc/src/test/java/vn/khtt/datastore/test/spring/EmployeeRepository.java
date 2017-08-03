package vn.khtt.datastore.test.spring;

import org.springframework.data.repository.CrudRepository;

import vn.khtt.datastore.test.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
