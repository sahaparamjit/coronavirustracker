package com.herokuapp.coronatrackingapp.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.herokuapp.coronatrackingapp.model.LocationDataPoints;
import com.herokuapp.coronatrackingapp.utils.UtilHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class DataServices {
	private String dailyReportedUrl, deathReportedUrl, recoveredUrl;
	private List<LocationDataPoints> allStates = new ArrayList<>();
	private final Logger logger = LoggerFactory.getLogger(DataServices.class);

	public List<LocationDataPoints> getAllStates() {
		return allStates;
	}

	@Autowired
	private Environment env;

	@Autowired
	private CSVService csvService;

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public List<LocationDataPoints> fetchUrlData() throws IOException {
		dailyReportedUrl = env.getProperty("covid.data.url");
		deathReportedUrl = env.getProperty("covid.death.url");
		recoveredUrl = env.getProperty("covid.recovered.url");
		int todaysReported = 0, yesterdaysReported = 0, yesterdayDeathsReported = 0, todaysDeathReported = 0,
				todaysRecoveredReported = 0, yesterdayRecoveredReported = 0;
		Iterable<CSVRecord> records = csvService.getCSVRecords(dailyReportedUrl);
		Iterable<CSVRecord> deaths = csvService.getCSVRecords(deathReportedUrl);
		Iterable<CSVRecord> recovered = csvService.getCSVRecords(recoveredUrl);
		Iterator<CSVRecord> death = deaths.iterator();
		Iterator<CSVRecord> recover = recovered.iterator();
		List<LocationDataPoints> locations = new ArrayList<>();
		for (CSVRecord record : records) {
			LocationDataPoints newState = new LocationDataPoints();
			if (UtilHelper.isValidData(record.get(record.size() - 1))
					&& UtilHelper.isValidData(record.get(record.size() - 2)) && death.hasNext() && recover.hasNext()) {
				CSVRecord deathRecord = death.next();
				CSVRecord recoveredCases = recover.next();
				todaysReported = Integer.parseInt(record.get(record.size() - 1));
				yesterdaysReported = Integer.parseInt(record.get(record.size() - 2));
				yesterdayDeathsReported = Integer.parseInt(deathRecord.get(deathRecord.size() - 2));
				todaysDeathReported = Integer.parseInt(deathRecord.get(deathRecord.size() - 1));
				yesterdayRecoveredReported = Integer.parseInt(recoveredCases.get(recoveredCases.size() - 2));
				todaysRecoveredReported = Integer.parseInt(recoveredCases.get(recoveredCases.size() - 1));
			}
			newState.setCountry(record.get("Country/Region"));
			newState.setState(record.get("Province/State"));
			newState.setTodaysReported(todaysReported);
			newState.setDifferenceFromLastDay(todaysReported - yesterdaysReported);
			newState.setTotalDeaths(todaysDeathReported);
			newState.setTodaysDeathToll(todaysDeathReported - yesterdayDeathsReported);
			newState.setTotalRecovered(todaysRecoveredReported);
			newState.setTodayRecovered(todaysRecoveredReported - yesterdayRecoveredReported);
			locations.add(newState);
		}
		logger.info("Fetched " + locations.size() + " new records  - LocalDate - " + LocalDate.now() + "-"
				+ LocalTime.now());
//		locations.forEach(System.out::println);
		allStates = locations;
		return allStates;
	}

	public List<LocationDataPoints> fetchLocationsByCountry(String countryName) {
		String country = countryName.trim();
		logger.info("Logging from business logic!");
		List<LocationDataPoints> countryWiseLocations = this.getAllStates().stream()
				.filter(x -> x.getCountry().toLowerCase().equals(country.toLowerCase())).collect(Collectors.toList());
		return countryWiseLocations;
	}

	public List<LocationDataPoints> fetchLocationsByCountryByState(String countryName, String stateName) {
		String country = countryName.trim();
		String state = stateName.trim();
		logger.info("Logging from business logic!");
		List<LocationDataPoints> countryAndStateWiseLocations = this.getAllStates().stream()
				.filter(x -> x.getCountry().toLowerCase().equals(country.toLowerCase()))
				.filter(x -> x.getState().toLowerCase().equals(state.toLowerCase())).collect(Collectors.toList());
		return countryAndStateWiseLocations;
	}
}
