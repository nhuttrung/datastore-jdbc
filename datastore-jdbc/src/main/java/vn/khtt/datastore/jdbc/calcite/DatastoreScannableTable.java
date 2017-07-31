package vn.khtt.datastore.jdbc.calcite;

import com.googlecode.objectify.annotation.Subclass;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.schema.ScannableTable;

import vn.khtt.datastore.jdbc.DatastoreConnection;

@Subclass
public class DatastoreScannableTable extends DatastoreTable implements ScannableTable {
    public DatastoreScannableTable() {
    }
    public DatastoreScannableTable(String tableName) {
        super(tableName);
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        final DatastoreConnection conn = getDatastoreConnection(root.getQueryProvider());

        final AtomicBoolean cancelFlag = DataContext.Variable.CANCEL_FLAG.get(root);
        return new AbstractEnumerable<Object[]>() {
          public Enumerator<Object[]> enumerator() {
            return new DatastoreEnumerator<>(conn, cancelFlag, DatastoreScannableTable.this);
          }
        };
    }
}
