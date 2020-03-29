package com.herokuapp.coronatrackingapp.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.herokuapp.coronatrackingapp.model.LocationDataPoints;
import com.herokuapp.coronatrackingapp.services.DataServices;

@Controller
public class WebController {

	@Autowired
	DataServices coronaServices;

	@GetMapping("/")
	public String Response(Model model) {

		List<LocationDataPoints> locations = coronaServices.getAllStates().stream()
				.sorted((p1, p2) -> p2.getTodaysReported() - p1.getTodaysReported())
				.filter(x -> x.getCountry().trim() != "").collect(Collectors.toList());
		model.addAttribute("locations", locations);
		int totalReported = coronaServices.getAllStates().stream().mapToInt(x -> x.getTodaysReported()).sum();
		int totalDeaths = coronaServices.getAllStates().stream().mapToInt(x -> x.getTotalDeaths()).sum();
		int totalRecovered = coronaServices.getAllStates().stream().mapToInt(x -> x.getTotalRecovered()).sum();
		int totalNewlyReported = coronaServices.getAllStates().stream().mapToInt(x -> x.getDifferenceFromLastDay())
				.sum();
		model.addAttribute("totalReported", totalReported);
		model.addAttribute("percentageRecovery", 100*((float) totalRecovered/totalReported));
		model.addAttribute("totalDeaths", totalDeaths);
		model.addAttribute("totalNewlyReported", totalNewlyReported);
		model.addAttribute("totalRecovered", totalRecovered);
		return "home";
	}

}
