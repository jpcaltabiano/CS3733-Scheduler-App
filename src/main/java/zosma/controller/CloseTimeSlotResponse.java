package zosma.controller;

import java.util.ArrayList;

import zosma.model.Timeslot;

public class CloseTimeSlotResponse {
	String message;
	ArrayList<Timeslot> slot;
	int httpCode;
	
	public CloseTimeSlotResponse (String message,ArrayList<Timeslot> slot, int code) {
		this.message = message;
		this.slot = slot;
		this.httpCode = code;
	}
	
	public CloseTimeSlotResponse (String message, int code) {
		this.message = message;
		this.slot = null;
		this.httpCode = code;
	}
	
	public String toString() {
		return "CloseTimeSlot(" + message + ")";
	}
}
