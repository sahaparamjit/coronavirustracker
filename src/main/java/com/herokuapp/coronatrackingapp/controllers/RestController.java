package com.herokuapp.coronatrackingapp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.herokuapp.coronatrackingapp.model.LocationDataPoints;
import com.herokuapp.coronatrackingapp.services.DataServices;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
public class RestController {

	private List<LocationDataPoints> filteredCountries = new ArrayList<>();

	@Autowired
	DataServices services;
 
	private final Logger logger = LoggerFactory.getLogger(RestController.class);

	@GetMapping("/countries")
	public ResponseEntity<List<LocationDataPoints>> getAllCountriesList() throws IOException {
		List<LocationDataPoints> locations = services.getAllStates();
		int uniqueCountries = locations.stream().map(x -> x.getCountry()).collect(Collectors.toSet()).size();
		logger.info(uniqueCountries + " countries of locations fetched");
		return new ResponseEntity<List<LocationDataPoints>>(locations, HttpStatus.OK);
	}

	@GetMapping("/countries/{countryname}")
	public ResponseEntity<List<LocationDataPoints>> getAllCountriesList(@PathVariable String countryname) {
		String countryName = countryname.trim();
		if (countryname.isEmpty()) {
			return new ResponseEntity<List<LocationDataPoints>>(HttpStatus.BAD_REQUEST);
		}
		List<LocationDataPoints> locations = services.fetchLocationsByCountry(countryName);
		filteredCountries = locations;
		return new ResponseEntity<List<LocationDataPoints>>(locations, HttpStatus.OK);
	}

	@GetMapping("/countries/{countryname}/{statename}")
	public ResponseEntity<List<LocationDataPoints>> getAllCountriesList(@PathVariable String countryname,
			@PathVariable String statename) {
		String countryName = countryname.trim();
		String stateName = statename.trim();
		getAllCountriesList(countryName);
		if (countryname.isEmpty() || stateName.isEmpty()) {
			return new ResponseEntity<List<LocationDataPoints>>(HttpStatus.BAD_REQUEST);
		}
		List<LocationDataPoints> filtered = services.fetchLocationsByCountryByState(countryName, stateName);

		return new ResponseEntity<List<LocationDataPoints>>(filtered, HttpStatus.OK);
	}

}
