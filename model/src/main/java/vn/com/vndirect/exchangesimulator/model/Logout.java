package vn.com.vndirect.exchangesimulator.model;

public class Logout extends HnxMessage{
	private String text;

	public Logout() {
		setMsgType("5");
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
