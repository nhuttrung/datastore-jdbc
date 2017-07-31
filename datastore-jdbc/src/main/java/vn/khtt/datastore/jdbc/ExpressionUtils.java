package vn.khtt.datastore.jdbc;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;

import java.util.Map;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.schema.Column;

public class ExpressionUtils {
    public static Object convertExpression(Expression exp, Map<Integer, Object> params){
        if (exp instanceof JdbcParameter){
            JdbcParameter jdbcParameter = (JdbcParameter)exp;
            return params.get(jdbcParameter.getIndex());
        }
        
        if (exp instanceof StringValue) {
            return ((StringValue)exp).getValue();
        }
        if (exp instanceof LongValue) {
            return ((LongValue)exp).getBigIntegerValue().longValue();
        }
        if (exp instanceof DoubleValue) {
            return ((DoubleValue)exp).getValue();
        }
        
        notImplemented(exp);
        return null;
    }

    public static Query.Filter buildFilterFromWhereClause(Expression whereClause, Map<Integer, Object> params){
        if (whereClause == null){
            return null;
        }
        
        if (whereClause instanceof EqualsTo){
            EqualsTo equalsTo = (EqualsTo)whereClause;
            Column column = null;
            Object value = null;
            if (equalsTo.getLeftExpression() instanceof Column){
                column = (Column)equalsTo.getLeftExpression();
                value = convertExpression(equalsTo.getRightExpression(), params);
            }else if (equalsTo.getRightExpression() instanceof Column){
                column = (Column)equalsTo.getRightExpression();
                value = convertExpression(equalsTo.getLeftExpression(), params);
            } else {
                // where 1=1
                Object left = convertExpression(equalsTo.getLeftExpression(), params);
                Object right = convertExpression(equalsTo.getRightExpression(), params);
                if (left.equals(right)){
                    return null;
                }
                notImplemented(whereClause);
            }
            
            return new Query.FilterPredicate(column.getColumnName(), Query.FilterOperator.EQUAL, value);
        }
        
        if (whereClause instanceof IsNullExpression){
            IsNullExpression isNullExpression = (IsNullExpression)whereClause;
            Column column = (Column)isNullExpression.getLeftExpression();
            String columnName = column.getColumnName();
            FilterOperator filterOperator = isNullExpression.isNot() ? FilterOperator.NOT_EQUAL : FilterOperator.EQUAL;
            return new Query.FilterPredicate(columnName, filterOperator, null);
        }

        if (whereClause instanceof OrExpression){
            OrExpression orExpression = (OrExpression)whereClause;
            Query.Filter left = buildFilterFromWhereClause(orExpression.getLeftExpression(), params);
            Query.Filter right = buildFilterFromWhereClause(orExpression.getRightExpression(), params);
            return CompositeFilterOperator.or(left, right);
        }
        
        if (whereClause instanceof AndExpression){
            AndExpression andExpression = (AndExpression)whereClause;
            Query.Filter left = buildFilterFromWhereClause(andExpression.getLeftExpression(), params);
            Query.Filter right = buildFilterFromWhereClause(andExpression.getRightExpression(), params);
            if (left == null){      // WHERE 1=1 AND COLUMN_1 = 2
                return right;
            }
            if (right == null){     // WHERE COLUMN_1 = 2 AND 1=1
                return left;
            }
            return CompositeFilterOperator.and(left, right);
        }
        
        // TODO
        notImplemented(whereClause);
        return null;
    }

    private static void notImplemented(){
        RuntimeException e = new UnsupportedOperationException("Utils: Not implemented");
        e.printStackTrace();
        throw e;
    }
    private static void notImplemented(Object obj){
        RuntimeException e = new UnsupportedOperationException(obj.toString());
        e.printStackTrace();
        throw e;
    }
}
