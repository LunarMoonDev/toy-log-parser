package com.project.toy_log_parser.service.impl;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.drive.Drive;
import com.project.toy_log_parser.service.GoogleStorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleStorageServiceImpl implements GoogleStorageService {

    @Autowired
    private Drive googleDrive;

    @Override
    public InputStream download(String fileId, String uuid) throws IOException {
        log.info("X-Tracker: {} | downloading from gdrive", uuid);
        log.debug("X-Tracker: {} | request id: {}", fileId);

        return googleDrive.files().get(fileId).executeMediaAsInputStream();
    }
}
