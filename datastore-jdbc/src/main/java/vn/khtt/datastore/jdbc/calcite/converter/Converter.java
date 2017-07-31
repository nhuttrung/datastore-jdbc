package vn.khtt.datastore.jdbc.calcite.converter;

/**
 * @see com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory
 * @param <D> Pojo type
 * @param <P> Datastore type
 */
public interface Converter <P, D> {
    /**
     * Convert Datastore type to Pojo type
     * @param value
     * @return
     */
    P loadValue(Object value);
    
    /**
     * Convert Pojo type to Datastore type
     * @param value
     * @return
     */
    D saveValue(Object value);
}
