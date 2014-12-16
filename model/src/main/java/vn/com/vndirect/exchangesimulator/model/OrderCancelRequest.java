package vn.com.vndirect.exchangesimulator.model;

import java.util.Date;

public class OrderCancelRequest extends HnxMessage{

	private String text;
	private Boolean possDupFlag;
	private String clOrdID;
	private String origClOrdID;
	private String symbol;
	private Date  transactTime;
	
	public OrderCancelRequest() {
		setMsgType("F");
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isPossDupFlag() {
		return possDupFlag;
	}
	public void setPossDupFlag(boolean possDupFlag) {
		this.possDupFlag = possDupFlag;
	}
	public String getClOrdID() {
		return clOrdID;
	}
	public void setClOrdID(String clOrdID) {
		this.clOrdID = clOrdID;
	}
	public String getOrigClOrdID() {
		return origClOrdID;
	}
	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdID = origClOrdID;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Date getTransactTime() {
		return transactTime;
	}
	public void setTransactTime(Date transactTime) {
		this.transactTime = transactTime;
	}

	@Override
	public String toString() {
		return "OrderCancelRequest [text=" + text + ", possDupFlag=" + possDupFlag + ", clOrdID=" + clOrdID + ", origClOrdID=" + origClOrdID + ", symbol=" + symbol
				+ ", transactTime=" + transactTime + "]";
	}

	
}
