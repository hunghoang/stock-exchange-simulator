package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.validator.NewOrderSingleValidator;
import vn.com.vndirect.exchangesimulator.validator.PriceValidator;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateCode;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class ReplaceOrderProcessor implements Processor {

	private Storage<NewOrderSingle> storage;

	private Matcher matcher;
	
	private NewOrderSingleValidator validator;

	public ReplaceOrderProcessor(Storage<NewOrderSingle> orderStorage, Matcher matcher, NewOrderSingleValidator validator) {
		this.storage = orderStorage;
		this.matcher = matcher;
		this.validator = validator;
	}

	@Override
	public List<ExecutionReport> process(HnxMessage message) {
		List<ExecutionReport> reports = new ArrayList<>();
		OrderReplaceRequest request = (OrderReplaceRequest) message;
		NewOrderSingle origOrder = storage.get(request.getOrigClOrdID());
		try {
			if (!validate(request, origOrder)) {
				return buildRejectReport(request, origOrder, new ValidateException("","Order is rejected"));
			}
		} catch (ValidateException e) {
			return buildRejectReport(request, origOrder, e);
		}

		ExecutionReport report = buildReplacedExecutionReport(request, origOrder);

		reports.add(report);

		if (isModifyPriceOrIncreaseQuantity(request, origOrder)) {
			NewOrderSingle newOrder = generateNewOrder(request, origOrder);
			removeOldOrder(origOrder);
			storage.add(newOrder);
			report.setOrderID(newOrder.getOrderId());
			reports.addAll(matcher.push(newOrder));
		} else {
			updateNewQuantity(request, origOrder);
		}
		return reports;
	}

	private void removeOldOrder(NewOrderSingle origOrder) {
		matcher.cancelOrder(origOrder);
		storage.remove(origOrder.getOrderId());
	}

	private void updateNewQuantity(OrderReplaceRequest request, NewOrderSingle origOrder) {
		origOrder.setOrderQty(getReplaceQuantity(request.getCashOrderQty(), request.getOrderQty(), origOrder.getOrderQty()));
	}

	protected boolean isModifyPriceOrIncreaseQuantity(
			OrderReplaceRequest request, NewOrderSingle origOrder) {
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
		newOrderSingle.setOrderId(origOrder.getOrderId());
		newOrderSingle.setMsgSeqNum(request.getMsgSeqNum());
		newOrderSingle.setSenderCompID(request.getSenderCompID());
		newOrderSingle.setSide(origOrder.getSide());
		newOrderSingle.setOrdType(origOrder.getOrdType());
		newOrderSingle.setAccount(origOrder.getAccount());
		newOrderSingle.setPrice(request.getPrice());
		newOrderSingle.setOrderQty(getReplaceQuantity(request.getCashOrderQty(), request.getOrderQty(),	origOrder.getOrderQty()));

		return newOrderSingle;
	}
	
	protected ExecutionReport buildReplacedExecutionReport(OrderReplaceRequest request, NewOrderSingle origOrder) {
		ExecutionReport executionReport = new ExecutionReport();
		executionReport.setTargetCompID(request.getSenderCompID());
		executionReport.setOrderID(origOrder.getOrderId());
		executionReport.setClOrdID(request.getClOrdID());
		executionReport.setOrigClOrdID(request.getClOrdID());
		executionReport.setExecType(ExecType.REPLACE);
		executionReport.setOrdStatus(OrdStatus.CANCELORREPLACE);
		executionReport.setOrderID(origOrder.getOrderId());
		executionReport.setLastQty(getReplaceQuantity(request.getCashOrderQty(), request.getOrderQty(), origOrder.getOrderQty()));
		executionReport.setLeavesQty(getLeaveQuantity(request.getCashOrderQty(), request.getOrderQty()));
		executionReport.setLastPx(request.getPrice());
		executionReport.setSymbol(request.getSymbol());
		executionReport.setSide(origOrder.getSide());
		executionReport.setOrdType(origOrder.getOrdType());
		executionReport.setAccount(origOrder.getAccount());
		return executionReport;
	}

	private int getLeaveQuantity(int cashOrderQty, int orderQty) {
		if (cashOrderQty > 0) {
			return cashOrderQty - orderQty;
		}
		return 0;
	}

	private int getReplaceQuantity(int cashOrderQty, int orderQty, int originOrderQty) {
		if (cashOrderQty > 0) {
			return originOrderQty + (cashOrderQty - orderQty);
		}
		return originOrderQty;
	}

	private List<ExecutionReport> buildRejectReport(OrderReplaceRequest request, NewOrderSingle originOrder, ValidateException e) {
		ExecutionReport report = new ExecutionReport();
		report.setTargetCompID(request.getSenderCompID());
		report.setClOrdID(request.getClOrdID());
		report.setOrigClOrdID(request.getClOrdID());
		report.setMsgType("3");
		report.setExecType(ExecType.REJECT);
		report.setOrdRejReason("5");
		report.setOrdStatus(OrdStatus.REJECT);
		report.setRefSeqNum(request.getMsgSeqNum());
		report.setLastPx(request.getPrice());
		report.setSymbol(request.getSymbol());
		if (originOrder == null) {
			report.setText("order is not existed or filled");
			report.setSessionRejectReason(ValidateCode.UNKNOWN_ORDER.code());
		} else {
			report.setOrdType(originOrder.getOrdType());
			report.setSide(originOrder.getSide());
			report.setText(e.getMessage());
			report.setSessionRejectReason(e.getCode());
		}
		report.setRefMsgType(request.getMsgType());
		report.setOrderID(request.getClOrdID());
		report.setUnderlyingLastQty(0);

		return Collections.singletonList(report);
	}

	private boolean validate(OrderReplaceRequest request,
			NewOrderSingle origOrder) throws ValidateException {
		if (origOrder == null || origOrder.getOrderQty() == 0) {
			return false;
		}

		if (isReplaceWithExpectedDecreasingQuantityBiggerThanCurrentQuantity(request.getOrderQty(), request.getCashOrderQty(), origOrder.getOrderQty())) {
			throw new ValidateException(ValidateCode.INVALID_QUANTITY.code(), ValidateCode.INVALID_QUANTITY.message());
		}
		
		PriceValidator priceValidator = validator.getPriceValidator();
		//priceValidator.validate(request.getSymbol(), request.getPrice());
		return true;
	}

	private boolean isReplaceWithExpectedDecreasingQuantityBiggerThanCurrentQuantity(int orderQty, int cashOrdQty, int origOrdQty) {
		return orderQty - cashOrdQty >= origOrdQty;
	}

}
