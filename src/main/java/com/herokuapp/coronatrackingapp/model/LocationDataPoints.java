package com.herokuapp.coronatrackingapp.model;

public class LocationDataPoints {
	private String country;
	private String state;
	private int todaysReported;
	private int differenceFromLastDay;
	private int totalDeaths;
	private int todaysDeathToll;
	private int totalRecovered;
	private int todayRecovered;

	public int getTotalRecovered() {
		return totalRecovered;
	}

	public void setTotalRecovered(int totalRecovered) {
		this.totalRecovered = totalRecovered;
	}

	public int getTodayRecovered() {
		return todayRecovered;
	}

	public void setTodayRecovered(int todayRecovered) {
		this.todayRecovered = todayRecovered;
	}

	public int getTotalDeaths() {
		return totalDeaths;
	}

	public void setTotalDeaths(int totalDeaths) {
		this.totalDeaths = totalDeaths;
	}

	public int getTodaysDeathToll() {
		return todaysDeathToll;
	}

	public void setTodaysDeathToll(int todaysDeathToll) {
		this.todaysDeathToll = todaysDeathToll;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getTodaysReported() {
		return todaysReported;
	}

	public void setTodaysReported(int todaysReported) {
		this.todaysReported = todaysReported;
	}

	public int getDifferenceFromLastDay() {
		return differenceFromLastDay;
	}

	public void setDifferenceFromLastDay(int differenceFromLastDay) {
		this.differenceFromLastDay = differenceFromLastDay;
	}

	@Override
	public String toString() {
		return "LocationDataPoints [country=" + country + ", state=" + state + ", todaysReported=" + todaysReported
				+ ", differenceFromLastDay=" + differenceFromLastDay + ", totalDeaths=" + totalDeaths
				+ ", todaysDeathToll=" + todaysDeathToll + ", totalRecovered=" + totalRecovered + ", todayRecovered="
				+ todayRecovered + "]";
	}
}
