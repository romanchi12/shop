package org.romanchi.database;

import org.romanchi.Bean;
import org.romanchi.Wired;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSourceFactory { //AFactory

    @Wired
    static Logger logger;

    private static Context context;

    static{
        try {
            context = new InitialContext();
        } catch (NamingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    private DataSourceFactory(){}

    private static class DataSourceHolder{
        private static DataSource DATA_SOURCE;

        static{
            try {
                DATA_SOURCE = (DataSource) context.lookup("java:/comp/env/jdbc/Epam");
            } catch (NamingException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }

    }
    @Bean
    public static DataSource getDataSource(){ //a
        return DataSourceHolder.DATA_SOURCE;
    }
}
