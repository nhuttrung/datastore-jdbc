package vn.khtt.datastore.jdbc;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.io.Serializable;

import java.sql.Types;

import java.util.Iterator;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.Metadata;
import org.hibernate.dialect.unique.DefaultUniqueDelegate;
import org.hibernate.dialect.unique.UniqueDelegate;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory;
import org.hibernate.mapping.UniqueKey;


public class Dialect extends org.hibernate.dialect.Dialect {
    private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    public static class DatastoreIdentifierGenerator implements IdentifierGenerator, Configurable {
        private Type type;
        private Properties params;
        private ServiceRegistry serviceRegistry;
        
        public DatastoreIdentifierGenerator() {
        }

        @Override
        public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
            this.type = type;
            this.params = params;
            this.serviceRegistry = serviceRegistry;
        }

        @Override
        public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
            String kind = params.getProperty(IdentifierGenerator.ENTITY_NAME);
            long autoId = datastore.allocateIds(kind, 1).getStart().getId();
            return autoId;
        }
    }
    
    public Dialect() {
        registerColumnType( Types.VARBINARY, "blob" );
        registerColumnType( Types.LONGVARBINARY, "blob" );
    }

    /**
     * @see DefaultIdentifierGeneratorFactory
     */
    public String getNativeIdentifierGeneratorStrategy() {
        return DatastoreIdentifierGenerator.class.getName();
    }
    
    public UniqueDelegate getUniqueDelegate() {
        return new DefaultUniqueDelegate(this){
            public String getAlterTableToAddUniqueKeyCommand(UniqueKey uniqueKey, Metadata metadata) {
                final JdbcEnvironment jdbcEnvironment = metadata.getDatabase().getJdbcEnvironment();

                final String tableName = jdbcEnvironment.getQualifiedObjectNameFormatter().format(
                        uniqueKey.getTable().getQualifiedTableName(),
                        dialect
                );

                final String constraintName = dialect.quote( uniqueKey.getName() );
                return "alter table " + tableName + " add unique key " + constraintName + " " + uniqueConstraintSql( uniqueKey );
            }

            protected String uniqueConstraintSql(UniqueKey uniqueKey) {
                final StringBuilder sb = new StringBuilder();
                sb.append( "(" );
                final Iterator<org.hibernate.mapping.Column> columnIterator = uniqueKey.columnIterator();
                while ( columnIterator.hasNext() ) {
                    final org.hibernate.mapping.Column column = columnIterator.next();
                    sb.append( column.getQuotedName( dialect ) );
                    if ( uniqueKey.getColumnOrderMap().containsKey( column ) ) {
                        sb.append( " " ).append( uniqueKey.getColumnOrderMap().get( column ) );
                    }
                    if ( columnIterator.hasNext() ) {
                        sb.append( ", " );
                    }
                }

                return sb.append( ')' ).toString();
            }
        };
    }
}
