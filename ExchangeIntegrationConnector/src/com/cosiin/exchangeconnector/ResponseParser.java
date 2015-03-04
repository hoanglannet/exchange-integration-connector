package com.cosiin.exchangeconnector;

import java.io.File;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ResponseParser {
	
	public static boolean parseSOAPCodeErrorResponse(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlString));
		Document dom = db.parse(is);
		
		NodeList errorCodeNodeList = dom.getElementsByTagNameNS("*", "ResponseCode");
		
		boolean ret = true;
		String code = "";
		
		if (errorCodeNodeList != null && errorCodeNodeList.getLength() > 0) {
			code = ((Element) errorCodeNodeList.item(0)).getFirstChild().getNodeValue();
		}
		
		if (code != null && code.equalsIgnoreCase("NoError")) {
			ret = false;
		}
		
		return ret;
	}
	
	public static String parseSyncStateKey(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlString));
		Document dom = db.parse(is);
		
		NodeList errorCodeNodeList = dom.getElementsByTagNameNS("*", "SyncState");
		
		String key = "";
		
		if (errorCodeNodeList != null && errorCodeNodeList.getLength() > 0) {
			key = ((Element) errorCodeNodeList.item(0)).getFirstChild().getNodeValue();
		}
		
		return key;
	}
	
	public static List<Appointment> parseSOAPResponseWithFullStatus(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlString));
		Document dom = db.parse(is);
		
		NodeList nodeList = dom.getElementsByTagNameNS("*", "Changes");
		List<Appointment> ret = new ArrayList<Appointment>();
		
		if (nodeList != null && nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			ret.addAll(getReturnAppointments(node, "Update"));
			ret.addAll(getReturnAppointments(node, "Delete"));
			ret.addAll(getReturnAppointments(node, "Create"));
		}
		
		return ret;
	}
	
	public static List<Appointment> parseSOAPResponse(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlString));
		Document dom = db.parse(is);
		
		NodeList nodeList = dom.getElementsByTagNameNS("*", "CalendarItem");
		List<Appointment> ret = new ArrayList<Appointment>();
		
		if (nodeList != null && nodeList.getLength() > 0) {
			
			Appointment appointment = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				appointment = extractAppointment(node);
				
				ret.add(appointment);
			}
		}
		
		return ret;
	}
	
	public static List<Appointment> parseSOAPResponseFromFile(String xmlFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document dom = builder.parse(new File(xmlFile));
		
		NodeList nodeList = dom.getElementsByTagNameNS("*", "CalendarItem");
		List<Appointment> ret = new ArrayList<Appointment>();
		
		if (nodeList != null && nodeList.getLength() > 0) {
			
			Appointment appointment = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				appointment = extractAppointment(node);
				
				ret.add(appointment);
			}
		}
		
		return ret;
	}
	
	private static Appointment extractAppointment(Node node) throws ParseException {
		Appointment appointment = new Appointment();
		appointment.setId(getItemId(node, "Id"));
		appointment.setChangeKey(getItemId(node, "ChangeKey"));
		appointment.setSubject(getCommonValue(node, "Subject"));
		
//		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//		Date d = f.parse("2013-03-01T05:00:00Z");
//
//		TimeZone tz = TimeZone.getTimeZone("US/Alaska");
//		boolean inDs = tz.inDaylightTime(d);
//		System.out.println(inDs);
		
		appointment.setStartDateTime(Utils.formatDateTimeToStringUTC(getCommonValue(node, "Start"), Utils.DATE_PATTERN_TIME));
		appointment.setEndDateTime(Utils.formatDateTimeToStringUTC(getCommonValue(node, "End"), Utils.DATE_PATTERN_TIME));
		appointment.setBody(getCommonValue(node, "Body"));
		appointment.setLocation(getCommonValue(node, "Location"));
		appointment.setDateTimeCreated(Utils.formatDateTimeToStringUTC(getCommonValue(node, "DateTimeCreated"), Utils.DATE_PATTERN_TIME));
		appointment.setAllDayEvent((getCommonValue(node, "IsAllDayEvent").equalsIgnoreCase("true")) ? true : false);
		return appointment;
	}
	
	private static String getItemId(Node node, String attribute) {
		String ret = "";
		Element e = (Element) ((Element) (node)).getElementsByTagNameNS("*", "ItemId").item(0);
		
		if (e != null) {
			ret = e.getAttribute(attribute);
		}
		return ret;
	}
	
	private static String getCommonValue(Node node, String tag) {
		String ret = "";
		Element e = (Element) ((Element) (node)).getElementsByTagNameNS("*", tag).item(0);
		
		if (e != null && e.getFirstChild() != null) {
			ret = e.getFirstChild().getNodeValue();
		}
		return ret;
	}
	
	private static List<Appointment> getReturnAppointments(Node node, String tag) throws Exception {
		NodeList nodeList = ((Element) (node)).getElementsByTagNameNS("*", tag);
		List<Appointment> ret = new ArrayList<Appointment>();
		
		if (nodeList != null && nodeList.getLength() > 0) {
			
			Appointment appointment = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node1 = nodeList.item(i);
				appointment = extractAppointment(node1);
				appointment.setChangeStatus(tag);
				
				ret.add(appointment);
			}
		}
		return ret;
	}
}
