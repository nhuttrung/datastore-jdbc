package vn.khtt.datastore.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbstractJdbcTest extends ResetPerMethodTest {
    protected Connection conn;
    
    public AbstractJdbcTest() {
    }
    
    public void setUp() throws Exception {
        super.setUp();

        // 1.
        Class.forName("vn.khtt.datastore.jdbc.Driver");
        // 2.
        // Driver.load();

        try {
            conn = DriverManager.getConnection("jdbc:datastore:");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void tearDown() throws Exception {
        conn.close();
        super.tearDown();
    }
}
