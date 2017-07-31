package vn.khtt.datastore.test;

import java.sql.ResultSet;

import org.junit.Test;

import vn.khtt.datastore.jdbc.Utils;

public class JdbcTests extends AbstractJdbcTest {
    public JdbcTests() {
    }
    
    @Test
    public void testSqlScripts() throws Exception {
        Utils.executeSqlScript(conn, JdbcTests.class.getResourceAsStream("/create-tables.sql"));
        ResultSet tables = conn.getMetaData().getTables(null, null, null, null);
        while (tables.next()) {
            System.out.println(
                tables.getString("TABLE_CAT")
                + " " + tables.getString("TABLE_SCHEM")
                + " " + tables.getString("TABLE_NAME")
                + " " + tables.getString("TABLE_TYPE") 
            );
        }
        // Utils.dumpResultSet(tables);
        
        // 2
        Utils.executeSqlScript(conn, JdbcTests.class.getResourceAsStream("/populate-tables.sql"));
        
        // 3
        String sql = "SELECT * FROM SUPPLIERS";
        Utils.dumpResultSet(conn, sql);
    }

//    @Test
    public void testZ() throws Exception {
        String sql = "SELECT * FROM SUPPLIERS";
        Utils.dumpResultSet(conn, sql);
    }
}
