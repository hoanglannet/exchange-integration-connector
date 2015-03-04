package com.devinition.exchangeIntegrationConnector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TestDate {
	public static void main(String[] args) {
//		GregorianCalendar gc = new GregorianCalendar();
//		TimeZone tz = gc.getTimeZone();
//		gc.add(GregorianCalendar.MILLISECOND, +tz.getDSTSavings());
//		System.out.println(gc.getTime());
//		
//		Date d = offsetTimeZone(new Date(), "Asia/Kolkata", "US/Central");
//		
//		// Get a Calendar instance using the default time zone and locale.
//				java.util.Calendar calendar = java.util.Calendar.getInstance();
//
//				// Set the calendar's time with the given date
//				calendar.setTime(d);
//				
//		System.out.println("Ouput: " + calendar.getTime());
		
		
		try {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date d = f.parse("2013-04-01T05:00:00Z");
	
			TimeZone tz = TimeZone.getTimeZone("Europe/Amsterdam");
			boolean inDs = tz.inDaylightTime(d);
			System.out.println(inDs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
