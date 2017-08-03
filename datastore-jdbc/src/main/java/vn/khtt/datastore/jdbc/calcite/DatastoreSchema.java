package vn.khtt.datastore.jdbc.calcite;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

public class DatastoreSchema extends AbstractSchema {
    public static final DatastoreSchema DEFAULT = new DatastoreSchema();
    
    private Map<String, Table> tableMap;
    
    public DatastoreSchema() {
    }

    @Override
    protected Map<String, Table> getTableMap() {
      if (tableMap == null) {
        tableMap = createTableMap();
      }
      return tableMap;
    }
    
    public boolean isMutable() {
        return false;
    }
    
    public void addTable(DatastoreTable table){
        ofy().save().entity(table).now();
        tableMap = null;
    }
    public void deleteTable(DatastoreTable table){
        ofy().delete().entity(table).now();
        tableMap = null;
    }
    public DatastoreTable getDatastoreTable(String name){
        Table table = getTableMap().get(name);
        return (DatastoreTable)table;
    }
    
    private Map<String, Table> createTableMap() {
        List<DatastoreTable> tables = ofy().load().type(DatastoreTable.class).list();
        Map<String, Table> tableMap = new HashMap<String, Table>();
        
        for (DatastoreTable table : tables){
            tableMap.put(table.getTableName(), table);
        }
    
        return tableMap;
    }
}
