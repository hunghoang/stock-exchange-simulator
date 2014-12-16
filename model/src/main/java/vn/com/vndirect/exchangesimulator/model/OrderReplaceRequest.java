package vn.com.vndirect.exchangesimulator.model;

import java.util.Date;

public class OrderReplaceRequest extends HnxMessage{
	private String text;
	private Boolean possDupFlag;
	private String clOrdID;
	private String origClOrdID;
	private String symbol;
	private Date  transactTime;
	private Double price;
	private Integer orderQty;
	private Integer cashOrderQty;
	private Integer orderQty2;
	private Double stopPx;
	
	public OrderReplaceRequest() {
		setMsgType("G");
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
	public double getPrice() {
		return Math.round(price);
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getOrderQty() {
		return orderQty == null ? 0 : orderQty;
	}
	
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}
	public int getCashOrderQty() {
		return cashOrderQty == null ? 0 : cashOrderQty;
	}
	public void setCashOrderQty(int cashOrderQty) {
		this.cashOrderQty = cashOrderQty;
	}
	public int getOrderQty2() {
		return orderQty2;
	}
	public void setOrderQty2(int orderQty2) {
		this.orderQty2 = orderQty2;
	}
	public double getStopPx() {
		return Math.round(stopPx);
	}
	public void setStopPx(double stopPx) {
		this.stopPx = stopPx;
	}
	
	
}
