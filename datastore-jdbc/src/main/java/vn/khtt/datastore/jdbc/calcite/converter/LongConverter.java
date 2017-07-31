package vn.khtt.datastore.jdbc.calcite.converter;

public class LongConverter implements Converter<Long, Long> {
    public LongConverter() {
    }

    @Override
    public Long loadValue(Object value) {
        return convert(value);
    }

    @Override
    public Long saveValue(Object value) {
        return convert(value);
    }

    private Long convert(Object value) {
        if (value instanceof Number){
            return ((Number)value).longValue();
        } else {
            return Long.parseLong(value.toString());
        }
    }
}
