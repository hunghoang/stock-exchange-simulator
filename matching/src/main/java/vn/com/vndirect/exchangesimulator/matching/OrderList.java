package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class OrderList {
	private List<NewOrderSingle> orders;

	
	public OrderList(List<NewOrderSingle> list){
		this.orders = list;
	}

	public OrderList(){
		orders = new ArrayList<NewOrderSingle>();
	}

	public static OrderList[] createList(String symbol, int size) {

		OrderList[] orders = new OrderList[size];
		for (int i = 0; i < orders.length; i++) {
			orders[i] = new OrderList();
		}
		return orders;
	}

	public NewOrderSingle get(int i) {
		return orders.get(i);
	}

	public int size() {
		return orders.size();
	}

	public void add(NewOrderSingle order) {
		this.orders.add(order);
	}
	
	public void addAll(OrderList list){
		this.orders.addAll(list.getList());
	}

	public List<NewOrderSingle> items() {
		return Collections.unmodifiableList(orders);
	}


	public void clearEmptyOrder() {
		Iterator<NewOrderSingle> it = orders.iterator();
		while (it.hasNext()) {
			if (it.next().getOrderQty() == 0) {
				it.remove();
			}
		}
	}

	public boolean isEmptyQuantity() {
		for (NewOrderSingle order : orders) {
			if (order.getOrderQty() != 0) {
				return false;
			}
		}
		return true;
	}

	public  List<NewOrderSingle> getList() {
		return orders;
	}
}
