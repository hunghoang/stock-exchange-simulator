package vn.com.vndirect.exchangesimulator.model;

public class GroupSide extends Group {
	private Character side;
	private String clOrdID;
	private Integer noPartyIDs;
	private String partyID;
	private String account;
	private Integer orderQty;
	private String accountType;

	public Character getSide() {
		return side;
	}

	public void setSide(Character side) {
		this.side = side;
	}

	public String getClOrdID() {
		return clOrdID;
	}

	public void setClOrdID(String clOrdID) {
		this.clOrdID = clOrdID;
	}

	public Integer getNoPartyIDs() {
		return noPartyIDs;
	}

	public void setNoPartyIDs(Integer noPartyIDs) {
		this.noPartyIDs = noPartyIDs;
	}

	public String getPartyID() {
		return partyID;
	}

	public void setPartyID(String partyID) {
		this.partyID = partyID;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

}
