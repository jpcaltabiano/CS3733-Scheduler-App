package zosma.controller;

import java.util.ArrayList;

import zosma.model.Timeslot;

public class OpenTimeSlotResponse {
	String message;
	ArrayList<Timeslot> slot;
	int httpCode;
	
	public OpenTimeSlotResponse (String message,ArrayList<Timeslot> slot, int code) {
		this.message = message;
		this.slot = slot;
		this.httpCode = code;
	}
	
	public OpenTimeSlotResponse (String message, int code) {
		this.message = message;
		this.slot = null;
		this.httpCode = code;
	}
	
	public String toString() {
		return "OpenTimeSlot(" + message + ")";
	}
}
