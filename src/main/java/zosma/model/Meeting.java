package zosma.model;

public class Meeting {

	String code;
	String user;
	
	public Meeting(String user) {
		this.user = user;
		this.code = new RandomString(8).nextString();
	}
	
}
