package vn.com.vndirect.exchangesimulator.processor;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.validator.NewOrderSingleValidator;
import vn.com.vndirect.exchangesimulator.validator.SessionValidator;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class CancelOrderProcessor implements Processor {
	private final static Logger LOG = Logger.getLogger(CancelOrderProcessor.class);
	private Storage<NewOrderSingle> storage;
	private Matcher matcher;
	private NewOrderSingleValidator validator;

	public CancelOrderProcessor(Storage<NewOrderSingle> orderStorage, Matcher matcher,
			NewOrderSingleValidator validator) {
		this.storage = orderStorage;
		this.matcher = matcher;
		this.validator = validator;
	}

	@Override
	public List<ExecutionReport> process(HnxMessage message) {
		OrderCancelRequest request = (OrderCancelRequest) message;
		ExecutionReport report = cancelOrder(request);
		removeOrder(request);
		return Collections.singletonList(report);
	}

	private void removeOrder(OrderCancelRequest request) {
		storage.remove(request.getOrigClOrdID());
	}

	private ExecutionReport cancelOrder(OrderCancelRequest request) {
		String originClOrdId = request.getOrigClOrdID();
		NewOrderSingle newOrderSingle = storage.get(originClOrdId);
		
		try {
			if (newOrderSingle == null) throw new ValidateException("Order not found");
			validate(request, newOrderSingle);
			ExecutionReport executionReport = buildCanceledExecutionReport(request, newOrderSingle);
			matcher.cancelOrder(newOrderSingle);
			return executionReport;
		} catch (ValidateException e) {
			return buildRejectReport(request, newOrderSingle, e.getCode()).get(0);
		}
	}

	protected void validate(OrderCancelRequest request, NewOrderSingle newOrderSingle)
			throws ValidateException {
		if (newOrderSingle == null || newOrderSingle.getOrderQty() == 0) {
			LOG.error("Order is not existed or fill: " + request);
			throw new ValidateException("Order is not existed or fill");
		}
		validateCancelRequest(request, newOrderSingle);
	}
	
	protected void validateCancelRequest(OrderCancelRequest request, NewOrderSingle newOrderSingle)
			throws ValidateException {
		SessionValidator sessionValidator = validator.getSessionValidator();
		sessionValidator.validate(request);
	}

	private ExecutionReport buildCanceledExecutionReport(OrderCancelRequest request, NewOrderSingle newOrderSingle) {
		ExecutionReport executionReport = new ExecutionReport();
		executionReport.setTargetCompID(newOrderSingle.getSenderCompID());
		executionReport.setExecType(ExecType.CANCEL);
		executionReport.setOrdStatus(OrdStatus.CANCELORREPLACE);
		executionReport.setOrderID('f' + newOrderSingle.getOrderId());
		executionReport.setLeavesQty(newOrderSingle.getOrderQty());
		executionReport.setClOrdID(request.getClOrdID());
		executionReport.setOrigClOrdID(request.getOrigClOrdID());
		executionReport.setSymbol(newOrderSingle.getSymbol());
		executionReport.setSide(newOrderSingle.getSide());
		executionReport.setOrdType(newOrderSingle.getOrdType());
		executionReport.setAccount(newOrderSingle.getAccount());
		executionReport.setPrice(newOrderSingle.getPrice());
		return executionReport;
	}

	private List<ExecutionReport> buildRejectReport(OrderCancelRequest request, NewOrderSingle originOrder,
			String text) {
		ExecutionReport executionReport = new ExecutionReport();
		executionReport.setTargetCompID(request.getSenderCompID());
		executionReport.setClOrdID(request.getClOrdID());
		executionReport.setOrigClOrdID(request.getClOrdID());
		executionReport.setMsgType("3");
		executionReport.setRefSeqNum(request.getMsgSeqNum());
		executionReport.setExecType(ExecType.REJECT);
		executionReport.setOrdRejReason("5");
		executionReport.setOrdStatus(OrdStatus.REJECT);
		executionReport.setOrderID(request.getClOrdID());
		if (originOrder != null && originOrder.getOrderQty() > 0) {
			executionReport.setOrdType(originOrder.getOrdType());
			executionReport.setSide(originOrder.getSide());
			executionReport.setOrdType(originOrder.getOrdType());
			executionReport.setSide(originOrder.getSide());
		}
		executionReport.setSessionRejectReason("Invalid order");
		executionReport.setText("order is rejected: " + text);
		executionReport.setRefMsgType(request.getMsgType());
		executionReport.setUnderlyingLastQty(0);

		return Collections.singletonList(executionReport);
	}

}
