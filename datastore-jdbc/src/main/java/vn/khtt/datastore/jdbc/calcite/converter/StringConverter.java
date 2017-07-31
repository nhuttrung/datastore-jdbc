package vn.khtt.datastore.jdbc.calcite.converter;

public class StringConverter implements Converter<String, String> {
    public StringConverter() {
    }

    @Override
    public String loadValue(Object value) {
        return convert(value);
    }

    @Override
    public String saveValue(Object value) {
        return convert(value);
    }
    
    private String convert(Object value) {
        return value.toString();
    }
}
