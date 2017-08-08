package vn.khtt.datastore.jdbc.calcite;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.khtt.datastore.jdbc.calcite.converter.BigDecimalLongConverter;
import vn.khtt.datastore.jdbc.calcite.converter.BlobConverter;
import vn.khtt.datastore.jdbc.calcite.converter.BooleanConverter;
import vn.khtt.datastore.jdbc.calcite.converter.ByteConverter;
import vn.khtt.datastore.jdbc.calcite.converter.Converter;
import vn.khtt.datastore.jdbc.calcite.converter.DateConverter;
import vn.khtt.datastore.jdbc.calcite.converter.DoubleConverter;
import vn.khtt.datastore.jdbc.calcite.converter.FloatConverter;
import vn.khtt.datastore.jdbc.calcite.converter.IntegerConverter;
import vn.khtt.datastore.jdbc.calcite.converter.LongConverter;
import vn.khtt.datastore.jdbc.calcite.converter.ShortConverter;
import vn.khtt.datastore.jdbc.calcite.converter.StringConverter;
import vn.khtt.datastore.jdbc.calcite.converter.TimestampConverter;

public class DataTypeConverter {
    private static Map<DatastoreFieldType, Converter> converters = new HashMap<DatastoreFieldType, Converter>();
    static{
        converters.put(DatastoreFieldType.BOOL, new BooleanConverter());
        converters.put(DatastoreFieldType.BOOLEAN, new BooleanConverter());
        converters.put(DatastoreFieldType.BIT, new BooleanConverter());

        converters.put(DatastoreFieldType.BYTE, new ByteConverter());
        converters.put(DatastoreFieldType.TINYINT, new ByteConverter());

        converters.put(DatastoreFieldType.SMALLINT, new ShortConverter());

        converters.put(DatastoreFieldType.INT, new IntegerConverter());
        converters.put(DatastoreFieldType.INTEGER, new IntegerConverter());

        converters.put(DatastoreFieldType.FLOAT, new FloatConverter());
        converters.put(DatastoreFieldType.DOUBLE, new DoubleConverter());
        converters.put(DatastoreFieldType.REAL, new DoubleConverter());

        converters.put(DatastoreFieldType.BIGINT, new LongConverter());

        converters.put(DatastoreFieldType.CHAR, new StringConverter());
        converters.put(DatastoreFieldType.VARCHAR, new StringConverter());
        converters.put(DatastoreFieldType.VARCHAR2, new StringConverter());

        converters.put(DatastoreFieldType.NUMBER, new BigDecimalLongConverter());
        converters.put(DatastoreFieldType.NUMERIC, new BigDecimalLongConverter());

        converters.put(DatastoreFieldType.TIMESTAMP, new TimestampConverter());
        converters.put(DatastoreFieldType.DATETIME, new TimestampConverter());
        converters.put(DatastoreFieldType.DATE, new DateConverter());

        converters.put(DatastoreFieldType.BLOB, new BlobConverter());
    }
    
    /**
     * Convert object read from Datastore to JDBC
     * @param value The value to be converted
     * @param fieldType Property type
     * @return The converted value
     */
    public static Object convertToJdbc(Object value, DatastoreFieldType fieldType){
        if (value == null) {
            return value;
        }
        if (fieldType == null) {
            throw new RuntimeException("Field type is null");
        }

        Converter converter = converters.get(fieldType);
        if (converter == null){
            throw new RuntimeException("No converter for " + fieldType);
        }
        return converter.loadValue(value);
    }

    /**
     * Convert Java object to suitable for storing in datastore Entity
     * @param value The value to be converted
     * @param datastoreTable Table information
     * @param columnName Column name
     * @return The converted value which should be stored in the datastore Entity
     */
    public static Object convertToDatastore(Object value, DatastoreTable datastoreTable, String columnName){
        if (value == null) {
            return value;
        }

        for (DatastoreField field : datastoreTable.getFields()){
            if (columnName.equals(field.getFieldName())){
                return convertToDatastore(value, field.getFieldType());
            }
        }
        throw new RuntimeException("No such field " + columnName);
    }
    public static Object convertToDatastore(Object value, DatastoreFieldType fieldType){
        if (value == null) {
            return value;
        }
        if (fieldType == null) {
            throw new RuntimeException("Field type is null");
        }

        Converter converter = converters.get(fieldType);
        if (converter == null){
            throw new RuntimeException("No converter for " + fieldType);
        }
        return converter.saveValue(value);
    }

    private static Object convertCommon(Object value, DatastoreFieldType fieldType){
        if (value == null) {
            return value;
        }

        switch (fieldType) {
        case BOOLEAN:
        case BOOL:
        case BIT:
            if (value instanceof Boolean){
                return value;
            } else {
                String tmp = value.toString();
                return "true".equalsIgnoreCase(tmp) || "Y".equalsIgnoreCase(tmp) || "1".equalsIgnoreCase(tmp);
            }
        case INT:
        case INTEGER:
            if (value instanceof Number){
                return ((Number)value).intValue();
            } else {
                return Integer.parseInt(value.toString());
            }
        case CHAR:
        case VARCHAR:
        case VARCHAR2:
            return value.toString();
        default:
            // return obj;
            throw new UnsupportedOperationException("convert: " + value.getClass() + " to type " + fieldType);
        }
    }
}
