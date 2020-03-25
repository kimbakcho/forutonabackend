package com.wing.forutona.GoogleStorageDao;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class GoogleStorgeAdmin {

    private String storgeserviekeypath;

    Storage storage;

    GoogleStorgeAdmin(@Value("${forutona.storgeserviekeypath}") String storgeserviekeypath) {
        this.storgeserviekeypath = storgeserviekeypath;
        GoogleCredentials credentials;
        System.out.println("sot = " + storgeserviekeypath);
        File credentialsPath = new File(storgeserviekeypath);
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
            storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId("forutona").build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Storage GetStorageInstance() {
        return storage;
    }
}
