package com.wing.forutona.Preferance;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Profile("local")
@Component
public class LocalPreference implements CustomPreference{

    @Override
    public String getAppDBUserName() {
        return "neoforutonatester";
    }

    @Override
    public String getAppDBPassword() {
        return "forutona1020";
    }

    @Override
    public String getAppDBUrl() {
        return "jdbc:mysql://forutonadb.thkomeet.com:3306/forutona_test?serverTimezone=Asia/Seoul&useSSL=yes&verifyServerCertificate=false";
    }

    @Override
    public Properties getAppJpaProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "com.wing.forutona.CustomDialect");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.default_batch_fetch_size", "1000");
        jpaProperties.put("hibernate.format_sql", "true");
        jpaProperties.put("hibernate.use_sql_comment", "true");
        return jpaProperties;
    }

    @Override
    public Properties getManagerJpaProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "com.wing.forutona.CustomDialect");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.format_sql", "true");
        jpaProperties.put("hibernate.default_batch_fetch_size", "1000");
        jpaProperties.put("hibernate.use_sql_comment", "true");
        return jpaProperties;
    }

    @Override
    public String getManagerDataSourceUserName() {
        return  "managementforutona";
    }

    @Override
    public String getManagerDataSourcePassword() {
        return "forutona1020";
    }

    @Override
    public String getManagerDataSourceUrl() {
        return "jdbc:mysql://forutonadb.thkomeet.com:3306/forutonamanager_test?serverTimezone=Asia/Seoul&useSSL=yes&verifyServerCertificate=false";
    }
}
