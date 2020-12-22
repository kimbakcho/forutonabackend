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
        basePackages = "com.wing.forutona.Manager",
        entityManagerFactoryRef = "managerEntityManagerFactory",
        transactionManagerRef = "managerTransactionManager"
)
public class ManagerJPAConfigure {

    final CustomPreference customPreference;

    @Bean
    public LocalContainerEntityManagerFactoryBean managerEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(managerDataSource());
        em.setPackagesToScan(new String[]{"com.wing.forutona.Manager"});
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(customPreference.getManagerJpaProperties());
        em.setPersistenceUnitName("manager");
        return em;
    }

    @Bean
    public DataSource managerDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(customPreference.getManagerDataSourceUserName());
        dataSource.setPassword(customPreference.getManagerDataSourcePassword());
        dataSource.setUrl(customPreference.getManagerDataSourceUrl());
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager managerTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(managerEntityManagerFactory().getObject());
        return transactionManager;
    }
}
