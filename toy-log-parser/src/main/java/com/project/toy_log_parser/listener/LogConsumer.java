package com.project.toy_log_parser.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.project.toy_log_parser.dto.KafkaDTO;
import com.project.toy_log_parser.service.ParseReportService;
import com.project.toy_log_parser.service.ProcessReportService;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class LogConsumer {

    @Autowired
    ParseReportService reportService;

    @Autowired
    ProcessReportService processReportService;

    @KafkaListener(topics = "${kafka.log.validation.topic.name}", groupId = "${spring.kafka.consumer.configuration.group.id}", containerFactory = "kafkaListenerContainerFactory", errorHandler = "listenerExceptionHandler")
    public void listen(KafkaDTO payload, Acknowledgment acknowledgment) throws IOException, CsvValidationException {
        String uuid = UUID.randomUUID().toString();

        log.info("X-Tracker: {} | consuming kafka message", uuid);
        CSVReader report = reportService.getReport(payload.getReportId(), uuid);
        processReportService.processReport(uuid, report, payload.getPlayerId());

        acknowledgment.acknowledge();
    }
}
