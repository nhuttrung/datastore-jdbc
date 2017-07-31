package vn.khtt.datastore.jdbc.calcite.converter;

import java.math.BigDecimal;

public class BigDecimalLongConverter implements Converter<BigDecimal, Long> {
    public static final long DEFAULT_FACTOR = 1000;
    
    com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory x;
    private BigDecimal factor;

    public BigDecimalLongConverter() {
        this(DEFAULT_FACTOR);
    }
    public BigDecimalLongConverter(long factor) {
        this.factor = new BigDecimal(factor);
    }

    @Override
    public BigDecimal loadValue(Object value) {
        long longValue = (Long)value;
        return new BigDecimal(longValue).divide(factor);
    }

    @Override
    public Long saveValue(Object value) {
        if (value instanceof String){
            value = Double.parseDouble((String)value);
        }
        
        // Try to convert to BigDecimal
        if (value instanceof Double){
            value = new BigDecimal((Double)value);
        }
        if (value instanceof Float){
            value = new BigDecimal((Float)value);
        }
        if (value instanceof Long){
            value = new BigDecimal((Long)value);
        }
        if (value instanceof Integer){
            value = new BigDecimal((Integer)value);
        }
        if (value instanceof Number){
            value = new BigDecimal(((Number)value).longValue());
        }
        
        if (value instanceof BigDecimal){
            BigDecimal bigDecimal = (BigDecimal)value;
            return bigDecimal.multiply(factor).longValueExact();
        }
        
        throw new RuntimeException("Could not convert " + value + " to long");
    }
}
