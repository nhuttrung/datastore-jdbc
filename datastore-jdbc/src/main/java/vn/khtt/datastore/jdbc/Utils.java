package vn.khtt.datastore.jdbc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
    // sql SHOUD BE 'SELECT COUNT(*) FROM MY_TABLE [WHERE ...]'
    public static int count(Connection conn, String sql) throws SQLException {
        System.out.println();
        System.out.println(sql);

        ResultSet rs = conn.createStatement().executeQuery(sql);
        int count;
        if (rs.next()){
            count = rs.getInt(1);
        } else {
            count = 0;
        }
        System.out.println("count: " + count);
        return count;
    }
    
    public static void dumpResultSet(Connection conn, String sql) throws SQLException {
        System.out.println();
        System.out.println(sql);
        ResultSet rs = conn.createStatement().executeQuery(sql);
        dumpResultSet(rs);
    }
    public static void dumpResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        while (rs.next()){
            for (int i=1; i<=meta.getColumnCount(); i++){
                String columnName = meta.getColumnName(i);
                System.out.print(columnName + ": " + rs.getObject(i) + ", ");
            }
            System.out.println();
        }
    }

    public static void executeSqlScript(Connection conn, InputStream in) throws Exception {
        Statement stmt = conn.createStatement();
        
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String line;
        String sql = "";
        while ((line = input.readLine()) != null) {
            line = line.trim();
            sql = sql + " " + line;
            if (line.endsWith(";")){
                // TODO
                System.out.println(sql);
                stmt.execute(sql);
                
                sql = "";
            }
        }
    }
}
