package vn.khtt.datastore.jdbc.calcite.converter;

public class DoubleConverter implements Converter<Double, Double> {
    public DoubleConverter() {
    }

    @Override
    public Double loadValue(Object value) {
        return convert(value);
    }

    @Override
    public Double saveValue(Object value) {
        return convert(value);
    }

    private Double convert(Object value) {
        if (value instanceof Number){
            return ((Number)value).doubleValue();
        } else {
            return Double.parseDouble(value.toString());
        }
    }
}
