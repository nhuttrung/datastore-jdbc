package vn.khtt.datastore.jdbc.calcite;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.avatica.AvaticaConnection;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.AbstractTableQueryable;
import org.apache.calcite.util.Pair;

import vn.khtt.datastore.jdbc.DatastoreConnection;

@Entity
public abstract class DatastoreTable extends AbstractQueryableTable { // implements ModifiableTable {
    @Id
    private String tableName;
    
    protected List<DatastoreField> fields = new ArrayList<DatastoreField>();
    
    public DatastoreTable() {
        this(null);
    }
    public DatastoreTable(String tableName) {
        super(Object[].class);
        this.tableName = tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
    
    public void addField(DatastoreField field){
        fields.add(field);
    }

    public List<DatastoreField> getFields() {
        return fields;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        final List<String> names = new ArrayList<>();
        final List<RelDataType> types = new ArrayList<>();
        
        for (DatastoreField field : fields){
            names.add(field.getFieldName());
            types.add(field.getFieldType().toType((JavaTypeFactory)typeFactory));
        }
        
        return typeFactory.createStructType(Pair.zip(names, types));
    }

    @Override
    public <T> Queryable<T> asQueryable(QueryProvider queryProvider, SchemaPlus schema, String tableName) {
        final DatastoreConnection conn = getDatastoreConnection(queryProvider);

        return new AbstractTableQueryable<T>(queryProvider, schema, this, tableName) {
            @Override
            public Enumerator<T> enumerator() {
                return new DatastoreEnumerator<>(conn, DatastoreTable.this);
            }
        };
    }

    protected DatastoreConnection getDatastoreConnection(QueryProvider queryProvider){
        Properties props = getConnectionProperties(queryProvider);
        final DatastoreConnection conn = 
            (DatastoreConnection)props.get(DatastoreConnection.DATASTORE_CONNECTION);
        return conn;
    }
    private Properties getConnectionProperties(Object queryProvider){
        if (!(queryProvider instanceof AvaticaConnection)){
            throw new RuntimeException();
        }
        AvaticaConnection conn = (AvaticaConnection)queryProvider;
        try {
            Class clazz = AvaticaConnection.class;
            
            Field field = clazz.getDeclaredField("info");
            field.setAccessible(true);
            Properties info = (Properties)field.get(conn);

            return info;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
