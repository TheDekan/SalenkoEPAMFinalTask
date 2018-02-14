package ua.nure.salenko.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

@WebListener
public class Listener implements ServletContextListener {
    
    private static final Logger LOGGER = Logger.getLogger(Listener.class);

    public void contextDestroyed(ServletContextEvent arg0) {
        LOGGER.info("Listener#contextDestroyed");
    }

    public void contextInitialized(ServletContextEvent arg0) {
        LOGGER.info("Listener#contextInitialized");
    }

}
