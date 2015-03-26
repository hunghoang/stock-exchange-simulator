package vn.com.vndirect.exchangesimulator.model;

import java.util.Date;

public class ExecutionReport extends HnxMessage{
	private String text;
	private String refMsgType;
	private Character execType;
	private Boolean possDupFlag;
	private Character ordStatus;
	private String orderID;
	private Date transactTime;
	private String clOrdID;
	private String symbol;
	private Character side;
	private Integer orderQty;
	private Integer orderQty2;
	private Character ordType;
	private Double price;
	private String account;
	private Double stopPx;
	private Integer leavesQty;
	private String origClOrdID;
	private Integer lastQty;
	private Double lastPx;
	private String secondaryClOrdID;
	private String execID;
	private String ordRejReason;
	private Double underlyingLastQty;
	private int refSeqNum;
	private String sessionRejectReason;
	
	public ExecutionReport() {
		setMsgType("8");
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public char getExecType() {
		return execType;
	}
	public void setExecType(char execType) {
		this.execType = execType;
	}

	public boolean isPossDupFlag() {
		return possDupFlag;
	}
	public void setPossDupFlag(boolean possDupFlag) {
		this.possDupFlag = possDupFlag;
	}
	public char getOrdStatus() {
		return ordStatus;
	}
	public void setOrdStatus(char ordStatus) {
		this.ordStatus = ordStatus;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public Date getTransactTime() {
		return transactTime;
	}
	public void setTransactTime(Date transactTime) {
		this.transactTime = transactTime;
	}
	public String getClOrdID() {
		return clOrdID;
	}
	public void setClOrdID(String clOrdID) {
		this.clOrdID = clOrdID;
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
	public int getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}
	public int getOrderQty2() {
		return orderQty2;
	}
	public void setOrderQty2(int orderQty2) {
		this.orderQty2 = orderQty2;
	}
	public char getOrdType() {
		return ordType;
	}
	public void setOrdType(char ordType) {
		this.ordType = ordType;
	}
	public double getPrice() {
		return Math.round(price);
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public double getStopPx() {
		return Math.round(stopPx);
	}
	public void setStopPx(double stopPx) {
		this.stopPx = stopPx;
	}
	public int getLeavesQty() {
		return leavesQty;
	}
	/**
     * Tag: 151<br>
     * case Replace: new quantity - origin quantity
     */
	public void setLeavesQty(int leavesQty) {
		this.leavesQty = leavesQty;
	}
	public String getOrigClOrdID() {
		return origClOrdID;
	}
	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdID = origClOrdID;
	}
	public int getLastQty() {
		return lastQty;
	}
	/**
	 * Tag: 32<br>
	 * case Replace: new quantity
	 */
	public void setLastQty(int lastQty) {
		this.lastQty = lastQty;
	}
	public double getLastPx() {
		return Math.round(lastPx);
	}
	/**
     * Tag: 31<br>
     * case Replace: new price
     */
	public void setLastPx(double lastPx) {
		this.lastPx = lastPx;
	}
	public String getSecondaryClOrdID() {
		return secondaryClOrdID;
	}
	public void setSecondaryClOrdID(String secondaryClOrdID) {
		this.secondaryClOrdID = secondaryClOrdID;
	}
	public String getExecID() {
		return execID;
	}
	public void setExecID(String execID) {
		this.execID = execID;
	}
	public String getOrdRejReason() {
		return ordRejReason;
	}
	public void setOrdRejReason(String ordRejReason) {
		this.ordRejReason = ordRejReason;
	}
	public double getUnderlyingLastQty() {
		return Math.round(underlyingLastQty);
	}
	public void setUnderlyingLastQty(double underlyingLastQty) {
		this.underlyingLastQty = underlyingLastQty;
	}

	@Override
	public String toString() {
		return "ExecutionReport [text=" + text + ", execType=" + execType
				+ ", ordStatus=" + ordStatus + ", orderID=" + orderID
				+ ", clOrdID=" + clOrdID + ", symbol=" + symbol + ", side="
				+ side + ", orderQty=" + orderQty + ", ordType=" + ordType
				+ ", price=" + price + ", account=" + account + ", stopPx="
				+ stopPx + ", leavesQty=" + leavesQty + ", origClOrdID="
				+ origClOrdID + ", lastQty=" + lastQty + ", lastPx=" + lastPx
				+ ", secondaryClOrdID=" + secondaryClOrdID + ", execID="
				+ execID + ", ordRejReason=" + ordRejReason
				+ ", underlyingLastQty=" + underlyingLastQty + "]";
	}

	public String getRefMsgType() {
		return refMsgType;
	}

	public void setRefMsgType(String refMsgType) {
		this.refMsgType = refMsgType;
	}

	public int getRefSeqNum() {
		return refSeqNum;
	}

	public void setRefSeqNum(int refSeqNum) {
		this.refSeqNum = refSeqNum;
	}

	public String getSessionRejectReason() {
		return sessionRejectReason;
	}

	public void setSessionRejectReason(String sessionRejectReason) {
		this.sessionRejectReason = sessionRejectReason;
	}
	
}
