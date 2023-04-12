package com.units.it.utils;

import com.units.it.entities.FileProxy;

import java.util.Comparator;

//https://www.geeksforgeeks.org/comparator-interface-java/#:~:text=Method%202%3A%20Using%20comparator%20interface,elements%20based%20on%20data%20members.
public class UploadComparator implements Comparator<FileProxy> {
    public int compare(FileProxy a, FileProxy b) {
        return b.getDataUpload().compareTo(a.getDataUpload());
    }
}