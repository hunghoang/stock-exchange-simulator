package vn.com.vndirect.exchangesimulator.constant;

public enum Side {
	BUY('1'), SELL('2');
	private char side;

	private Side(char side) {
		this.side = side;
	}

	public char side() {
		return side;
	}
}
