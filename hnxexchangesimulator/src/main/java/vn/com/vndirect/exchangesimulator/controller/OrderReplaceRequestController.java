package vn.com.vndirect.exchangesimulator.controller;

import java.util.Date;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.OrderReplaceRequestQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;

public class OrderReplaceRequestController extends GatewayMessageController<OrderReplaceRequest, ExecutionReport>{
	
	private InMemory memory;

	public OrderReplaceRequestController(OrderReplaceRequestQueue orderReplaceRequestQueue, QueueOutService<ExecutionReport> queueOutService, InMemory memory) {
		super(orderReplaceRequestQueue, queueOutService);
		this.memory = memory;
	}

	@Override
	protected ExecutionReport process(OrderReplaceRequest messageIn) {
		Integer lastProccessedSeq = updateSequence(messageIn);
		ExecutionReport executionReport = buildExecutionReport(messageIn, lastProccessedSeq);
		return executionReport;
	}
	
	private ExecutionReport buildExecutionReport(OrderReplaceRequest message, int lastProccessedSeq) {
		String orderId = message.getOrigClOrdID();
		ExecutionReport rootOrderReport = (ExecutionReport) memory.get("ExecutionReport", orderId);
		// goi validator
		// put vao matching
		ExecutionReport executionReport = new ExecutionReport();
		executionReport.setTargetCompID(message.getSenderCompID());
		executionReport.setOrderID(orderId);
		executionReport.setClOrdID(rootOrderReport.getOrderID());
		executionReport.setAccount(rootOrderReport.getAccount());
		executionReport.setSide(rootOrderReport.getSide());
		executionReport.setOrigClOrdID(rootOrderReport.getOrderID());
		executionReport.setOrdType(rootOrderReport.getOrdType());
		executionReport.setLastQty(rootOrderReport.getLastQty());
		executionReport.setLastPx(rootOrderReport.getLastPx());
		executionReport.setLastMsgSeqNumProcessed(lastProccessedSeq);
		executionReport.setPrice(message.getPrice());
		executionReport.setOrderQty(message.getOrderQty());
		executionReport.setSymbol(message.getSymbol());
		executionReport.setTransactTime(new Date());
		executionReport.setOrdStatus('3');
		executionReport.setExecType('5');
		executionReport.setLeavesQty(0);
		memory.put("ExecutionReport", orderId, executionReport);
		return executionReport;
	}

	private Integer updateSequence(OrderReplaceRequest message) {
		Integer lastProccessedSeq = (Integer) memory.get("last_processed_sequence", message.getSenderCompID());
		memory.put("last_processed_sequence", message.getSenderCompID(), ++lastProccessedSeq);
		return lastProccessedSeq;
	}

}
