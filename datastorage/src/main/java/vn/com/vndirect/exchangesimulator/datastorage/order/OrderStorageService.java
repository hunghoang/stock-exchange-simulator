package vn.com.vndirect.exchangesimulator.datastorage.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorage;
import vn.com.vndirect.exchangesimulator.datastorage.order.StorageFactory;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

@Component
public class OrderStorageService {

	public List<NewOrderSingle> getAllOrder() {
		List<OrderStorage> stores = StorageFactory.getAllStores();
		List<NewOrderSingle> results = new ArrayList<>();
		for (OrderStorage orderStorage : stores) {
			results.addAll(orderStorage.remain());
		}
		return results;
	}
}
