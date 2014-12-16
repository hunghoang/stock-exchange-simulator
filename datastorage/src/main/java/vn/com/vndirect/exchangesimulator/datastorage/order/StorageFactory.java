package vn.com.vndirect.exchangesimulator.datastorage.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StorageFactory {

	private StorageFactory() {
		
	}
	
	private static Map<String, OrderStorage> storeMap = new HashMap<>();
	
	public static OrderStorage getStore(String symbol) {
		OrderStorage storage = storeMap.get(symbol);
		if (storage == null) {
			storage = new OrderStorage();
			storeMap.put(symbol, storage);
		}
		
		return storage;
	}
	
	public static List<OrderStorage> getAllStores(){
		List<OrderStorage> stores = new ArrayList<>();
		stores.addAll(storeMap.values());
		return stores;
	}

}
