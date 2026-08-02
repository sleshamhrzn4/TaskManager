package com.taskmanager.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;

@WebListener
public class FlywayMigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = Flyway.configure().dataSource(DBConfig.getDataSource())
                .baselineOnMigrate(true).load();

        flyway.migrate();
    }
}