package com.units.it.entities;

public class FileProxy {

    private String id;
    private String name;
    private String usernameUpl;
    private String usernameCons;
    private String dataUpload;
    private String dataView;
    private String indIp;
    private String hashtag;

    public FileProxy() {
    }

    public FileProxy(File file) {
        this.id = file.getId();
        this.usernameUpl = file.getUsernameUpl();
        this.usernameCons = file.getUsernameCons();
        this.name = file.getName();
        this.dataUpload = file.getDataUpload();
        this.dataView = file.getDataView();
        this.indIp = file.getIP();
        this.hashtag = file.getHashtag();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsernameUpl() {
        return usernameUpl;
    }

    public void setUsernameUpl(String usernameUpl) {
        this.usernameUpl = usernameUpl;
    }

    public String getUsernameCons() {
        return usernameCons;
    }

    public void setUsernameCons(String usernameCons) {
        this.usernameCons = usernameCons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(String dataUpload) {
        this.dataUpload = dataUpload;
    }

    public String getDataView() {
        return dataView;
    }

    public void setDataView(String dataView) {
        this.dataView = dataView;
    }

    public String getIndIp() {
        return indIp;
    }

    public void setIndIp(String indIp) {
        this.indIp = indIp;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}

