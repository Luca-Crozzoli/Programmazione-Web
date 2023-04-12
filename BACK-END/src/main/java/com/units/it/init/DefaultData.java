package com.units.it.init;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.units.it.entities.Account;
import com.units.it.helpers.AccountHelper;
import com.units.it.utils.Consts;
import com.units.it.utils.DevException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DefaultData implements ServletContextListener {

    /*
        https://stackoverflow.com/questions/34476401/objectify-context-not-started-objectifyfilter-missing
     */

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ObjectifyService.run(new VoidWork() {
            public void vrun() {
                Account account = new Account("admin@luca", "Password123", "Admin", "crozzoliluca9@gmial.com", Consts.ADMINISTRATOR, "");

                try {
                    AccountHelper.saveNow(account, true);
                } catch (DevException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
