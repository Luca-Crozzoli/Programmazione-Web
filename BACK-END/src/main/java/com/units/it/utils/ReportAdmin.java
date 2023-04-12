package com.units.it.utils;


public class ReportAdmin {

    private String username;
    private String name;
    private String email;
    private String logo;
    private int uploadedDocs;
    private int uploaderConsumers;


    public ReportAdmin() {
    }

    public ReportAdmin(String uploader, String nameUploader, String emailUploader, int uploadedDocs, int consumers, String logo) {
        this.username = uploader;
        this.name = nameUploader;
        this.email = emailUploader;
        this.uploadedDocs = uploadedDocs;
        this.uploaderConsumers = consumers;
        this.logo = logo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getuploadedDocs() {
        return uploadedDocs;
    }

    public void setuploadedDocs(int uploadedDocs) {
        this.uploadedDocs = uploadedDocs;
    }

    public int getUploaderConsumers() {
        return uploaderConsumers;
    }

    public void setUploaderConsumers(int uploaderConsumers) {
        this.uploaderConsumers = uploaderConsumers;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}