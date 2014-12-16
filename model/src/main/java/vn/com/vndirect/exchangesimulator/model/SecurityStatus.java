package vn.com.vndirect.exchangesimulator.model;

import java.util.Date;

public class SecurityStatus extends HnxMessage{
	private String text;
	private Boolean possDupFlag;
	private String tradingSessionSubID;
	private String securityStatusReqID;
	private String securityType;
	private Date maturityDate;
	private Date issueDate;
	private Double couponRate;
	private String issuer;
	private String securityDesc;
	private Double buyVolume;
	private String symbol;
	private Double lastPx;
	private Double highPx;
	private Double lowPx;
	private Integer securityTradingStatus;
	
	public SecurityStatus() {
		setMsgType("f");
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
	public String getTradingSessionSubID() {
		return tradingSessionSubID;
	}
	public void setTradingSessionSubID(String tradingSessionSubID) {
		this.tradingSessionSubID = tradingSessionSubID;
	}
	public String getSecurityStatusReqID() {
		return securityStatusReqID;
	}
	public void setSecurityStatusReqID(String securityStatusReqID) {
		this.securityStatusReqID = securityStatusReqID;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getSecurityType() {
		return securityType;
	}
	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}
	public Date getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public double getCouponRate() {
		return Math.round(couponRate);
	}
	public void setCouponRate(double couponRate) {
		this.couponRate = couponRate;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getSecurityDesc() {
		return securityDesc;
	}
	public void setSecurityDesc(String securityDesc) {
		this.securityDesc = securityDesc;
	}
	public double getHighPx() {
		return Math.round(highPx);
	}
	public void setHighPx(double highPx) {
		this.highPx = highPx;
	}
	public double getLowPx() {
		return Math.round(lowPx);
	}
	public void setLowPx(double lowPx) {
		this.lowPx = lowPx;
	}
	public double getLastPx() {
		return Math.round(lastPx);
	}
	public void setLastPx(double lastPx) {
		this.lastPx = lastPx;
	}
	public int getSecurityTradingStatus() {
		return securityTradingStatus;
	}
	public void setSecurityTradingStatus(int securityTradingStatus) {
		this.securityTradingStatus = securityTradingStatus;
	}
	public double getBuyVolume() {
		return Math.round(buyVolume);
	}
	public void setBuyVolume(double buyVolume) {
		this.buyVolume = buyVolume;
	}
	
	
	
}
