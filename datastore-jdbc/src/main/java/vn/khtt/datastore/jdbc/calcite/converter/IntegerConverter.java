package vn.khtt.datastore.jdbc.calcite.converter;

public class IntegerConverter implements Converter<Integer, Integer> {
    public IntegerConverter() {
    }

    @Override
    public Integer loadValue(Object value) {
        return convert(value);
    }

    @Override
    public Integer saveValue(Object value) {
        return convert(value);
    }
    
    private Integer convert(Object value) {
        if (value instanceof Number){
            return ((Number)value).intValue();
        } else {
            return Integer.parseInt(value.toString());
        }
    }
}
