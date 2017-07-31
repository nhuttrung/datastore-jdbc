package vn.khtt.datastore.jdbc.calcite;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;

import com.googlecode.objectify.annotation.Subclass;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexDynamicParam;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.schema.FilterableTable;
import org.apache.calcite.sql.SqlKind;

import vn.khtt.datastore.jdbc.DatastoreConnection;

@Subclass
public class DatastoreFilterableTable extends DatastoreTable implements FilterableTable {
    public DatastoreFilterableTable() {
    }
    public DatastoreFilterableTable(String tableName) {
        super(tableName);
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root, List<RexNode> filters) {
        final DatastoreConnection conn = getDatastoreConnection(root.getQueryProvider());
        
        final AtomicBoolean cancelFlag = DataContext.Variable.CANCEL_FLAG.get(root);
        if (filters.size() > 1){
            throw new UnsupportedOperationException("Currently we support one filter ONLY");
        }
        final Filter[] queryFilter = new Filter[1];
        if (filters.size() == 1){
            RexNode filter = filters.get(0);
            queryFilter[0] = buildQueryFilter(root, filter);
            filters.remove(0);
        }
        
        return new AbstractEnumerable<Object[]>() {
            public Enumerator<Object[]> enumerator() {
                return new DatastoreEnumerator<>(conn, cancelFlag, DatastoreFilterableTable.this, queryFilter[0]);
            }
        };
    }
    
    private Filter buildQueryFilter(DataContext root, RexNode filter){
        if (filter.isA(SqlKind.EQUALS)) {
            final RexCall call = (RexCall) filter;
            RexNode left = call.getOperands().get(0);
            if (left.isA(SqlKind.CAST)) {
                left = ((RexCall) left).operands.get(0);
            }
            RexNode right = call.getOperands().get(1);
            if (right.isA(SqlKind.CAST)) {
                right = ((RexCall) right).operands.get(0);
            }
            
            if (left instanceof RexInputRef && right instanceof RexLiteral) {
                // WHERE ID=1
                final int index = ((RexInputRef) left).getIndex();
                
                String fieldName = fields.get(index).getFieldName();
                Object value = ((RexLiteral) right).getValue2();
                return new Query.FilterPredicate(fieldName, Query.FilterOperator.EQUAL, value);
            } else if (right instanceof RexInputRef && left instanceof RexLiteral) {
                // WHERE 1=ID
                final int index = ((RexInputRef) right).getIndex();
                
                String fieldName = fields.get(index).getFieldName();
                Object value = ((RexLiteral) left).getValue2();
                return new Query.FilterPredicate(fieldName, Query.FilterOperator.EQUAL, value);
            } else if (left instanceof RexInputRef && right instanceof RexDynamicParam) {
                // WHERE ID=?
                final int index = ((RexInputRef) left).getIndex();
                
                String fieldName = fields.get(index).getFieldName();
                String name = ((RexDynamicParam)right).getName();
                Object value = root.get(name);
                return new Query.FilterPredicate(fieldName, Query.FilterOperator.EQUAL, value);
            } else if (right instanceof RexInputRef && left instanceof RexDynamicParam) {
                // WHERE ?=ID
                final int index = ((RexInputRef) right).getIndex();
                
                String fieldName = fields.get(index).getFieldName();
                String name = ((RexDynamicParam)left).getName();
                Object value = root.get(name);
                return new Query.FilterPredicate(fieldName, Query.FilterOperator.EQUAL, value);
            } else if (left instanceof RexLiteral && right instanceof RexLiteral) {
                // WHERE 1=1
                Object leftValue = ((RexLiteral) left).getValue2();
                Object rightValue = ((RexLiteral) right).getValue2();
                if (leftValue.equals(rightValue)){      // WHERE 1=1
                    return null;
                } else {
                    // TODO
                }
            }
        }
        
        if (filter.isA(SqlKind.AND)) {
            final RexCall call = (RexCall) filter;
            Query.Filter left = buildQueryFilter(root, call.getOperands().get(0));
            Query.Filter right = buildQueryFilter(root, call.getOperands().get(1));
            if (left == null){      // WHERE 1=1 AND COLUMN_1=2
                return right;
            }
            if (right == null){     // WHERE COLUMN_1=2 AND 1=1
                return left;
            }
            return CompositeFilterOperator.and(left, right);
        }
        
        if (filter.isA(SqlKind.OR)) {
            final RexCall call = (RexCall) filter;
            Query.Filter left = buildQueryFilter(root, call.getOperands().get(0));
            Query.Filter right = buildQueryFilter(root, call.getOperands().get(1));
            if (left == null){      // WHERE 1=1 OR COLUMN_1=2
                return null;
            }
            if (right == null){     // WHERE COLUMN_1=2 OR 1=1
                return null;
            }
            return CompositeFilterOperator.or(left, right);
        }

        throw new UnsupportedOperationException(filter.getKind() + ": " + filter);
    }
}
