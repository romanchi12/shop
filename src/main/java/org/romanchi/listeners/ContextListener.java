package org.romanchi.listeners;

import com.google.common.base.Predicate;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.romanchi.Application;
import org.romanchi.ApplicationContext;
import org.romanchi.DependencyInjectionConfiguration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

public class ContextListener implements ServletContextListener {
    public final static Logger logger = Logger.getLogger(ContextListener.class.getName());
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /*ServletRegistration servletRegistration = servletContextEvent.getServletContext().getServletRegistrations().get("DispatcherServlet");*/
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
