package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;

public class ReplaceOrderProcessor implements Processor {
	
	private Storage<NewOrderSingle> storage;
	
	private Matcher matcher;
	
	public ReplaceOrderProcessor(Storage<NewOrderSingle> orderStorage, Matcher matcher) {
		this.storage = orderStorage;
		this.matcher = matcher;
	}
	
	@Override
	public List<ExecutionReport> process(HnxMessage message) {
		List<ExecutionReport> reports = new ArrayList<>();
		OrderReplaceRequest request = (OrderReplaceRequest) message;
		NewOrderSingle origOrder = storage.get(request.getOrigClOrdID());
		if (!validate(request, origOrder)) {
			return buildRejectReport(request, origOrder);
		}
		
		ExecutionReport report = buildReplacedExecutionReport(request, origOrder);
		
		reports.add(report);
		if (isModifyPriceOrIncreaseQuantity(request, origOrder)) {
			NewOrderSingle newOrder = generateNewOrder(request, origOrder);
			matcher.cancelOrder(origOrder);
			storage.remove(origOrder.getOrderId());
			report.setOrderID(newOrder.getOrderId());
			if (newOrder != null) {
				reports.addAll(matcher.push(newOrder));
				storage.add(newOrder);
			}
		} else {
			updateNewQuantity(request, origOrder);
		}
		return reports;
	}

	private void updateNewQuantity(OrderReplaceRequest request, NewOrderSingle origOrder) {
		origOrder.setOrderQty(getReplaceQuantity(request.getCashOrderQty(), request.getOrderQty(), origOrder.getOrderQty()));
		origOrder.setOrgiQty(request.getCashOrderQty());
	}

	protected boolean isModifyPriceOrIncreaseQuantity(OrderReplaceRequest request, NewOrderSingle origOrder) {
		if (request.getPrice() != origOrder.getPrice()) {
			return true;
		}
		
		if (request.getCashOrderQty() > request.getOrderQty()) {
			return true;
		}
		
		return false;
	}

	protected NewOrderSingle generateNewOrder(OrderReplaceRequest request, NewOrderSingle origOrder) {
		NewOrderSingle newOrderSingle = new NewOrderSingle();
		newOrderSingle.setSymbol(origOrder.getSymbol());
		newOrderSingle.setClOrdID(request.getClOrdID());
		newOrderSingle.setOrderId(request.getClOrdID());
		newOrderSingle.setSenderCompID(request.getSenderCompID());
		newOrderSingle.setSide(origOrder.getSide());
		newOrderSingle.setOrdType(origOrder.getOrdType());
		newOrderSingle.setAccount(origOrder.getAccount());
		newOrderSingle.setPrice(request.getPrice());
		newOrderSingle.setOrderQty(getReplaceQuantity(request.getCashOrderQty(), request.getOrderQty(), origOrder.getOrderQty()));
		
		return newOrderSingle;
	}

	protected ExecutionReport buildReplacedExecutionReport(OrderReplaceRequest request, NewOrderSingle origOrder) {
		ExecutionReport report = new ExecutionReport();
		report.setTargetCompID(request.getSenderCompID());
		report.setOrderID(origOrder.getOrderId());
		report.setClOrdID(request.getClOrdID());
		report.setOrigClOrdID(request.getClOrdID());
		report.setExecType('5');
		report.setOrdStatus('3');
		report.setOrderID(origOrder.getOrderId());
		report.setLastQty(request.getCashOrderQty());
		report.setLeavesQty(getReplaceQuantity(request.getCashOrderQty(), request.getOrderQty(), origOrder.getOrderQty()));
		report.setLastPx(request.getPrice()); 
		report.setSymbol(request.getSymbol());
		report.setSide(origOrder.getSide());
		report.setOrdType(origOrder.getOrdType());
		report.setAccount(origOrder.getAccount());
		return report;
	}

	private int getReplaceQuantity(int cashOrderQty, int orderQty, int originOrderQty) {
		if (cashOrderQty > 0) {
			return cashOrderQty + originOrderQty - orderQty;
		}
		return originOrderQty;
	}

	private List<ExecutionReport> buildRejectReport(OrderReplaceRequest request, NewOrderSingle originOrder) {
		ExecutionReport report = new ExecutionReport();
		report.setTargetCompID(request.getSenderCompID());
		report.setClOrdID(request.getClOrdID());
		report.setOrigClOrdID(request.getClOrdID());
		report.setExecType('8');
		report.setOrdRejReason("5");
		report.setOrdStatus('8');
		report.setLastPx(request.getPrice()); 
		report.setSymbol(request.getSymbol());
		report.setOrdType(originOrder.getOrdType());
		report.setSide(originOrder.getSide());
		report.setOrderID(originOrder.getOrderId());
		report.setUnderlyingLastQty(request.getCashOrderQty() - request.getOrderQty());
		
		return Collections.singletonList(report);
	}

	private boolean validate(OrderReplaceRequest request, NewOrderSingle origOrder) {
		if (origOrder == null) {
			return false;
		}
		
		if (request.getOrderQty() - request.getCashOrderQty() > origOrder.getOrderQty()) {
			return false;
		}
		return true;
	}

}
