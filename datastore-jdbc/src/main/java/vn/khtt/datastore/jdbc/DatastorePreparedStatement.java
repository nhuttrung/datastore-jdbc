package vn.khtt.datastore.jdbc;

import java.io.InputStream;
import java.io.Reader;

import java.math.BigDecimal;

import java.net.URL;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

public class DatastorePreparedStatement extends DatastoreStatement implements PreparedStatement {
    private DatastoreConnection conn;
    private String sql;
    private PreparedStatement calcitePreparedStatement;
    
    private Insert insert;
    private Update update;
    private Delete delete;
    
    private Map<Integer, Object> params = new HashMap<Integer, Object>();

    public DatastorePreparedStatement(DatastoreConnection conn, String sql, PreparedStatement calcitePreparedStatement) throws SQLException {
        super(conn, calcitePreparedStatement);
        
        this.conn = conn;
        this.sql = sql;
        this.calcitePreparedStatement = calcitePreparedStatement;
        
        net.sf.jsqlparser.statement.Statement stmt = parseSql(sql);
        if (stmt instanceof Insert){
            insert = (Insert)stmt;
        }
        if (stmt instanceof Update){
            update = (Update)stmt;
        }
        if (stmt instanceof Delete){
            delete = (Delete)stmt;
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        warnIfQueryInsideTransaction();
        return calcitePreparedStatement.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        if (insert != null){
            return executeInsert(insert, params);
        }
        if (update != null){
            return executeUpdate(update, params);
        }
        if (delete != null){
            return executeDelete(delete, params);
        }
        
        notImplemented();
        return 0;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        // Empty --> TODO
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        // Empty --> TODO
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setBoolean(parameterIndex, x);
        }
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setByte(parameterIndex, x);
        }
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setShort(parameterIndex, x);
        }
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setInt(parameterIndex, x);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setLong(parameterIndex, x);
        }
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setFloat(parameterIndex, x);
        }
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setDouble(parameterIndex, x);
        }
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setBigDecimal(parameterIndex, x);
        }
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setString(parameterIndex, x);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
//        this.notImplemented();
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setBytes(parameterIndex, x);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setDate(parameterIndex, x);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        notImplemented();
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setDate(parameterIndex, x, cal);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setTime(parameterIndex, x);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        this.notImplemented();
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setTime(parameterIndex, x, cal);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        params.put(parameterIndex, new java.util.Date(x.getTime()));
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setTimestamp(parameterIndex, x);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        this.notImplemented();
        params.put(parameterIndex, x);
        if (calcitePreparedStatement != null){
            calcitePreparedStatement.setTimestamp(parameterIndex, x, cal);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        notImplemented();
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        notImplemented();
    }

    @Override
    public void clearParameters() throws SQLException {
        params.clear();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        notImplemented();
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        notImplemented();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        notImplemented();
    }

    @Override
    public boolean execute() throws SQLException {
        notImplemented();

        // TODO Implement this method
        return false;
    }

    @Override
    public void addBatch() throws SQLException {
        notImplemented();

        // TODO Implement this method
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        notImplemented();
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        notImplemented();
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        notImplemented();
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        notImplemented();
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        notImplemented();
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        notImplemented();
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        notImplemented();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        notImplemented();
        return null;
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        notImplemented();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        notImplemented();
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        notImplemented();
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        notImplemented();
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        notImplemented();
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        notImplemented();
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        notImplemented();
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        notImplemented();
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        notImplemented();
    }

    private void notImplemented(){
        RuntimeException e = new UnsupportedOperationException("DatastorePreparedStatement: Not implemented");
        e.printStackTrace();
        throw e;
    }
}
