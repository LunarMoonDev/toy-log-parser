package com.project.toy_log_parser.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.project.toy_log_parser.entity.Player;
import com.project.toy_log_parser.repository.PlayerRepository;
import com.project.toy_log_parser.service.ProcessReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessReportServiceImpl implements ProcessReportService {

    @Autowired
    private PlayerRepository repository;

    @Override
    public void processReport(String uuid, CSVReader report, long playerId) throws CsvValidationException, IOException {
        log.info("X-Tracker: {} | parsing data", uuid);
        log.info("X-Tracker: {} | grabbing player id from db", uuid);

        // throw generic exception
        Player player = repository.findById(BigInteger.valueOf(playerId)).orElseThrow();

        String[] nextLine = report.readNext();
        String[] reportHeaders = Arrays.copyOf(nextLine, nextLine.length);

        nextLine = report.readNext(); // skip headers
        while(nextLine != null) {
            Long critNum = player.getCritLogNum() + 1;
            Long directNum = player.getDirectLogNum() + 1;
            Long dmgNum = player.getDmgLogNum() + 1;

            for(int i = 0; i < nextLine.length; i++) {
                String cellValue =  nextLine[i].trim();
                String header = reportHeaders[i].trim();

                switch (header) {
                    case "DIRECT_HIT":
                        cellValue = cellValue.substring(0, cellValue.length() - 1);
                        Double directNewAvg = (player.getDirectAverage() * (directNum - 1)) + Double.valueOf(cellValue);
                        directNewAvg /= directNum; 

                        player.setDirectAverage(directNewAvg);
                        player.setDirectLogNum(directNum);
                        break;
                    case "CRIT_HIT":
                        cellValue = cellValue.substring(0, cellValue.length() - 1);
                        Double critNewAvg = (player.getCritAverage() * (critNum - 1)) + Double.valueOf(cellValue);
                        critNewAvg /= critNum; 

                        player.setCritAverage(critNewAvg);
                        player.setCritLogNum(critNum);
                        break;
                    case "DAMAGE":
                        Double damageNewAvg = (player.getDmgAverage() * (dmgNum - 1)) + Double.valueOf(cellValue);
                        damageNewAvg /= dmgNum;

                        player.setDmgAverage(damageNewAvg);
                        player.setDmgLogNum(dmgNum);
                        break;
                    default:
                        break;
                }
            }

            nextLine = report.readNext();
        }

        // save progress
        log.info("X-Tracker: {} | updating player in database", uuid);
        repository.save(player);
    }
}
