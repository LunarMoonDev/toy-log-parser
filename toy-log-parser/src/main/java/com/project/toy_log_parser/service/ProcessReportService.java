package com.project.toy_log_parser.service;

import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public interface ProcessReportService {
    void processReport(String uuid, CSVReader report, long playerId) throws CsvValidationException, IOException;
}
