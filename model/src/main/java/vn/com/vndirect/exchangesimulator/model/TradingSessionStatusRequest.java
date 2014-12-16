package vn.com.vndirect.exchangesimulator.model;

import java.util.Date;

public class TradingSessionStatusRequest extends HnxMessage {
	private String text;
	private Boolean possDupFlag;
	private String tradingSessionID;
	private String tradSesReqID;
	private Integer tradSesMode;
	private Integer tradSesStatus;
	private Date tradSesStartTime;
	private Character subscriptionRequestType;

	public TradingSessionStatusRequest() {
		setMsgType("g");
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

	public String getTradingSessionID() {
		return tradingSessionID;
	}

	public void setTradingSessionID(String tradingSessionID) {
		this.tradingSessionID = tradingSessionID;
	}

	public String getTradSesReqID() {
		return tradSesReqID;
	}

	public void setTradSesReqID(String tradSesReqID) {
		this.tradSesReqID = tradSesReqID;
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

	public char getSubscriptionRequestType() {
		return subscriptionRequestType;
	}

	public void setSubscriptionRequestType(char subscriptionRequestType) {
		this.subscriptionRequestType = subscriptionRequestType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((possDupFlag == null) ? 0 : possDupFlag.hashCode());
		result = prime
				* result
				+ ((subscriptionRequestType == null) ? 0
						: subscriptionRequestType.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result
				+ ((tradSesMode == null) ? 0 : tradSesMode.hashCode());
		result = prime * result
				+ ((tradSesReqID == null) ? 0 : tradSesReqID.hashCode());
		result = prime
				* result
				+ ((tradSesStartTime == null) ? 0 : tradSesStartTime.hashCode());
		result = prime * result
				+ ((tradSesStatus == null) ? 0 : tradSesStatus.hashCode());
		result = prime
				* result
				+ ((tradingSessionID == null) ? 0 : tradingSessionID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradingSessionStatusRequest other = (TradingSessionStatusRequest) obj;
		if (possDupFlag == null) {
			if (other.possDupFlag != null)
				return false;
		} else if (!possDupFlag.equals(other.possDupFlag))
			return false;
		if (subscriptionRequestType == null) {
			if (other.subscriptionRequestType != null)
				return false;
		} else if (!subscriptionRequestType
				.equals(other.subscriptionRequestType))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (tradSesMode == null) {
			if (other.tradSesMode != null)
				return false;
		} else if (!tradSesMode.equals(other.tradSesMode))
			return false;
		if (tradSesReqID == null) {
			if (other.tradSesReqID != null)
				return false;
		} else if (!tradSesReqID.equals(other.tradSesReqID))
			return false;
		if (tradSesStartTime == null) {
			if (other.tradSesStartTime != null)
				return false;
		} else if (!tradSesStartTime.equals(other.tradSesStartTime))
			return false;
		if (tradSesStatus == null) {
			if (other.tradSesStatus != null)
				return false;
		} else if (!tradSesStatus.equals(other.tradSesStatus))
			return false;
		if (tradingSessionID == null) {
			if (other.tradingSessionID != null)
				return false;
		} else if (!tradingSessionID.equals(other.tradingSessionID))
			return false;
		return true;
	}

}
