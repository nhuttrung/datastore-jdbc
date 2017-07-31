package vn.khtt.datastore.jdbc.calcite;

import com.google.appengine.api.datastore.DatastoreService;

import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import com.google.appengine.api.datastore.Query.Filter;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.calcite.linq4j.Enumerator;

import vn.khtt.datastore.jdbc.DatastoreConnection;

public class DatastoreEnumerator<E> implements Enumerator<E> {
//    private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    private DatastoreConnection conn;
    private final AtomicBoolean cancelFlag;
    private final DatastoreTable table;
    private Iterator<Entity> iterator;
    private E current;
    
    public DatastoreEnumerator(DatastoreConnection conn, DatastoreTable table) {
        this(conn, null, table, null);
    }
    public DatastoreEnumerator(DatastoreConnection conn, AtomicBoolean cancelFlag, DatastoreTable table) {
        this(conn, cancelFlag, table, null);
    }
    public DatastoreEnumerator(DatastoreConnection conn, AtomicBoolean cancelFlag, DatastoreTable table, Filter filter) {
        this.conn = conn;
        this.cancelFlag = cancelFlag;
        this.table = table;
        
        String tableName = table.getTableName();
        Query query = new Query(tableName);
        if (filter != null){
            query.setFilter(filter);
        }
        PreparedQuery pq = conn.getDatastoreService().prepare(query);
        iterator = pq.asIterator();
    }

    @Override
    public E current() {
        return current;
    }

    @Override
    public boolean moveNext() {
        if ((cancelFlag != null) && (cancelFlag.get())) {
            return false;
        }
        
        boolean hasNext = iterator.hasNext();
        if (!hasNext){
            return false;
        }

        Entity entity = iterator.next();
        List<DatastoreField> fields = table.getFields();
        Object[] row = new Object[fields.size()];
        for (int i=0; i<fields.size(); i++){
            DatastoreField field = fields.get(i);
            Object obj = entity.getProperty(field.getFieldName());
            // row[i] = convert(obj, field.getFieldType());
            row[i] = DataTypeConverter.convertToJdbc(obj, field.getFieldType());
        }
        current = (E)row;
        return true;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
    }
    
    private Object convert(Object obj, DatastoreFieldType fieldType){
        if (obj == null) {
            return obj;
        }
        if (fieldType == null) {
            return obj;
        }
        switch (fieldType) {
        case INT:
            if (obj instanceof Number){
                return ((Number)obj).intValue();
            } else {
                return Integer.parseInt(obj.toString());
            }
        case CHAR:
        case VARCHAR:
            return obj.toString();
        default:
            // return obj;
            throw new UnsupportedOperationException("convert " + obj + " to type " + fieldType);
        }
    }
}
