package vn.khtt.datastore.jdbc.calcite.converter;

import com.google.appengine.api.datastore.Blob;

public class BlobConverter implements Converter<byte[], Blob> {
    public BlobConverter() {
        super();
    }

    @Override
    public byte[] loadValue(Object value) {
        if (value instanceof Blob){
            Blob blob = (Blob)value;
            return blob.getBytes();
        }
        
        throw new RuntimeException("Not a BLOB");
    }

    @Override
    public Blob saveValue(Object value) {
        if (value instanceof byte[]){
            byte[] b = (byte[])value;
            return new Blob(b);
        }
        
        throw new RuntimeException("Not a byte[]");
    }
}
