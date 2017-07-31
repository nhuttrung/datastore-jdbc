package vn.khtt.datastore.jdbc.calcite.converter;

public class ByteConverter implements Converter<Byte, Byte> {
    public ByteConverter() {
        super();
    }

    @Override
    public Byte loadValue(Object value) {
        return convert(value);
    }

    @Override
    public Byte saveValue(Object value) {
        return convert(value);
    }

    private Byte convert(Object value) {
        if (value instanceof Number){
            return ((Number)value).byteValue();
        } else {
            return Byte.parseByte(value.toString());
        }
    }
}
