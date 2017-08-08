package vn.khtt.datastore.jdbc;

import java.sql.SQLException;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

public class CaseTransformer {
    static class UpperCaseTransformer extends CaseTransformer {
        protected String transformName(String name){
            if (name == null){
                return null;
            }
            return name.toUpperCase();
        }
    }
    
    public CaseTransformer() {
    }
    
    public String transformSql(String sql) throws SQLException {
        StringBuilder buffer = new StringBuilder();
        
        final ExpressionDeParser expressionDeParser = new ExpressionDeParser(){
            public void visit(Column tableColumn){
                super.visit(tableColumn);
                String columnName = tableColumn.getColumnName();
                columnName = transformName(columnName);
                tableColumn.setColumnName(columnName);
            }
        };
        
        SelectDeParser selectDeParser = new SelectDeParser(expressionDeParser, buffer){
            public void visit(Table table){
                super.visit(table);
                String tableNane = table.getName();
                tableNane = transformName(tableNane);
                table.setName(tableNane);
            }
        };
        
        StatementDeParser statementDeParser =  new StatementDeParser(expressionDeParser, selectDeParser, buffer){
            public void visit(CreateTable createTable) {
                super.visit(createTable);
                
                String tableName = createTable.getTable().getName();
                tableName = transformName(tableName);
                createTable.getTable().setName(tableName);
                
                for (ColumnDefinition columnDefinition : createTable.getColumnDefinitions()){
                    String columnName = columnDefinition.getColumnName();
                    columnName = transformName(columnName);
                    columnDefinition.setColumnName(columnName);
                }
            }
            public void visit(Insert insert) {
                super.visit(insert);
                
                String tableName = insert.getTable().getName();
                tableName = transformName(tableName);
                insert.getTable().setName(tableName);
                
                if (insert.getColumns() != null) {
                    for (Column column : insert.getColumns()){
                        column.accept(expressionDeParser);
                    }
                }
            }
        };
        
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new SQLException("Error while parsing sql: " + sql, e);
        }
        statement.accept(statementDeParser);
        
//        System.out.println("Transform: " + sql);
//        System.out.println("==> " + statement.toString());
        return statement.toString();
    }
    
    protected String transformName(String name){
        return name;
    }
    
    public static void main(String[] args) throws Exception {
        CaseTransformer caseTransformer = new UpperCaseTransformer();
        String sql = "SELECT t.empno FROM emps t WHERE x=1";
        sql = "INSERT INTO EMPS (name, empno) VALUES ('Jackson', 3)";
        sql = "CREATE TABLE EMPS (empno NUMBER, name VARCHAR)";

        sql = caseTransformer.transformSql(sql);
        System.out.println(sql);
    }
}
