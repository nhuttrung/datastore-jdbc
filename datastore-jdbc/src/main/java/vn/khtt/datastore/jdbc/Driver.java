package vn.khtt.datastore.jdbc;

import com.googlecode.objectify.ObjectifyService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import java.util.Properties;
import java.util.logging.Logger;

import vn.khtt.datastore.jdbc.calcite.DatastoreFilterableTable;
import vn.khtt.datastore.jdbc.calcite.DatastoreScannableTable;
import vn.khtt.datastore.jdbc.calcite.DatastoreTable;

public class Driver implements java.sql.Driver {
    public static final int MAJOR_VERION = 1;
    public static final int MINOR_VERION = 4;
    public static final String URL_PREFIX = "jdbc:datastore:";

    private static final Driver INSTANCE = new Driver();
    private static volatile boolean registered;
    
    public static synchronized Driver load() {
        try {
            if (!registered) {
                registered = true;
                DriverManager.registerDriver(INSTANCE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return INSTANCE;
    }
    private static synchronized void registerEntities() {
//        ObjectifyService.register(DatastoreTable.class);
//        ObjectifyService.register(DatastoreScannableTable.class);
//        ObjectifyService.register(DatastoreFilterableTable.class);
    }
    
    static{
        load();
        registerEntities();
    }

    private org.apache.calcite.jdbc.Driver driver = new org.apache.calcite.jdbc.Driver();
    public Driver() {
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(URL_PREFIX);
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)){
            return null;
        }
        
        String schema = url.substring(URL_PREFIX.length());
        if (schema.isEmpty()){
            schema = "DEFAULT";
        }
        
        if (info == null){
            info = new Properties();
        }
        info.put("model",
            "inline:"
                + "{\n"
                + "  version: '1.0',\n"
                + "  defaultSchema: '" + schema + "',\n"
                + "  schemas: [\n"
                + "    {\n"
                + "      type: 'custom',\n"
                + "      name: '" + schema + "',\n"
                + "      factory: 'vn.khtt.datastore.jdbc.calcite.DatastoreSchemaFactory'\n"
                + "    }\n"
                + "  ]\n"
                + "}");
//        info.put("abc", "xyz");
        
        DatastoreConnection datastoreConnection = new DatastoreConnection(info);
        info.put(DatastoreConnection.DATASTORE_CONNECTION, datastoreConnection);
        Connection calciteConnection = driver.connect(org.apache.calcite.jdbc.Driver.CONNECT_STRING_PREFIX, info);
        datastoreConnection.calciteConnection = calciteConnection;
        return datastoreConnection;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return driver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERION;
    }

    @Override
    public int getMinorVersion() {
        return MINOR_VERION;
    }

    @Override
    public boolean jdbcCompliant() {
        return driver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return driver.getParentLogger();
    }
}
