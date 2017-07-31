package vn.khtt.datastore.jdbc.calcite.converter;

public class ShortConverter implements Converter<Short, Short> {
    public ShortConverter() {
    }

    @Override
    public Short loadValue(Object value) {
        return convert(value);
    }

    @Override
    public Short saveValue(Object value) {
        return convert(value);
    }

    private Short convert(Object value) {
        if (value instanceof Number){
            return ((Number)value).shortValue();
        } else {
            return Short.parseShort(value.toString());
        }
    }
}
