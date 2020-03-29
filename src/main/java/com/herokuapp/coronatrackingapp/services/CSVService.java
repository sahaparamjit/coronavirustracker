package com.herokuapp.coronatrackingapp.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;

@Service
public class CSVService {
    @Autowired
    private RestTemplate restTemplate;

    public Iterable<CSVRecord> getCSVRecords(String url) throws IOException {
        String response = restTemplate.getForObject(url, String.class);
        StringReader reader = new StringReader(response);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        return records;
    }
}
