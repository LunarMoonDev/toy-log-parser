package com.project.toy_log_parser.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.project.toy_log_parser.dto.KafkaDTO;
import com.project.toy_log_parser.service.ParseReportService;
import com.project.toy_log_parser.service.ProcessReportService;

public class LogConsumerTest {
    @InjectMocks
    private LogConsumer consumer;

    @Mock
    private ParseReportService service;

    @Mock
    private ProcessReportService processReportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public KafkaDTO createKafkaDTO() {
        return KafkaDTO.builder()
                .reportId("any").build();
    }

    private Acknowledgment createAcknowledgment() {
        return mock(Acknowledgment.class);
    }

    public String createPassReport() {
        return "FIRST_NAME,LAST_NAME,SERVER,JOB,DIRECT_HIT,CRIT_HIT,DAMAGE\n" +
                "Igor,Ozouf,Kujata,VPR,89%,13%,2345\n" +
                "Igor,Ozouf,Kujata,SAM,56%,26%,7452\n";
    }

    public CSVReader createCsvReader(String mockCsvReport) {
        return new CSVReader(new StringReader(mockCsvReport));
    }

    @Test
    public void testListen() throws IOException, CsvValidationException {
        KafkaDTO payload = createKafkaDTO();
        Acknowledgment acknowledgment = createAcknowledgment();
        CSVReader report = createCsvReader(createPassReport());

        when(service.getReport(anyString(), anyString())).thenReturn(report);
        doNothing().when(processReportService).processReport(anyString(), any(), anyLong());

        consumer.listen(payload, acknowledgment);

        verify(service, times(1)).getReport(anyString(), anyString());
        verify(processReportService, times(1)).processReport(anyString(), any(), anyLong());
        verify(acknowledgment, times(1)).acknowledge();
    }
}
