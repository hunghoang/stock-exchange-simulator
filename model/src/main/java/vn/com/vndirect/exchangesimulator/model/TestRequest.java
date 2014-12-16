package vn.com.vndirect.exchangesimulator.model;

public class TestRequest extends HnxMessage{
	private String testSeqID;
	
	public TestRequest() {
		setMsgType("1");
	}

	public String getTestSeqID() {
		return testSeqID;
	}

	public void setTestSeqID(String testSeqID) {
		this.testSeqID = testSeqID;
	}

}
