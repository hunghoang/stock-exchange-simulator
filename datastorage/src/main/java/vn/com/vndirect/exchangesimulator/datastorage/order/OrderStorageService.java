package vn.com.vndirect.exchangesimulator.datastorage.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.CrossOrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.NewOrderCross;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

@Component
public class OrderStorageService {

	private List<NewOrderCross> newOrderCrossList = new ArrayList<NewOrderCross>();
	private List<CrossOrderCancelRequest> crossOrderCancelList = new ArrayList<CrossOrderCancelRequest>();
	
	public List<NewOrderSingle> getAllOrder() {
		List<OrderStorage> stores = StorageFactory.getAllStores();
		List<NewOrderSingle> results = new ArrayList<>();
		for (OrderStorage orderStorage : stores) {
			results.addAll(orderStorage.remain());
		}
		return results;
	}
	
	public void clearOrder() {
		StorageFactory.resetStorage();
		newOrderCrossList.clear();
		crossOrderCancelList.clear();
	}
	
	public List<NewOrderCross> getAllCrossOrder() {
		return newOrderCrossList;
	}

	public void addCrossOrder(NewOrderCross newOrderCross) {
		newOrderCrossList.add(newOrderCross);
	}

	public void addCancelCrossOrder(CrossOrderCancelRequest crossOrderCancelRequest) {
		for(int i = 0; i < crossOrderCancelList.size(); i++) {
			CrossOrderCancelRequest request = crossOrderCancelList.get(i);
			if (request.getOrderId().equals(crossOrderCancelRequest.getOrderId())) {
				crossOrderCancelList.remove(i);
				crossOrderCancelList.add(i, crossOrderCancelRequest);
			}
		}
	}

	public List<CrossOrderCancelRequest> getCrossOrderCancelList() {
		return crossOrderCancelList;
	}

	public void setCrossOrderCancelList(List<CrossOrderCancelRequest> crossOrderCancelList) {
		this.crossOrderCancelList = crossOrderCancelList;
	}

	public NewOrderCross getOrderCross(String crossID) {
		for(NewOrderCross orderCross : newOrderCrossList) {
			if (orderCross.getCrossID().equals(crossID)) {
				return orderCross;
			}
		}
		return null;
	}

	public void removeCrossOrder(NewOrderCross order) {
		newOrderCrossList.remove(order);
	}
}
