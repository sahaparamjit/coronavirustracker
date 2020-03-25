package com.herokuapp.coronatrackingapp.utils;

public class UtilHelper {
	public static boolean isValidData(String data) {
		if (data.isEmpty() || data==null || !(data instanceof String))
			return false;
		return true;
	}
}
