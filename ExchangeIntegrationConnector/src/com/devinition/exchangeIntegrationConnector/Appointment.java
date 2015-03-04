package com.devinition.exchangeIntegrationConnector;

import java.io.Serializable;

public class Appointment implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String changeKey;
	private String subject;
	private String location;
	private String startDateTime;
	private String endDateTime;
	private String body;
	private String dateTimeCreated;
	private String dateTimeReceived;
	private String dateTimeSent;
	private boolean allDayEvent;
	private String organizer;
	private boolean cancel;
	private String changeStatus;
	
	public Appointment(){}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateTimeCreated() {
		return dateTimeCreated;
	}

	public void setDateTimeCreated(String dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}

	public String getDateTimeReceived() {
		return dateTimeReceived;
	}

	public void setDateTimeReceived(String dateTimeReceived) {
		this.dateTimeReceived = dateTimeReceived;
	}

	public String getDateTimeSent() {
		return dateTimeSent;
	}

	public void setDateTimeSent(String dateTimeSent) {
		this.dateTimeSent = dateTimeSent;
	}

	public boolean isAllDayEvent() {
		return allDayEvent;
	}

	public void setAllDayEvent(boolean allDayEvent) {
		this.allDayEvent = allDayEvent;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getChangeKey() {
		return changeKey;
	}

	public void setChangeKey(String changeKey) {
		this.changeKey = changeKey;
	}
	

	public String getChangeStatus() {
		return changeStatus;
	}

	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
