package com.wing.forutona;

import com.wing.forutona.Preferance.CustomPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = "com.wing.forutona.App",
        entityManagerFactoryRef = "forutonaAppEntityManagerFactory",
        transactionManagerRef = "forutonaAppTransactionManager"
)
public class ForutonaAppJPAConfig {

    final CustomPreference customPreference;

    @Bean
    @Primary
    public DataSource forutonaAppDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(customPreference.getAppDBUserName());
        dataSource.setPassword(customPreference.getAppDBPassword());
        dataSource.setUrl(customPreference.getAppDBUrl());
        return dataSource;
    }


    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean forutonaAppEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(forutonaAppDataSource());
        em.setPackagesToScan(new String[]{"com.wing.forutona.App"});
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(customPreference.getAppJpaProperties());
        em.setPersistenceUnitName("forutonaApp");
        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager forutonaAppTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(forutonaAppEntityManagerFactory().getObject());
        return transactionManager;
    }
}
