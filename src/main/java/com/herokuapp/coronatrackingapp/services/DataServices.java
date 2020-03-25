package com.herokuapp.coronatrackingapp.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.herokuapp.coronatrackingapp.model.LocationDataPoints;
import com.herokuapp.coronatrackingapp.utils.UtilHelper;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

@Service
public class DataServices {
	private String dataUrl;
	private final RestTemplate restTemplate;
	private List<LocationDataPoints> allStates = new ArrayList<>();
	private final Logger logger = LoggerFactory.getLogger(DataServices.class);

	public List<LocationDataPoints> getAllStates() {
		return allStates;
	}

	@Autowired
	private Environment env;

	@Autowired
	public DataServices(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public List<LocationDataPoints> fetchUrlData() throws IOException {
        dataUrl = env.getProperty("covid.data.url");
		String response = restTemplate.getForObject(dataUrl, String.class);
		StringReader reader = new StringReader(response);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
		List<LocationDataPoints> locations = new ArrayList<>();
		int todaysReported = 0, yesterdaysReported = 0;
		for (CSVRecord record : records) {
			LocationDataPoints newState = new LocationDataPoints();
			if (UtilHelper.isValidData(record.get(record.size() - 1))
				&& UtilHelper.isValidData(record.get(record.size() - 2))){
				todaysReported = Integer.parseInt(record.get(record.size() - 1));
				yesterdaysReported = Integer.parseInt(record.get(record.size() - 2));
			}
			newState.setCountry(record.get("Country/Region"));
			newState.setState(record.get("Province/State"));
			newState.setTodaysReported(todaysReported);
			newState.setDifferenceFromLastDay(todaysReported - yesterdaysReported);
			locations.add(newState);
		}
		logger.info("Fetched " + locations.size() + " new records  - LocalDate - " + LocalDate.now() + "-"
				+ LocalTime.now());
		allStates = locations;
		return allStates;
	}

	public List<LocationDataPoints> fetchLocationsByCountry(String countryName) {
		String country = countryName.trim();
		logger.info("Logging from business logic!");
		List<LocationDataPoints> countryWiseLocations =
				this.getAllStates().stream()
				.filter(x -> x.getCountry().toLowerCase().equals(country.toLowerCase()))
				.collect(Collectors.toList());
		return countryWiseLocations;
	}

	public List<LocationDataPoints> fetchLocationsByCountryByState(String countryName, String stateName) {
		String country = countryName.trim();
		String state = stateName.trim();
		logger.info("Logging from business logic!");
		List<LocationDataPoints> countryAndStateWiseLocations =
				this.getAllStates().stream()
				.filter(x -> x.getCountry().toLowerCase().equals(country.toLowerCase()))
				.filter(x -> x.getState().toLowerCase().equals(state.toLowerCase()))
				.collect(Collectors.toList());
		return countryAndStateWiseLocations;
	}
}
