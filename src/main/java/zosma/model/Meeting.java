package zosma.model;

public class Meeting {

	String participantCode;
	String organizerCode;
	String user;
	String slotid;
	
	public Meeting(String slotid) {
		this.participantCode = "none";
		this.organizerCode = "none";
		this.user = "none";
		this.slotid = slotid;
	}
	
	public Meeting(String user, String organizerCode, String slotid) {
		this.user = user;
		this.slotid = slotid;
		this.organizerCode = organizerCode;
		this.participantCode = new RandomString(8).nextString();
	}
	
	public boolean checkCode(String code) {
		return this.participantCode.equals(code) || this.organizerCode.equals(code);
	}
	
	public String getUser() {
		return user;
	}
	public String getPSC() {
		return participantCode;
	}
	
	public String getOSC() {
		return organizerCode;
	}
	
	public String getSID() {
		return slotid;
	}

	public void setPSC(String psc) {
		this.participantCode = psc;
		
	}
	
}
