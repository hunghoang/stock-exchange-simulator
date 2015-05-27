package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorageService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.ExecutionReportQueue;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

@Component
public class Matcher {

	private static final Logger log = Logger.getLogger(Matcher.class);

	@Autowired
	private ContinuousSessionAllOrderMatcher continuousSessionAllOrderMatcher;
	
	@Autowired
	private ATCSessionAllOrderMatcher atcSessionAllOrderMatcher;
	
	private boolean isATC;
	
	@Autowired
	private ExecutionReportQueue executionReportQueue;
	
	@Autowired
	private OrderStorageService orderStorageService;
	
	public List<ExecutionReport> push(NewOrderSingle order) {
		List<ExecutionReport> reports = new ArrayList<>();
		if (isATC) {
			atcSessionAllOrderMatcher.push(order);
		} else {
			continuousSessionAllOrderMatcher.push(order);
			reports.addAll(continuousSessionAllOrderMatcher.getExecutionReport(order.getSymbol()));
		}
		
		return reports;
	}
	
	public void cancelOrder(NewOrderSingle order) {
		order.setOrderQty(0);
	}
	
	public List<ExecutionReport> endATC() {
		return atcSessionAllOrderMatcher.processATC();
	}
	
	public void reset() {
		continuousSessionAllOrderMatcher.clear();
		atcSessionAllOrderMatcher.clear();
		isATC = false;
	}
	
	public void beginATC() {
		isATC = true;
		pushAllOrderToATCMatcher();
	}

	private void pushAllOrderToATCMatcher() {
		List<NewOrderSingle> orders = orderStorageService.getAllOrder();
		for(NewOrderSingle order : orders) {
			if (isNotATCOrder(order)) {
				if (order.getOrderQty() == 0) continue;
				atcSessionAllOrderMatcher.push(order);
			}
		}
	}

	private boolean isNotATCOrder(NewOrderSingle order) {
		return order.getOrdType() != '5';
	}
	
}
