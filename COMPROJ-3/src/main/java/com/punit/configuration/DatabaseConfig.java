package com.punit.configuration;

import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class DatabaseConfig {
    
    Logger cloudFoundryDataSourceConfigLogger = LoggerFactory.getLogger(this.getClass());
    
    // ✅ FIX: Use default empty string if property not found
    @Value("${vcap.services.sql.credentials.username:}")
    private String username;
    
    @Value("${vcap.services.sql.credentials.password:}")
    private String password;
    
    @Value("${vcap.services.sql.credentials.hostname:}")
    private String hostname;
    
    @Value("${vcap.services.sql.credentials.port:}")
    private String port;
    
    @Value("${vcap.services.sql.credentials.dbname:}")
    private String dbname;
    
    @Bean
    public DataSource dataSource() {
        cloudFoundryDataSourceConfigLogger.info("========================================");
        cloudFoundryDataSourceConfigLogger.info("🔍 DATABASE CONFIG FROM VCAP_SERVICES");
        cloudFoundryDataSourceConfigLogger.info("========================================");
        cloudFoundryDataSourceConfigLogger.info("Hostname: " + hostname);
        cloudFoundryDataSourceConfigLogger.info("Port: " + port);
        cloudFoundryDataSourceConfigLogger.info("Database: " + dbname);
        cloudFoundryDataSourceConfigLogger.info("Username: " + username);
        cloudFoundryDataSourceConfigLogger.info("========================================");
        
        // ✅ CRITICAL: Check if credentials are actually loaded
        if (hostname == null || hostname.trim().isEmpty()) {
            String errorMsg = "❌ ERROR: VCAP_SERVICES not loaded!\n"
                + "Cloud Foundry did not inject database credentials.\n"
                + "Possible causes:\n"
                + "1. Service 'sql' is not bound to app (check: cf services)\n"
                + "2. Service binding name is wrong in manifest.yml\n"
                + "3. Service not created yet\n"
                + "Run: cf bind-service report_app sql\n"
                + "Then: cf restart report_app";
            cloudFoundryDataSourceConfigLogger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // ✅ Build JDBC URL with SSL mode
        String url = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbname + "?sslmode=require";
        cloudFoundryDataSourceConfigLogger.info("✅ JDBC URL: " + url);
        
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        
        cloudFoundryDataSourceConfigLogger.info("✅ DataSource created successfully!");
        
        return dataSource;
    }
    
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.punit");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.show_sql", "false");
        props.setProperty("hibernate.format_sql", "true");
        props.setProperty("hibernate.jdbc.batch_size", "20");
        props.setProperty("hibernate.order_inserts", "true");
        props.setProperty("hibernate.order_updates", "true");
        
        em.setJpaProperties(props);
        return em;
    }
    
    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}