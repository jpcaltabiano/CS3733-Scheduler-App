package zosma.controller;

import java.util.ArrayList;

import zosma.model.Timeslot;

public class SearchOpenTimeSlotResponse {
	String message;
	ArrayList<Timeslot> slots;
	int httpCode;
	
	public SearchOpenTimeSlotResponse (String message, ArrayList<Timeslot> slots, int code) {
		this.message = message;
		this.slots = slots;
		this.httpCode = code;
	}
	
	public SearchOpenTimeSlotResponse (String errorMessage, int code) {
		this.message = errorMessage;
		this.slots = null;
		this.httpCode = code;
	}
	
	public String toString() {
		if (slots == null) { return message; }
		return "SearchOpenTimeSlot(" + message + "," + slots.toString() + ")";
	}
}
