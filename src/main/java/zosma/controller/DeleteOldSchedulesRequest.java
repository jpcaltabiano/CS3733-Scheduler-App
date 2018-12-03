package zosma.controller;

public class DeleteOldSchedulesRequest {
	int day;

	public DeleteOldSchedulesRequest(int day) {
		this.day = day;
	}

	public String toString() {
		return "ShowWeekSchedule(" + day + ")";
	}
}
