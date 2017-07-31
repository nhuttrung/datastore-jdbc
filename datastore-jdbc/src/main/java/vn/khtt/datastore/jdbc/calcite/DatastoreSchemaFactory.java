package vn.khtt.datastore.jdbc.calcite;

import com.googlecode.objectify.ObjectifyService;

import java.util.Map;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

public class DatastoreSchemaFactory implements SchemaFactory {
    static{
        registerEntities();
    }
    private static synchronized void registerEntities() {
        ObjectifyService.register(DatastoreTable.class);
        ObjectifyService.register(DatastoreScannableTable.class);
        ObjectifyService.register(DatastoreFilterableTable.class);
    }

    /** Public singleton, per factory contract. */
    public static final DatastoreSchemaFactory INSTANCE = new DatastoreSchemaFactory();

    private DatastoreSchemaFactory() {
    }

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        return new DatastoreSchema();
    }
}
