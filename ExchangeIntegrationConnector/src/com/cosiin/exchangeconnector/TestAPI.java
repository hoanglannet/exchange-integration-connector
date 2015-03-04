package com.cosiin.exchangeconnector;

public class TestAPI {
	
	public static void main(String[] args) {
		String exchangeURL = "https://home.bgochten.nl/EWS/Exchange.asmx";
		String username = "ccollector";
		String password = "B5AtL5t";
		String domain = "rolec-bv";
		String datePattern = "yyyy-MM-dd";
		
		String hisSharedMailBox = "sdepartment@kgrolf.nl";
		String startDate = "2013-03-01";
		String endDate = "2013-04-02";
		
		String syncStateKey = "H4sIAAAAAAAEAO29B2AcSZYlJi9tynt/SvVK1+B0oQiAYBMk2JBAEOzBiM3mkuwdaUcjKasqgcplVmVdZhZAzO2dvPfee++999577733ujudTif33/8/XGZkAWz2zkrayZ4hgKrIHz9+fB8/In6NX+PX+TXoOfjF+w+P9+/d3znePnlwfLC9f3xyb/vJyZOT7Wd7Tw5Onjx49vD+/Z1f8t3X49fXy+nrNmvzk2yZ1de/xqP3f/NZVc7y+mz2azx8/3d/Mq+bolr+Gofv/+p36f/1IqvfypCTX+PX+C3oa0WmzRcNWv4avzl9tUP/H6PNn7T4w/+iX/r7/ktP/sL/4U/8h/67X+8X/b2/5q/xl/2q//PP+Q3+yfV3/rw/+9f9y/7Ef+LOX0KN/uR/7Zf9Gr/Gr/Vr/Cb0f3p+/V/j1/ib6ccXxy/Pvjh+gU9+jWf3n/3kr/1r9J+X9P/f2ft7Qf8/iLTrPl0cAPvVr/Fr/Jp/5j/+y17+Gvfr//0vPvx9vv/F3/idz/6Dv+rX+1/+7V/31/g1fr0v/rBXv96v/7L3Hn31a/yZf/yr//a/+zV/xctf47/Jf40/6P8B4TUCoQ0CAAA=";
//		String syncStateKey = null;
		
		try {
//			String file = "res.xml";
//			List<Appointment> ret = ResponseParser.parseSOAPResponseFromFile(file);
			
			Calendar cal = ExchangeConnector.synHisSharedAppointmentList(exchangeURL, username, password, domain, datePattern, hisSharedMailBox, syncStateKey);
			System.out.println("SyncSate: " + cal.getSyncStateKey());
			System.out.println("-----------------");
			
//			Calendar cal = ExchangeConnector.getHisSharedAppointmentList(exchangeURL, username, password, domain, datePattern, hisSharedMailBox, startDate, endDate);
			for (int i = 0; i < cal.getAppointments().length; i++) {
				Appointment a = cal.getAppointments()[i];
				System.out.println("Id: " + a.getId());
				System.out.println("ChangeKey: " + a.getChangeKey());
				System.out.println("subject: " + a.getSubject());
				System.out.println("location: " + a.getLocation());
				System.out.println("body: " + a.getBody());
				System.out.println("start: " + a.getStartDateTime());
				System.out.println("end: " + a.getEndDateTime());
				System.out.println("allDayEvents: " + a.isAllDayEvent());
				System.out.println("changeStatus: " + a.getChangeStatus());
				System.out.println("-----------------");
			}
			
//			String exchangeId = "AAMkADU4NGNhZjcyLTI4YWQtNDM5NC05ZTZmLWQyYTBhYTRlOWMwMwBGAAAAAACmebuiQyTET5rmTyIp9r+2BwCt7uNmIeu+TbaQnlCoXL+zAAAAnRdKAACt7uNmIeu+TbaQnlCoXL+zAAAAn32kAAA=";
//			ExchangeConnector.deleteHisAppointment(exchangeURL, username, password, domain, exchangeId, hisSharedMailBox);
			
			
//			String input = "2011-08-11T01:23:45Z";
//			TimeZone utc = TimeZone.getTimeZone("UTC");
//			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//			f.setTimeZone(utc);
//			GregorianCalendar cal = new GregorianCalendar(utc);
//			cal.setTime(f.parse(input));
//			
//			System.out.println(Utils.formatDateToString(cal.getTime(), "yyyy-MM-dd HH:mm:ss"));
			
			
//			String input = "01-01-2013 10:10";
//			System.out.println(Utils.formatDateTimeFromGTMToDateUTC(input, ""));
			
			
//			String datePatternTime = "dd-MM-yyyy HH:mm";
//			String subject = "Lan - create with new solution 1";
//			String location = "VN";
//			String body = "11111Lan - create with new solution body";
//			String startDateTime = "02-03-2013 06:20";
//			String endDateTime = "02-03-2013 07:20";
//			String exchangeId = "AAMkADY2OWFjZjFkLTNhZGEtNDFlYi1iZDBkLThiMTdkNDZlODI3ZABGAAAAAACQbYWgflzQQp/uj8DsBnG7BwCk+f2aCMd1SpyZBaSPximiAAAAkdTzAACk+f2aCMd1SpyZBaSPximiAAAAlSrPAAA=";
//			String exchangeChangeId = "DwAAABYAAACk+f2aCMd1SpyZBaSPximiAAAAltVU";
			
//			Appointment e = ExchangeConnector.updateHisAppointment(exchangeURL, username, password, domain, datePatternTime, subject, location, startDateTime, endDateTime, body, hisSharedMailBox, exchangeId, exchangeChangeId);
//			System.out.println(e.getId());
//			ExchangeConnector.makeHisAppointment(exchangeURL, username, password, domain, datePatternTime, subject, location, startDateTime, endDateTime, body, hisSharedMailBox);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
