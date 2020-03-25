package com.herokuapp.coronatrackingapp.model;

public class LocationDataPoints {
	private String country;
	private String state;
	private int todaysReported;
	private int differenceFromLastDay;

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
				+ ", differenceFromLastDay=" + differenceFromLastDay + "]";
	}

}
