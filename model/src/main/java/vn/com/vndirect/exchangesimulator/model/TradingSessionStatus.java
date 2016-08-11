package vn.com.vndirect.exchangesimulator.model;

import java.util.Date;

public class TradingSessionStatus extends HnxMessage {
	private String text;
	private Boolean possDupFlag;
	private String tradSesReqID;
	private String tradingSessionID;
	private int tradSesMode;
	private int tradSesStatus;
	private Date tradSesStartTime;
	
	public TradingSessionStatus() {
		setMsgType("h");
	}
	
	private TradSesStatus tradingSessionCode;
	
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

	public String getTradSesReqID() {
		return tradSesReqID;
	}

	public void setTradSesReqID(String tradSesReqID) {
		this.tradSesReqID = tradSesReqID;
	}

	public String getTradingSessionID() {
		return tradingSessionID;
	}

	public void setTradingSessionID(String tradingSessionID) {
		this.tradingSessionID = tradingSessionID;
	}

	public int getTradSesMode() {
		return tradSesMode;
	}

	public void setTradSesMode(int tradSesMode) {
		this.tradSesMode = tradSesMode;
	}

	public int getTradSesStatus() {
		return tradSesStatus;
	}

	public void setTradSesStatus(int tradSesStatus) {
		this.tradSesStatus = tradSesStatus;
	}

	public Date getTradSesStartTime() {
		return tradSesStartTime;
	}

	public void setTradSesStartTime(Date tradSesStartTime) {
		this.tradSesStartTime = tradSesStartTime;
	}

	public TradSesStatus getTradingSessionCode() {
		return tradingSessionCode;
	}

	public void setTradingSessionCode(TradSesStatus tradingSessionCode) {
		this.tradingSessionCode = tradingSessionCode;
	}

}
