package vn.com.vndirect.exchangesimulator.matching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;

@Component
public class ContinuousSessionAllOrderMatcher {
	private static final Logger log = Logger.getLogger(ContinuousSessionAllOrderMatcher.class);

	
	@Autowired
	private InMemory memory; 
	private Map<String, ContinuousSessionMatcher> matcherMap = new HashMap<String, ContinuousSessionMatcher>();
	
	private OrderMatcher orderMatcher = new OrderMatcher(new ContinuousReport());
	
	private OrderPriceIndex orderPriceIndex = new OrderPriceIndex();
	
	protected int getFloor(String symbol){
		SecurityStatus securityStatus = (SecurityStatus) memory.get("securitystatus", symbol);
		return (int) Math.floor(securityStatus.getLowPx());	}
	
	protected int getCeil(String symbol){
		SecurityStatus securityStatus = (SecurityStatus) memory.get("securitystatus", symbol);		
		return (int) Math.floor(securityStatus.getHighPx());
	}
	
	public void push(NewOrderSingle order){
		String symbol = order.getSymbol();
		if (!matcherMap.containsKey(symbol)){			
			matcherMap.put(symbol, new ContinuousSessionMatcher(symbol, getPriceRange(symbol), orderMatcher, orderPriceIndex));
		}
		matcherMap.get(symbol).push(order);
	}

	private PriceRange getPriceRange(String symbol) {
		int floorPrice = getFloor(symbol);
		int ceilingPrice = getCeil(symbol);
		return new PriceRange(floorPrice, ceilingPrice, 100);
	}

	public List<ExecutionReport> getExecutionReport(String symbol) {
		ContinuousSessionMatcher matcher = matcherMap.get(symbol);
		return matcher.getLastMatches();
	}
	
	public void clear(){
		matcherMap = new HashMap<String, ContinuousSessionMatcher>();
	}

	public void setMemory(InMemory memory) {
		this.memory = memory;
	}
	
}
