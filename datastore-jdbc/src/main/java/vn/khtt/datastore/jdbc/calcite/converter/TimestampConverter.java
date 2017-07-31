package vn.khtt.datastore.jdbc.calcite.converter;

import java.sql.Timestamp;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class TimestampConverter implements Converter<Timestamp, Date>{
    private static final DateFormat YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    
    public TimestampConverter() {
    }

    @Override
    public Timestamp loadValue(Object value) {
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
        
        if (value instanceof Date){
            Date date = (Date)value;
            return new Timestamp(date.getTime());
        }

        throw new RuntimeException("Could not convert " + value + " to Timestamp");
    }

    @Override
    public Date saveValue(Object value) {
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
        
        if (value instanceof Date){
            return (Date)value;
        }

        if (value instanceof Timestamp){
            Timestamp t = (Timestamp)value;
            return new Date(t.getTime());
        }
        
        throw new RuntimeException("Could not convert " + value + " to Date");
    }
}
