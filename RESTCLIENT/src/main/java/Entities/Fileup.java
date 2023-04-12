package Entities;

public class Fileup {
    private String file;
    private String fileName;
    private String hashtag;
    private String usernameConsumer;
    private String nameConsumer;
    private String emailConsumer;


    public Fileup() {
    }

    public Fileup(String usernameConsumer, String nameConsumer, String emailConsumer, String file, String fileName, String hashtag) {
        this.file = file;
        this.fileName = fileName;
        this.hashtag = hashtag;
        this.usernameConsumer = usernameConsumer;
        this.nameConsumer = nameConsumer;
        this.emailConsumer = emailConsumer;
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
}