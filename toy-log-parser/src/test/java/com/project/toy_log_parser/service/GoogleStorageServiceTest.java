package com.project.toy_log_parser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Get;
import com.project.toy_log_parser.service.impl.GoogleStorageServiceImpl;

public class GoogleStorageServiceTest {

    @InjectMocks
    private GoogleStorageServiceImpl service;

    @Mock
    private Drive googleDrive;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String createUuid() {
        return UUID.randomUUID().toString();
    }

    private String createFileId() {
        return "fileId";
    }

    private InputStream createInputStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

    private Files createFile() {
        return mock(Drive.Files.class);
    }

    private Get createGet() {
        return mock(Drive.Files.Get.class);
    }

    @Test
    public void testDownload() throws IOException {
        String uuid = createUuid();
        String fileId = createFileId();
        InputStream stream = createInputStream();
        Files file = createFile();
        Get get = createGet();
        
        when(googleDrive.files()).thenReturn(file);
        when(file.get(anyString())).thenReturn(get);
        when(get.executeMediaAsInputStream()).thenReturn(stream);
        InputStream rStream = service.download(fileId, uuid);

        assertNotNull(rStream);
        assertEquals(stream, rStream);
    }
}
