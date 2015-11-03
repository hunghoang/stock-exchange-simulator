package vn.com.vndirect.exchangesimulator.model;

import java.util.Date;

public class CrossOrderCancelRequest extends HnxMessage{
	private Boolean possDupFlag;
	private String orderId;
	private String crossID;
	private String origCrossID;
	private String crossType;
	private Date transactTime;
	private String symbol;
	private Character side;
		
	
	public CrossOrderCancelRequest() {
		setMsgType("u");
	}
	
	@Override
	public String toString() {
		return "{\"possDupFlag\":\"" + possDupFlag + "\", \"orderId\":\""
				+ orderId + "\", \"crossID\":\"" + crossID
				+ "\", \"origCrossID\":\"" + origCrossID
				+ "\", \"crossType\":\"" + crossType
				+ "\", \"transactTime\":\"" + transactTime
				+ "\", \"symbol\":\"" + symbol + "\", \"side\":\"" + side
				+ "\"}";
	}

	public boolean isPossDupFlag() {
		return possDupFlag;
	}

	public void setPossDupFlag(boolean possDupFlag) {
		this.possDupFlag = possDupFlag;
	}

	public Date getTransactTime() {
		return transactTime;
	}

	public void setTransactTime(Date transactTime) {
		this.transactTime = transactTime;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Boolean getPossDupFlag() {
		return possDupFlag;
	}

	public void setPossDupFlag(Boolean possDupFlag) {
		this.possDupFlag = possDupFlag;
	}

	public String getCrossType() {
		return crossType;
	}

	public void setCrossType(String crossType) {
		this.crossType = crossType;
	}

	public String getCrossID() {
		return crossID;
	}

	public void setCrossID(String crossID) {
		this.crossID = crossID;
	}

	public String getOrigCrossID() {
		return origCrossID;
	}

	public void setOrigCrossID(String origCrossID) {
		this.origCrossID = origCrossID;
	}

}
