package com.project.toy_log_parser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;

import com.opencsv.CSVReader;
import com.project.toy_log_parser.service.GoogleStorageService;
import com.project.toy_log_parser.service.ParseReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParseReportServiceImpl implements ParseReportService {
    @Autowired
    GoogleStorageService googleService;

    @Override
    public CSVReader getReport(String fileId, String uuid) throws IOException {
        log.info("X-Tracker: {} | retrieving csv report file", uuid);
        log.debug("X-Tracker: {} | request id: {}", fileId);
        
        return new CSVReader(new InputStreamReader(googleService.download(fileId, uuid)));
    }
}
