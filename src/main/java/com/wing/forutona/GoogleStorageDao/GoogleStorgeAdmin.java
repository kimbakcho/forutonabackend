package com.wing.forutona.GoogleStorageDao;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.wing.forutona.Prefrerance;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class GoogleStorgeAdmin {
    Storage storage;
    GoogleStorgeAdmin(){
        GoogleCredentials credentials;
        File credentialsPath = new File(Prefrerance.storgeserviekeypath);  // TODO: update to your key path.
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
            storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId("forutona").build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Storage GetStorageInstance(){
        return storage;
    }
}
