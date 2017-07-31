package vn.khtt.datastore.jdbc.calcite.converter;

/**
 * @see com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory
 * @param <D> Pojo type
 * @param <P> Datastore type
 */
public interface Converter <P, D> {
    /**
     * Convert datastore Entity's property value to Pojo's property
     * @param value Property value to be converted.
     * @return The converted value which should be stored in the Pojo
     */
    P loadValue(Object value);
    
    /**
     * Convert Pojo's property value to datastore Entity's property
     * @param value Property value to be converted.
     * @return The converted value which should be stored in the datastore Entity
     */
    D saveValue(Object value);
}
