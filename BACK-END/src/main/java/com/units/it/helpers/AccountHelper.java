package com.units.it.helpers;

import com.units.it.entities.Account;
import com.units.it.utils.DevException;
import com.units.it.utils.PasswordSaltHashCheck;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AccountHelper extends AbstractHelper {
    public static void saveDelayed(Account account, boolean newPassword) throws DevException {
        if (newPassword) {
            setSaltAndPassword(account);
        }
        ofy().save().entity(account);
    }

    public static void saveNow(Account account, boolean newPassword) throws DevException {
        if (newPassword) {
            setSaltAndPassword(account);
        }
        ofy().save().entity(account).now();
    }

    private static void setSaltAndPassword(Account account) throws DevException {
        String salt = PasswordSaltHashCheck.generateSalt();
        account.setSalt(salt);
        String password = account.getPassword();
        String passwordHashed = PasswordSaltHashCheck.hashPassword(password, salt);
        if (passwordHashed == null) {
            throw new DevException("Hashing Error");
        }
        account.setPassword(passwordHashed);
    }

    public static List<Account> accountListByRole(String role) {
        return ofy().load().type(Account.class).filter("role", role).list();
    }
}
