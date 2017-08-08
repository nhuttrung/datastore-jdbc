package vn.khtt.datastore.jdbc.calcite;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.tree.Primitive;
import org.apache.calcite.rel.type.RelDataType;

public enum DatastoreFieldType {
    // BOOLEAN type
    BOOLEAN(Primitive.BOOLEAN),
    BOOL(Boolean.class, "BOOL"),
    BIT(Boolean.class, "BIT"),

    // INT type
    INT(Primitive.INT),
    INTEGER(Integer.class, "INTEGER"),

    // TINYINT type
    BYTE(Primitive.BYTE),
    TINYINT(Byte.class, "TINYINT"),

    // SMALLINT type
    SMALLINT(Short.class, "SMALLINT"),

    // BIGINT type
    BIGINT(Long.class, "BIGINT"),

    // REAL type
    FLOAT(Primitive.FLOAT),
    DOUBLE(Primitive.DOUBLE),
    REAL(Double.class, "REAL"),

    // DATE TIME
    DATE(java.sql.Date.class, "date"),
    TIME(java.sql.Time.class, "time"),
    TIMESTAMP(java.sql.Timestamp.class, "timestamp"),
    DATETIME(java.sql.Timestamp.class, "DATETIME"),

    // VARCHAR types
    CHAR(String.class, "char"),
    VARCHAR(String.class, "varchar"),
    VARCHAR2(String.class, "VARCHAR2"),

    // Others
    NUMBER(BigDecimal.class, "number"),
    NUMERIC(BigDecimal.class, "NUMERIC"),

    BLOB(byte[].class, "BLOB"),
//TODO    LONGTEXT(String.class, "LONGTEXT"),
    ;

    private static final Map<String, DatastoreFieldType> MAP = new HashMap<String, DatastoreFieldType>();
    static {
        for (DatastoreFieldType value : values()) {
            MAP.put(value.simpleName.toUpperCase(), value);
        }
    }

    private final Class clazz;
    private final String simpleName;

    DatastoreFieldType(Primitive primitive) {
        this(primitive.boxClass, primitive.primitiveClass.getSimpleName());
    }
    DatastoreFieldType(Class clazz, String simpleName) {
        this.clazz = clazz;
        this.simpleName = simpleName;
    }

    public RelDataType toType(JavaTypeFactory typeFactory) {
        RelDataType javaType = typeFactory.createJavaType(clazz);
        RelDataType sqlType = typeFactory.createSqlType(javaType.getSqlTypeName());
        return typeFactory.createTypeWithNullability(sqlType, true);
    }

    public static DatastoreFieldType of(String typeString) {
        return MAP.get(typeString.toUpperCase());
    }
}
