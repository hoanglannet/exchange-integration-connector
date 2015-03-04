package com.cosiin.exchangeconnector;

import java.io.Serializable;

public class Calendar implements Serializable {
	private static final long serialVersionUID = 1L;
	private String syncStateKey;
	
	private boolean error;
	private Appointment[] appointments;

	public Calendar() {
	}

	public Appointment[] getAppointments() {
		return appointments;
	}

	public void setAppointments(Appointment[] appointments) {
		this.appointments = appointments;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getSyncStateKey() {
		return syncStateKey;
	}

	public void setSyncStateKey(String syncStateKey) {
		this.syncStateKey = syncStateKey;
	}
}
