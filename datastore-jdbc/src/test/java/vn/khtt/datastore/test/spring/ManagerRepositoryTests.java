package vn.khtt.datastore.test.spring;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import vn.khtt.datastore.test.HibernateUtils;
import vn.khtt.datastore.test.ResetPerMethodTest;
import vn.khtt.datastore.test.model.Employee;
import vn.khtt.datastore.test.model.EntityConfig;
import vn.khtt.datastore.test.model.Manager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(EntityConfig.class)
public class ManagerRepositoryTests extends ResetPerMethodTest {
    @Autowired
    private ManagerRepository repository;

    @Autowired
    private DataSource dataSource;

    public ManagerRepositoryTests() {
    }

    public void setUp() throws Exception {
        super.setUp();
        
        HibernateUtils.autoCreateSchema(dataSource, Employee.class.getPackage());
    }

    @Test
    public void createManager() throws Exception {
        String[] roles = new String[]{"ROLE_ADMIN"};
        Manager manager = new Manager("ADMIN", roles);
        repository.save(manager);
        
        manager = repository.findOne(manager.getId());
        Assert.assertArrayEquals(manager.getRoles(), roles);
    }
}
