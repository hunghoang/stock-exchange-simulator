package vn.com.vndirect.exchangesimulator.datastorage.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class OrderStorage implements Storage<NewOrderSingle> {

	private Map<String, NewOrderSingle> map = new HashMap<>();
	
	@Override
	public void add(NewOrderSingle order) {
		map.put(order.getOrderId(), order);		
	}
	
	@Override
	public boolean remove(String key) {
		NewOrderSingle order = map.remove(key);
		return order != null;
	}

	@Override
	public List<NewOrderSingle> remain() {
		List<NewOrderSingle> result = new ArrayList<>();
		result.addAll(map.values());
		return result;   
	}

	@Override
	public NewOrderSingle get(String key) {
		return map.get(key);		
	}
	
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	@Override
	public int size() {
		return map.size();
	}
}
