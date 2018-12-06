package zosma.model;

public class Meeting {

	String participantCode;
	String organizerCode;
	String user;
	
	public Meeting(String user, String organizerCode) {
		this.user = user;
		this.organizerCode = organizerCode;
		this.participantCode = new RandomString(8).nextString();
	}
	
	public boolean checkCode(String code) {
		return this.participantCode.equals(code) || this.organizerCode.equals(code);
	}
	
}
