package org.romanchi;

import java.util.logging.Logger;

@Scan(packageToScan = "org.romanchi")
public class DependencyInjectionConfiguration {
    @Bean
    public Logger logger(){
        return Logger.getLogger("logger");
    }
}
