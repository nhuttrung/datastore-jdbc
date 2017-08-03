package vn.khtt.datastore.test.spring;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import vn.khtt.datastore.test.HibernateUtils;
import vn.khtt.datastore.test.ResetPerMethodTest;
import vn.khtt.datastore.test.model.Employee;
import vn.khtt.datastore.test.model.EntityConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@Import(EntityConfig.class)
// @EntityScan("vn.khtt.datastore.test.model")
public class RepositoryTests extends ResetPerMethodTest {
    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private DataSource dataSource;

    public RepositoryTests() {
    }

    public void setUp() throws Exception {
        super.setUp();
        
        HibernateUtils.autoCreateSchema(dataSource, Employee.class.getPackage());
    }
    
    @Test
    public void testEmployeeRepository() throws Exception {
        System.out.println(repository.count());
    }
}
