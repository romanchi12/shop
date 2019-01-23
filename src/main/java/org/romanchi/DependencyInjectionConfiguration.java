package org.romanchi;

import java.util.logging.Logger;

@Scan(packageToScan = "org.romanchi")
public class DependencyInjectionConfiguration extends Object {
    @Bean
    public Logger logger(){
        //System.setProperty("java.util.logging.config.file",
         //       "C:\\Users\\Roman\\IdeaProjects\\shop\\src\\main\\webapp\\resources\\logger.properties");
        return Logger.getLogger(DependencyInjectionConfiguration.class.getName());
    }
}
