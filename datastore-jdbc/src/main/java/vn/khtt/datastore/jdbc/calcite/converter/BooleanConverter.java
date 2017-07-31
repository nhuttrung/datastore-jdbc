package vn.khtt.datastore.jdbc.calcite.converter;

public class BooleanConverter implements Converter<Boolean, Boolean> {
    public BooleanConverter() {
    }

    @Override
    public Boolean loadValue(Object value) {
        return convert(value);
    }

    @Override
    public Boolean saveValue(Object value) {
        return convert(value);
    }

    private Boolean convert(Object value) {
        if (value instanceof Boolean){
            return (Boolean)value;
        } else {
            String tmp = value.toString();
            return "true".equalsIgnoreCase(tmp) || "Y".equalsIgnoreCase(tmp) || "1".equalsIgnoreCase(tmp);
        }
    }
}
