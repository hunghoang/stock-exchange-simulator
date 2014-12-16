package vn.com.vndirect.exchangesimulator.model;

public class SecurityStatusRequest extends HnxMessage {
	private Boolean possDupFlag;
	private String securityStatusReqID;
	private Character subscriptionRequestType;
	private String symbol;
	
	public SecurityStatusRequest() {
		setMsgType("e");
	}
	
	public boolean isPossDupFlag() {
		return possDupFlag;
	}

	public void setPossDupFlag(boolean possDupFlag) {
		this.possDupFlag = possDupFlag;
	}

	public String getSecurityStatusReqID() {
		return securityStatusReqID;
	}

	public void setSecurityStatusReqID(String securityStatusReqID) {
		this.securityStatusReqID = securityStatusReqID;
	}

	public char getSubscriptionRequestType() {
		return subscriptionRequestType;
	}

	public void setSubscriptionRequestType(char subscriptionRequestType) {
		this.subscriptionRequestType = subscriptionRequestType;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((possDupFlag == null) ? 0 : possDupFlag.hashCode());
		result = prime
				* result
				+ ((securityStatusReqID == null) ? 0 : securityStatusReqID
						.hashCode());
		result = prime
				* result
				+ ((subscriptionRequestType == null) ? 0
						: subscriptionRequestType.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		SecurityStatusRequest other = (SecurityStatusRequest) obj;
		if (possDupFlag == null) {
			if (other.possDupFlag != null)
				return false;
		} else if (!possDupFlag.equals(other.possDupFlag))
			return false;
		if (securityStatusReqID == null) {
			if (other.securityStatusReqID != null)
				return false;
		} else if (!securityStatusReqID.equals(other.securityStatusReqID))
			return false;
		if (subscriptionRequestType == null) {
			if (other.subscriptionRequestType != null)
				return false;
		} else if (!subscriptionRequestType
				.equals(other.subscriptionRequestType))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

}
