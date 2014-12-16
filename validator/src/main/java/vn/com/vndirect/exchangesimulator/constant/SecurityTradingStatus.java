package vn.com.vndirect.exchangesimulator.constant;

public enum SecurityTradingStatus {
	NEW(1), SUSPENDED(2), CANCELED(4), NORMAL(17), HALTED(19);

	private int status;

	private SecurityTradingStatus(int status) {
		this.status = status;
	}

	public int status() {
		return status;
	}
}
