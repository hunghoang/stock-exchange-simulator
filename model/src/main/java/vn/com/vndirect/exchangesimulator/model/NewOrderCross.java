package vn.com.vndirect.exchangesimulator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewOrderCross extends HnxMessage {
	public static final char BUY = '1';
	public static final char SELL = '2';
	private Boolean possDupFlag;
	private String crossType;
	private String crossPrioritization;
	private String settlType;
	private String crossID;
	private String quoteID;
	private Date transactTime;
	private String symbol;
	private Character ordType;
	private Double price;
	private String orderId;
	private String origClOrdID;
	private Integer noSides;
	private String currentStatus;
	
	private List<GroupSide> groupSides = new ArrayList<GroupSide>();

	public String getOrigClOrdID() {
		return origClOrdID;
	}

	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdID = origClOrdID;
	}

	public NewOrderCross() {
		setMsgType("s");
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

	public char getOrdType() {
		return ordType;
	}

	public void setOrdType(char ordType) {
		this.ordType = ordType;
	}

	public double getPrice() {
		return Math.round(price == null ? 0 : price);
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCrossType() {
		return crossType;
	}

	public void setCrossType(String crossType) {
		this.crossType = crossType;
	}

	public String getCrossPrioritization() {
		return crossPrioritization;
	}

	public void setCrossPrioritization(String crossPrioritization) {
		this.crossPrioritization = crossPrioritization;
	}

	public String getSettlType() {
		return settlType;
	}

	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}

	public String getCrossID() {
		return crossID;
	}

	public void setCrossID(String crossID) {
		this.crossID = crossID;
	}

	public String getQuoteID() {
		return quoteID;
	}

	public void setQuoteID(String quoteID) {
		this.quoteID = quoteID;
	}

	public List<GroupSide> getGroupSides() {
		return groupSides;
	}

	public void setGroupSides(List<GroupSide> groupSides) {
		this.groupSides = groupSides;
	}

	public void addGroup(Group group) {
		groupSides.add((GroupSide) group);
	}

	public Integer getNoSides() {
		return groupSides.size();
	}

	public void setNoSides(Integer noSides) {
		this.noSides = noSides;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
} 