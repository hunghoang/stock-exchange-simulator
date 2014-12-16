package vn.com.vndirect.exchangesimulator.model;

public class Heartbeat extends HnxMessage{
	private String testSeqID;
	
	public Heartbeat() {
		setMsgType("0");
	}

	public String getTestSeqID() {
		return testSeqID;
	}

	public void setTestSeqID(String testSeqID) {
		this.testSeqID = testSeqID;
	}
	
}
