package com.units.it.utils;

import com.units.it.helpers.AccountHelper;
import com.units.it.security.Jwt;
import com.units.it.security.Token;
import com.units.it.entities.Account;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Misc {

    public static boolean isSyntaxUsernameWrong(String username, String role) {
        Pattern pattern;
        switch (role) {
            case Consts.ADMINISTRATOR:
                pattern = Pattern.compile("^[a-zA-Z0-9.]+@[a-z]+\\.[a-z]+$");
                break;
            case Consts.UPLOADER:
                pattern = Pattern.compile("^[a-zA-Z0-9]{4}$");
                break;
            case Consts.CONSUMER:
                pattern = Pattern.compile("^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$");
                break;
            default:
                return true;
        }
        Matcher matcher = pattern.matcher(username);
        return !matcher.matches();
    }

    public static String getDataString() {
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(nowDate);
    }

    public static void checkModifyPermissions(String token, String role) throws DevException {
        switch (Jwt.getRoleFromJWT(token)) {
            //CONSUMER can't modify other accountS
            case Consts.CONSUMER:
                throw new DevException("Not allowed operation (C)");
                //UPLOADER can modify consumer accounts
            case Consts.UPLOADER:
                if (!role.equals(Consts.CONSUMER))
                    throw new DevException("Not allowed operation (U)");
                break;
            //ADMINISTRATOR can modify uploader accounts
            case Consts.ADMINISTRATOR:
                if (role.equals(Consts.CONSUMER))
                    throw new DevException("Not allowed operation (A)");
                break;
            default:
                throw new DevException("missing role");
        }
    }

    public static void printData(Account account, String action) {
        System.out.println("\n" + action + "-----------");
        System.out.println("ACCOUNT DATA-----------");
        System.out.println(" - Username: " + account.getUsername());
        System.out.println(" - Password: " + account.getPassword());
        System.out.println(" - Name: " + account.getName());
        System.out.println(" - E-mail: " + account.getEmail());
        System.out.println(" - Role: " + account.getRole());
        System.out.println(" - Logo: " + account.getLogo());
        System.out.println("END ACCOUNT DATA-------");
    }

    //https://cloud.google.com/appengine/docs/standard/java/mail/sending-mail-with-mail-api
    //https://github.com/GoogleCloudPlatform/java-docs-samples/blob/2e5996c68440134a79f1511c57529fa5cf987628/appengine-java8/mail/src/main/java/com/example/appengine/mail/MailServlet.java

    public static String sendMail(String mailAddressFrom, String nameFrom, String mailAddressTo, String nameTo, String subject, String mailText, String response) throws DevException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mailAddressFrom, nameFrom));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(mailAddressTo, nameTo));
            msg.setSubject(subject);
            msg.setText(mailText);
            Transport.send(msg);

            return response;

        } catch (AddressException e) {
            if (Consts.debug) {
                System.out.println("Wrong formatted address");
            }
            throw new DevException("Wrong Formatted address");

        } catch (MessagingException e) {
            if (Consts.debug) {
                System.out.println("Problem while sending message");
            }
            throw new DevException("Problem while sending message");

        } catch (UnsupportedEncodingException e) {
            if (Consts.debug) {
                System.out.println("Character encoding is not supported");
            }
            throw new DevException("Character encoding not supported");
        }
    }

    public static String sendMailNewAccount(Account newAccount, String temporaryPassword, String usernameUploader) throws DevException, NullPointerException {

        //Check if the uploader is present in the db
        Account accountUploader = AccountHelper.getById(Account.class, usernameUploader);

        //unchecked exception do not need try and catch block
        if (accountUploader == null) {
            throw new NullPointerException("Unexisting uploader");
        }


        return sendMail(usernameUploader + "@programmazioneweb2020-319008.appspotmail.com", accountUploader.getName(), newAccount.getEmail(), newAccount.getName(),
                "Welcome new Consumer!! \n ",
                "Thank you " + newAccount.getName() + " !! \n " +
                        "You have been added to our sharing platform with the following account: \n" +
                        "USERNAME: " + newAccount.getUsername() + "\n" +
                        "PASSWORD: " + temporaryPassword + "\n" +
                        "E-MAIL: " + newAccount.getEmail() + "\n" +
                        "\n Start now and join us : " + Consts.HOMEPAGE + "\n" +
                        "\n\n\n\n This is an automatic email do not reply",
                "New Consumer " + newAccount.getUsername() + " registered. \n"
        );
    }

    public static String sendNotice(ReportUploader ReportUploader, String usernameUpl, String fileId, String fileName, String hashtag) throws DevException, NullPointerException {

        String linkDownload = Consts.PATH_FOR_DIRECT_DOWNLOAD + "/" + fileId + "/" + Token.generateToken(fileId);

        if (Consts.debug) {
            System.out.println(linkDownload);
        }

        //Check if the account who upload the file is present in our db
        Account accountUploader = AccountHelper.getById(Account.class, usernameUpl);

        //unchecked exception do not need try and catch block
        if (accountUploader == null) {
            throw new NullPointerException("Uploader not found");
        }

        return sendMail(usernameUpl + "@programmazioneweb2020-319008.appspotmail.com", accountUploader.getName(), ReportUploader.getEmailConsumer(), ReportUploader.getNameConsumer(),
                "New file loaded",
                "The file  " + fileName + "\n"+
                        "with hashtag: " + hashtag + "\n"+
                        "IS ADDED FOR YOU! \n"+
                        "for direct download: " + linkDownload +
                        "\n\n\nAUTOMATIC E-MAIL do not reply",
                "Notification sent successfully"
        );
    }

}
