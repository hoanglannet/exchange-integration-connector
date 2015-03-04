package com.devinition.exchangeIntegrationConnector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Utils {
	public static final String DATE_PATTERN = "dd-MM-yyyy";
	public static final String DATE_PATTERN_TIME = "dd-MM-yyyy HH:mm";
	
	public static String formatDateToString(Date date, String datePattern) {
		if (datePattern == null || datePattern.trim() == "") {
			datePattern = DATE_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		return sdf.format(date);
	}
	
	public static String formatDateToStringUTC(Date date, String datePattern) {
		if (datePattern == null || datePattern.trim() == "") {
			datePattern = DATE_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		return sdf.format(date);
	}
	
	public static Date parseStringToDate(String dateString, String datePattern) {
		if (datePattern == null || datePattern.trim() == "") {
			datePattern = DATE_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		Date ret = null;
		try {
			ret = sdf.parse(dateString);
		} catch (ParseException e) {
		}
		
		return ret;
	}
	
	public static String formatDateTimeToString(Date date, String datePattern) {
		if (datePattern == null || datePattern.trim() == "") {
			datePattern = DATE_PATTERN_TIME;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		return sdf.format(date);
	}
	
	public static String formatDateTimeToStringUTC(String dateString, String datePattern) throws ParseException {
		if (datePattern == null || datePattern.trim() == "") {
			datePattern = DATE_PATTERN_TIME;
		}
		if (dateString == null || dateString.trim() == "") {
			return "";
		}
		
		TimeZone utc = TimeZone.getTimeZone("UTC");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		GregorianCalendar cal = new GregorianCalendar(utc);
		cal.setTime(f.parse(dateString));
		
		TimeZone tz = TimeZone.getTimeZone("Europe/Amsterdam");
		boolean inDs = tz.inDaylightTime(cal.getTime());
		if (inDs) {
			cal.set(GregorianCalendar.HOUR, cal.get(GregorianCalendar.HOUR) + 1);
		}
		
		return Utils.formatDateToString(cal.getTime(), DATE_PATTERN_TIME);
	}
	
	public static String formatDateTimeFromGTMToDateUTC(String dateString, String datePattern) throws ParseException {
		if (datePattern == null || datePattern.trim() == "") {
			datePattern = DATE_PATTERN_TIME;
		}
		if (dateString == null || dateString.trim() == "") {
			return "";
		}
		
		SimpleDateFormat f = new SimpleDateFormat(datePattern);
		String t = "";
		try {
			Date ret = f.parse(dateString);
			f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			
			TimeZone tz = TimeZone.getTimeZone("Europe/Amsterdam");
			boolean inDs = tz.inDaylightTime(ret);
			if (inDs) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(ret);
				cal.set(GregorianCalendar.HOUR, cal.get(GregorianCalendar.HOUR) - 1);
				ret = cal.getTime();
			}
			
			t = f.format(ret);
		} catch (ParseException e) {
		}
		return t;
	}
	
	public static Date parseStringToDateTime(String dateString, String datePattern) {
		if (datePattern == null || datePattern.trim() == "") {
			datePattern = DATE_PATTERN_TIME;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		Date ret = null;
		try {
			ret = sdf.parse(dateString);
		} catch (ParseException e) {
		}
		
		return ret;
	}
}
