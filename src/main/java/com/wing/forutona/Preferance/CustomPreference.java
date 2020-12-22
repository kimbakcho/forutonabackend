package com.wing.forutona.Preferance;

import java.util.Properties;

public interface CustomPreference {
    String getAppDBUserName();
    String getAppDBPassword();
    String getAppDBUrl();
    Properties getAppJpaProperties();

    Properties getManagerJpaProperties();

    String getManagerDataSourceUserName();

    String getManagerDataSourcePassword();

    String getManagerDataSourceUrl();
}
