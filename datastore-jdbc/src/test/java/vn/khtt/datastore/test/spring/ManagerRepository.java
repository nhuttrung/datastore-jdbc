package vn.khtt.datastore.test.spring;

import org.springframework.data.repository.CrudRepository;

import vn.khtt.datastore.test.model.Manager;

public interface ManagerRepository extends CrudRepository<Manager, Long> {
}
