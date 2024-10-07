package com.project.toy_log_parser.service;

import org.apache.kafka.common.Uuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.project.toy_log_parser.service.impl.ParseReportServiceImpl;

public class ParseReportServiceTest {

    @InjectMocks
    private ParseReportServiceImpl service;

    @Mock
    private GoogleStorageService googleStorageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String createUuid() {
        return Uuid.randomUuid().toString();
    }

    private String createFileId() {
        return "fileId";
    }

    private InputStream createInputStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

    @Test
    public void testGetReport() throws IOException {
        String uuid = createUuid();
        String fileId = createFileId();
        InputStream stream = createInputStream();

        when(googleStorageService.download(fileId, uuid)).thenReturn(stream);
        CSVReader reader = service.getReport(fileId, uuid);

        assertNotNull(reader);
    }
}
