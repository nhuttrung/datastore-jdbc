package vn.khtt.datastore.test;

import java.sql.PreparedStatement;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import vn.khtt.datastore.jdbc.Utils;

public class TransactionTests extends AbstractJdbcTest {
    public TransactionTests() {
    }

    @Test
    public void testSimpleTransaction() throws Exception {
        // Create table
        String sql = "CREATE TABLE CUSTOMER (ID INT, NAME VARCHAR) PRIMARY KEY(ID)";
        conn.createStatement().execute(sql);
        
        // Start a transaction
        conn.setAutoCommit(false);
        try{
            // INSET & UPDATE
            sql = "INSERT INTO CUSTOMER (ID, NAME) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 100);
            stmt.setString(2, "Apple");
            stmt.executeUpdate();
            
            sql = "INSERT INTO CUSTOMER (ID, NAME) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 200);
            stmt.setString(2, "Google");
            stmt.executeUpdate();

            /*
            */
            sql = "SELECT * FROM CUSTOMER WHERE ID=100";
            Utils.dumpResultSet(conn, sql);
            
            sql = "SELECT COUNT(*) FROM CUSTOMER";
//            assertEquals(Utils.count(conn, sql), 2);
            
            sql = "UPDATE CUSTOMER SET NAME=? WHERE ID=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "Facebook");
            stmt.setInt(2, 100);
            stmt.executeUpdate();
            
            // Simulate an exception
            if (Math.random() > 0.5d){
                throw new RuntimeException();
            }
            
            conn.commit();
            
            conn.setAutoCommit(true);
            sql = "SELECT * FROM CUSTOMER";
            Utils.dumpResultSet(conn, sql);
            sql = "SELECT COUNT(*) FROM CUSTOMER";
            int count = Utils.count(conn, sql);
            assertEquals(count, 2);
            return;
        } catch(Exception e) {
            e.printStackTrace();
            conn.rollback();
        }
        // Checking
        sql = "SELECT COUNT(*) FROM CUSTOMER";
        int count = Utils.count(conn, sql);
        assertEquals(count, 0);
    }
}
