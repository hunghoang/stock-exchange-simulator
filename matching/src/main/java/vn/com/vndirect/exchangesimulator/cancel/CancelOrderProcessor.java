package vn.com.vndirect.exchangesimulator.cancel;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.ExecutionReportQueue;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;

@Component
public class CancelOrderProcessor implements Processor<OrderCancelRequest>{

	private static final Logger LOG = Logger.getLogger(CancelOrderProcessor.class);

	private InMemory memory;
	private ExecutionReportQueue queueOut;
	
	@Autowired
	public CancelOrderProcessor(InMemory memory, ExecutionReportQueue queueOut) {
		this.memory = memory;
		this.queueOut = queueOut;
	}

	public ExecutionReport generate(OrderCancelRequest request) {
		String originClOrdId = request.getOrigClOrdID();
		NewOrderSingle newOrderSingle = (NewOrderSingle) memory.get(NewOrderSingle.class.getSimpleName(), originClOrdId);
		ExecutionReport executionReport = new ExecutionReport();
		if (newOrderSingle == null) {
			LOG.error("Root order does not exist for request: " + request);
			return executionReport;
		}
		executionReport.setTargetCompID(newOrderSingle.getSenderCompID());
		executionReport.setExecType('4');
		executionReport.setOrdStatus('3');
		executionReport.setOrderID(newOrderSingle.getOrderId());
		// TODO Get remain quantity of root order
		executionReport.setLeavesQty(newOrderSingle.getOrderQty());
		executionReport.setClOrdID(request.getClOrdID());
		executionReport.setOrigClOrdID(request.getClOrdID());
		executionReport.setSymbol(newOrderSingle.getSymbol());
		executionReport.setSide(newOrderSingle.getSide());
		executionReport.setOrdType(newOrderSingle.getOrdType());
		executionReport.setAccount(newOrderSingle.getAccount());
		return executionReport;
	}

	@Override
	public void process(OrderCancelRequest order) {
		ExecutionReport report = generate(order);
		send(report);
	}
	
	protected void send(ExecutionReport report) {
		queueOut.add(report);
	}
}
