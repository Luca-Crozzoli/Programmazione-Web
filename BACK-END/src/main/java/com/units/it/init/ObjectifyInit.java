package com.units.it.init;


import com.googlecode.objectify.ObjectifyService;
import com.units.it.entities.Account;
import com.units.it.entities.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ObjectifyInit implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ObjectifyService.register(Account.class);
        ObjectifyService.register(File.class);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
