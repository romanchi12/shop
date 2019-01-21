package org.romanchi.listeners;


import org.romanchi.Application;
import org.romanchi.ApplicationContext;
import org.romanchi.DependencyInjectionConfiguration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.io.IOException;

import java.util.logging.Logger;

public class ContextListener implements ServletContextListener {
    public final static Logger logger = Logger.getLogger(ContextListener.class.getName());
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            Application.run(DependencyInjectionConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ApplicationContext context = Application.getContext();
        System.out.println(context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
