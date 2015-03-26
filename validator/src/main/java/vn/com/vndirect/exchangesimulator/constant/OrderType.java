package vn.com.vndirect.exchangesimulator.constant;

public enum OrderType {
	LO('2'), ATC('5'), MOK('K'), MAK('A'), MTL('T');

	private char orderType;

	private OrderType(char orderType) {
		this.orderType = orderType;
	}

	public char orderType() {
		return orderType;
	}
}
