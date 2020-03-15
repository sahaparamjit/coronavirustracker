package com.synapse.metastore.services;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.synapse.metastore.model.LocationDataPoints;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

@Service
public class DataServices {
	private final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
	private final RestTemplate restTemplate;
	private List<LocationDataPoints> allStates = new ArrayList<>();
	
	public List<LocationDataPoints> getAllStates() {
		return allStates;
	}

	@Autowired
	public DataServices(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public List<LocationDataPoints> fetchUrlData() throws IOException {
		String response = restTemplate.getForObject(VIRUS_DATA_URL, String.class);
		StringReader reader = new StringReader(response);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
		List<LocationDataPoints> locations = new ArrayList<>();
		for (CSVRecord record : records) {
			LocationDataPoints newState = new LocationDataPoints();
			int todaysReported = Integer.parseInt(record.get(record.size() - 1));
			int yesterdaysReported = Integer.parseInt(record.get(record.size() - 2));
			newState.setCountry(record.get("Country/Region"));
			newState.setState(record.get("Province/State"));
			newState.setTodaysReported(todaysReported);
			newState.setDifferenceFromLastDay(todaysReported - yesterdaysReported);
			locations.add(newState);
		}
		allStates = locations;
		return allStates;
	}
}
