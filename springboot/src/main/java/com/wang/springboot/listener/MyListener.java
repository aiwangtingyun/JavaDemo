package com.wang.springboot.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Web 应用启用...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Web 应用销毁...");
    }
}
