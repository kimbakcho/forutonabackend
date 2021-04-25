package com.wing.forutona.CustomUtil;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class FireBaseAdmin {
    FireBaseAdmin(    @Value("${forutona.serviekeypath}")  String serviekeypath) {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(serviekeypath);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://forutona.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
