package com.units.it.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Base64;
import java.util.UUID;

@Entity
public class File {
    @Id
    private String id;

    @Index
    private String usernameUpl;

    @Index
    private String usernameCons;

    private String name;
    private byte[] file;
    private String dataUpload;
    private String dataView;
    private String indIP;
    private String hashtag;

    public File() {
    }

    public File(String usernameUpl, String usernameCons, String file, String name, String dataUpload, String hashtag) {
        this.id = UUID.randomUUID().toString();
        this.usernameUpl = usernameUpl;
        this.usernameCons = usernameCons;
        this.file = Base64.getDecoder().decode(file);
        this.name = name;
        this.dataUpload = dataUpload;
        this.hashtag = hashtag;
        this.dataView = "";
        this.indIP = "";
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

    public void setUsernameUpl(String username) {
        this.usernameUpl = username;
    }

    public String getUsernameCons() {
        return usernameCons;
    }

    public void setUsernameCons(String username) {
        this.usernameCons = username;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
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

    public String getIP() {
        return indIP;
    }

    public void setIP(String indIP) {
        this.indIP = indIP;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}
