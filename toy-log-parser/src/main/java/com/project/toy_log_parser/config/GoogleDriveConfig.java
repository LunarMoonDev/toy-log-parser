package com.project.toy_log_parser.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;


@Configuration
public class GoogleDriveConfig {

    @Value("${drive.parse.v1.application-name}")
    private String applicationName;

    @Value("${drive.parse.v1.cred-file}")
    private String credentialFile;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Bean("serviceAccountKeyPath")
    public String createServiceAccountKeyPath() throws FileNotFoundException {
        return ResourceUtils.getURL("classpath:creds/" + credentialFile).getPath();
    }

    @Bean("googleDriveService")
    public Drive createDriveSerDrive(@Autowired @Qualifier("serviceAccountKeyPath") String serviceAccountKeyPath)
            throws GeneralSecurityException, IOException {
        
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyPath))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, requestInitializer).build();
    }
}
