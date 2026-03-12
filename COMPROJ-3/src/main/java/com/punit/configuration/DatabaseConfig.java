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

    @Value("${vcap.services.sql.credentials.username}")
    private String username;

    @Value("${vcap.services.sql.credentials.password}")
    private String password;

    @Value("${vcap.services.sql.credentials.hostname}")
    private String hostname;

    @Value("${vcap.services.sql.credentials.port}")
    private String port;

    @Value("${vcap.services.sql.credentials.dbname}")
    private String dbname;

    @Bean
    public DataSource dataSource() {
        cloudFoundryDataSourceConfigLogger.info("Detected Host name is : " + this.hostname);
        cloudFoundryDataSourceConfigLogger.info("Detected Port is : " + this.port);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://" + hostname + ":" + port + "/" + dbname + "?sslmode=require");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
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
        em.setJpaProperties(props);

        return em;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}