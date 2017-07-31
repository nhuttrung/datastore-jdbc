package vn.khtt.datastore.jdbc.calcite;

public class DatastoreField {
    private String fieldName;
    private DatastoreFieldType fieldType;
    
    public DatastoreField() {
    }
    public DatastoreField(String fieldName, DatastoreFieldType fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldType(DatastoreFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public DatastoreFieldType getFieldType() {
        return fieldType;
    }
}
