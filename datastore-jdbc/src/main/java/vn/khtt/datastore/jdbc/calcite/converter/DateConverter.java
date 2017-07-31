package vn.khtt.datastore.jdbc.calcite.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateConverter implements Converter<java.sql.Date, java.util.Date> {
    private static final DateFormat YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    public DateConverter() {
    }

    @Override
    public java.sql.Date loadValue(Object value) {
        // Try to convert String to Date
        if (value instanceof String){
            try {
                value = YYYYMMDD_HHMMSS.parse((String) value);
            } catch (ParseException e) {
            }
        }
        if (value instanceof String){
            try {
                value = YYYYMMDD.parse((String) value);
            } catch (ParseException e) {
            }
        }

        if (value instanceof java.util.Date){
            java.util.Date date = (java.util.Date)value;
            return new java.sql.Date(date.getTime());
        }

        throw new RuntimeException("Could not convert " + value + " to java.sql.Date");
    }

    @Override
    public java.util.Date saveValue(Object value) {
        // Try to convert String to Date
        if (value instanceof String){
            try {
                value = YYYYMMDD_HHMMSS.parse((String) value);
            } catch (ParseException e) {
            }
        }
        if (value instanceof String){
            try {
                value = YYYYMMDD.parse((String) value);
            } catch (ParseException e) {
            }
        }

        if (value instanceof java.util.Date){
            return (java.util.Date)value;
        }

        if (value instanceof java.sql.Date){
            java.sql.Date d = (java.sql.Date)value;
            return new java.util.Date(d.getTime());
        }
        
        throw new RuntimeException("Could not convert " + value + " to java.util.Date");
    }
}
