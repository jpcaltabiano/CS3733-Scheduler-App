package zosma.controller;

public class ReportActivityRequest {

	int hour;

	public ReportActivityRequest(int hour) {
		this.hour = hour;
	}

	public String toString() {
		return "ReportActivity(" + hour + ")";
	}

}
