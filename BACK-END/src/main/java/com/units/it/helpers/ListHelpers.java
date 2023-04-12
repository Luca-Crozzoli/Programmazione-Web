package com.units.it.helpers;

import com.units.it.entities.Account;
import com.units.it.entities.AccountProxy;
import com.units.it.entities.File;
import com.units.it.entities.FileProxy;
import com.units.it.utils.UploadComparator;

import java.util.ArrayList;
import java.util.List;

import static com.units.it.helpers.AccountHelper.accountListByRole;
import static com.units.it.helpers.FileHelper.*;

public class ListHelpers {
    private static List<FileProxy> getFilesProxyListFromFiles(List<File> filesList) {
        List<FileProxy> filesProxyList = new ArrayList<>();
        for (File file : filesList) {
            filesProxyList.add(new FileProxy(file));
        }
        return filesProxyList;
    }

    public static List<FileProxy> completeFilesProxy() {
        return getFilesProxyListFromFiles(fileList());
    }

    public static List<FileProxy> consumerFilesProxy(String usernameConsumer) {
        List<FileProxy> filesProxyList = getFilesProxyListFromFiles(consumerFileList(usernameConsumer));
        return orderByData(filesProxyList);
    }

    public static List<FileProxy> uploaderFilesProxy(String usernameUploader) {
        List<FileProxy> info = getFilesProxyListFromFiles(uploaderFileList(usernameUploader));
        return orderByData(info);
    }

    public static List<AccountProxy> getAccountProxyListFromAccounts(List<Account> accountList) {
        List<AccountProxy> accountProxyList = new ArrayList<>();
        for (Account a : accountList) {
            accountProxyList.add(new AccountProxy(a));
        }
        return accountProxyList;
    }

    public static List<AccountProxy> accountProxyListByRole(String role) {
        return getAccountProxyListFromAccounts(accountListByRole(role));
    }

    private static List<FileProxy> orderByData(List<FileProxy> filesProxyList) {
        List<FileProxy> filesRead = new ArrayList<>();
        for (FileProxy fileProxy : filesProxyList) {
            if (!fileProxy.getDataView().equals(""))
                filesRead.add(fileProxy);
        }
        filesProxyList.removeIf(el -> !el.getDataView().equals(""));
        filesProxyList.sort(new UploadComparator());
        filesRead.sort(new UploadComparator());
        filesProxyList.addAll(filesRead);
        return filesProxyList;
    }




}
