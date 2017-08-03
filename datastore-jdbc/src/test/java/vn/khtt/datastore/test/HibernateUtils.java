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
    public static void autoCreateSchema(DataSource dataSource, Package... scanPackages){
        String[] packages = new String[scanPackages.length];
        for (int i=0; i<scanPackages.length; i++){
            packages[i] = scanPackages[i].getName();
        }
        
        autoCreateSchema(dataSource, packages);
    }
    public static void autoCreateSchema(DataSource dataSource, String... scanPackages){
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
            .applySetting(AvailableSettings.DATASOURCE, dataSource)
            .applySetting(AvailableSettings.DIALECT, vn.khtt.datastore.jdbc.Dialect.class)
        ;

        ServiceRegistry serviceRegistry = registryBuilder.build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        
        // Scan for Entity classes
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        new LocalSessionFactoryBuilder(null, resourceLoader, metadataSources).scanPackages(scanPackages);
        
        Metadata metadata = metadataSources.getMetadataBuilder().build();
        Map<String, Object> properties = new HashMap<String, Object>();
//        properties.putAll(serviceRegistry.getService(ConfigurationService.class).getSettings());
        properties.put(AvailableSettings.HBM2DDL_AUTO, Action.CREATE);
        
        SchemaManagementToolCoordinator.process(metadata, serviceRegistry, properties, null);
    }
}
