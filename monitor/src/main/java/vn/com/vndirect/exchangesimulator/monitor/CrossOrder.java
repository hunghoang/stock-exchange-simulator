package vn.com.vndirect.exchangesimulator.monitor;

public class CrossOrder {
	public String msgType;
	public String orderId;
	public String symbol;
	public double price;
	public int quantity;
	public int crossType;
	public String firm1;
	public String account1;
	public String firm2;
	public String account2;
	public String orderStatus;
	public boolean fakedSeller;
}
