package com.wing.forutona.Preferance;

import java.util.Properties;

public class RealTestPreference implements CustomPreference{
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
//        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        jpaProperties.put("hibernate.dialect", "com.wing.forutona.CustomDialect");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.format_sql", "true");
        jpaProperties.put("hibernate.default_batch_fetch_size", "1000");
        jpaProperties.put("hibernate.use_sql_comment", "true");
        return jpaProperties;
    }
}