package com.project.toy_log_parser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import org.apache.kafka.common.Uuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.project.toy_log_parser.entity.Player;
import com.project.toy_log_parser.repository.PlayerRepository;
import com.project.toy_log_parser.service.impl.ProcessReportServiceImpl;

public class ProcessReportServiceTest {

    @InjectMocks
    private ProcessReportServiceImpl service;

    @Mock
    private PlayerRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String createUuid() {
        return Uuid.randomUuid().toString();
    }

    private long createPlayerId() {
        return 1l;
    }

    public String createPassReport() {
        return "FIRST_NAME,LAST_NAME,SERVER,JOB,DIRECT_HIT,CRIT_HIT,DAMAGE\n" +
                "Igor,Ozouf,Kujata,VPR,89%,13%,2345\n" +
                "Igor,Ozouf,Kujata,SAM,56%,26%,7452\n";
    }

    public CSVReader createCsvReader(String mockCsvReport) {
        return new CSVReader(new StringReader(mockCsvReport));
    }

    public Player createPlayer() {
        Player player = new Player();
        player.setCritAverage(0.0);
        player.setDirectAverage(0.0);
        player.setDmgAverage(0.0);
        player.setCritLogNum(0l);
        player.setDirectLogNum(0l);
        player.setDmgLogNum(0l);

        return player;
    }

    @Test
    public void testProcessReport() throws CsvValidationException, IOException{
        String uuid = createUuid();
        long playerId = createPlayerId();
        CSVReader report = createCsvReader(createPassReport());
        Player player = createPlayer();

        when(repository.findById(any())).thenReturn(Optional.of(player));
        when(repository.save(any())).thenReturn(player);

        service.processReport(uuid, report, playerId);

        assertEquals(72.5, player.getDirectAverage());
        assertEquals(19.5, player.getCritAverage());
        assertEquals(4898.5, player.getDmgAverage());
        assertEquals(2, player.getCritLogNum());
        assertEquals(2, player.getDirectLogNum());
        assertEquals(2, player.getDmgLogNum());

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
    }
}
