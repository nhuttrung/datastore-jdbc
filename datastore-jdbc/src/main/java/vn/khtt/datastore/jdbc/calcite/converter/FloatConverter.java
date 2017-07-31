package vn.khtt.datastore.jdbc.calcite.converter;

public class FloatConverter implements Converter<Float, Float> {
    public FloatConverter() {
    }

    @Override
    public Float loadValue(Object value) {
        return convert(value);
    }

    @Override
    public Float saveValue(Object value) {
        return convert(value);
    }

    private Float convert(Object value) {
        if (value instanceof Number){
            return ((Number)value).floatValue();
        } else {
            return Float.parseFloat(value.toString());
        }
    }
}
