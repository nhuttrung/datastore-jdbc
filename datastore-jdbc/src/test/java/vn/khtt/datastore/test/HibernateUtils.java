package vn.khtt.datastore.test;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.schema.Action;
import org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

public class HibernateUtils {
    public static void autoCreateSchema(DataSource dataSource, Package... packagesToScan){
        String[] packages = new String[packagesToScan.length];
        for (int i=0; i<packagesToScan.length; i++){
            packages[i] = packagesToScan[i].getName();
        }
        
        autoCreateSchema(dataSource, packages);
    }
    public static void autoCreateSchema(DataSource dataSource, String... packagesToScan){
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
            .applySetting(AvailableSettings.DATASOURCE, dataSource)
            .applySetting(AvailableSettings.DIALECT, vn.khtt.datastore.jdbc.Dialect.class)
        ;

        ServiceRegistry serviceRegistry = registryBuilder.build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        
        // Scan for Entity classes
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        new LocalSessionFactoryBuilder(dataSource, resourceLoader, metadataSources).scanPackages(packagesToScan);
        
        Metadata metadata = metadataSources.getMetadataBuilder().build();
        Map<String, Object> properties = new HashMap<String, Object>();
//        properties.putAll(serviceRegistry.getService(ConfigurationService.class).getSettings());
        properties.put(AvailableSettings.HBM2DDL_AUTO, Action.CREATE);
        
        SchemaManagementToolCoordinator.process(metadata, serviceRegistry, properties, null);
    }
}
