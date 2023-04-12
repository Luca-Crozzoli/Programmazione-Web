package com.units.it.helpers;

import com.units.it.entities.File;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class FileHelper extends AbstractHelper {
    public static List<File> fileList() {

        return ofy().load().type(File.class).list();
    }

    public static List<File> consumerFileList(String usernameConsumer) {
        List<File> files = ofy().load().type(File.class).filter("usernameCons", usernameConsumer).list();
        files.removeIf(file -> file.getFile() == null);
        return files;
    }

    public static List<File> uploaderFileList(String usernameUploader) {
        List<File> files = uploaderCompleteFileList(usernameUploader);
        files.removeIf(file -> file.getFile() == null);
        return files;
    }

    public static List<File> uploaderCompleteFileList(String usernameUploader) {
        return ofy().load().type(File.class).filter("usernameUpl", usernameUploader).list();
    }
}
