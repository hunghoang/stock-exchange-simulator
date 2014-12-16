package vn.com.vndirect.exchangesimulator.model;

import java.util.Date;

public class NewOrderSingle extends HnxMessage{
	public static final char BUY = '1';
	public static final char SELL = '2';
	private String text;
	private Boolean possDupFlag;
	private String clOrdID;
	private String account;
	private Integer accountType;
	private Date transactTime;
	private String symbol;
	private Character side;
	private Integer orderQty;
	private Integer orderQty2;
	private Character ordType;
	private Double price;
	private Double stopPx;
	private String orderId;
	private Integer orgiQty;
	private String origClOrdID;

	public Integer getOrgiQty() {
		return orgiQty;
	}

	public void setOrgiQty(Integer orgiQty) {
		this.orgiQty = orgiQty;
	}

	
	public String getOrigClOrdID() {
		return origClOrdID;
	}

	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdID = origClOrdID;
	}
	
	
	public NewOrderSingle() {
		setMsgType("D");
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
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

	public double getStopPx() {
		return Math.round(stopPx);
	}

	public void setStopPx(double stopPx) {
		this.stopPx = stopPx;
	}
	
	public String getOrderId() {
		if (orderId == null) {
			return generateOrderId();
		}
		return orderId;
	}

	private String generateOrderId() {
		return symbol + getSenderCompID() + clOrdID;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result
				+ ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((clOrdID == null) ? 0 : clOrdID.hashCode());
		result = prime * result + ((ordType == null) ? 0 : ordType.hashCode());
		result = prime * result
				+ ((orderQty == null) ? 0 : orderQty.hashCode());
		result = prime * result
				+ ((orderQty2 == null) ? 0 : orderQty2.hashCode());
		result = prime * result
				+ ((possDupFlag == null) ? 0 : possDupFlag.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((side == null) ? 0 : side.hashCode());
		result = prime * result + ((stopPx == null) ? 0 : stopPx.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result
				+ ((transactTime == null) ? 0 : transactTime.hashCode());
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
		NewOrderSingle other = (NewOrderSingle) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (clOrdID == null) {
			if (other.clOrdID != null)
				return false;
		} else if (!clOrdID.equals(other.clOrdID))
			return false;
		if (ordType == null) {
			if (other.ordType != null)
				return false;
		} else if (!ordType.equals(other.ordType))
			return false;
		if (orderQty == null) {
			if (other.orderQty != null)
				return false;
		} else if (!orderQty.equals(other.orderQty))
			return false;
		if (orderQty2 == null) {
			if (other.orderQty2 != null)
				return false;
		} else if (!orderQty2.equals(other.orderQty2))
			return false;
		if (possDupFlag == null) {
			if (other.possDupFlag != null)
				return false;
		} else if (!possDupFlag.equals(other.possDupFlag))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (side == null) {
			if (other.side != null)
				return false;
		} else if (!side.equals(other.side))
			return false;
		if (stopPx == null) {
			if (other.stopPx != null)
				return false;
		} else if (!stopPx.equals(other.stopPx))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (transactTime == null) {
			if (other.transactTime != null)
				return false;
		} else if (!transactTime.equals(other.transactTime))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("accountNumber = ");
		sb.append(account);
		sb.append(" * ");
		
		sb.append("clOrdID = ");
		sb.append(clOrdID);
		sb.append(" * ");
		
		sb.append("symbol = ");
		sb.append(symbol);
		sb.append(" * ");
		
		sb.append("price = ");
		sb.append(price);
		sb.append(" * ");

		sb.append("orderQty = ");
		sb.append(orderQty);
		sb.append(" * ");
		
		sb.append("side = ");
		sb.append(side);
		
		return sb.toString();
	}

}
