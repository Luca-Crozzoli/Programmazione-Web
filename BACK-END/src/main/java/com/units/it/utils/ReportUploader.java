package com.units.it.utils;

public class ReportUploader {

    private String file;
    private String fileName;
    private String hashtag;
    private String dataUp;
    private String usernameUpl;
    private String usernameConsumer;
    private String nameConsumer;
    private String emailConsumer;

    public ReportUploader() {
    }

    public ReportUploader(String file, String nameFile, String hashtag, String usernameCons, String nameCons, String emailCons) {
        this.file = file;
        this.fileName = nameFile;
        this.hashtag = hashtag;
        this.dataUp = Misc.getDataString();
        this.usernameUpl = "";
        this.usernameConsumer = usernameCons;
        this.nameConsumer = nameCons;
        this.emailConsumer = emailCons;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getDataUp() {
        return dataUp;
    }

    public void setDataUp(String dataUp) {
        this.dataUp = dataUp;
    }

    public String getUsernameUpl() {
        return usernameUpl;
    }

    public void setUsernameUpl(String usernameUpl) {
        this.usernameUpl = usernameUpl;
    }

    public String getUsernameConsumer() {
        return usernameConsumer;
    }

    public void setUsernameConsumer(String usernameConsumer) {
        this.usernameConsumer = usernameConsumer;
    }

    public String getNameConsumer() {
        return nameConsumer;
    }

    public void setNameConsumer(String nameConsumer) {
        this.nameConsumer = nameConsumer;
    }

    public String getEmailConsumer() {
        return emailConsumer;
    }

    public void setEmailConsumer(String emailConsumer) {
        this.emailConsumer = emailConsumer;
    }
}
