package vn.com.vndirect.exchangesimulator.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.ExecutionReportQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;

@Component
public class ExecutionReportController extends GatewayMessageController<ExecutionReport, ExecutionReport> {

	private static final Logger log = Logger.getLogger(ExecutionReportController.class);

	private InMemory memory;
	
	@Autowired
	public ExecutionReportController(ExecutionReportQueue queueIn, QueueOutService<ExecutionReport> queueOutService, InMemory memory) {
		super(queueIn, queueOutService);
		this.memory = memory;
	}

	@Override
	protected ExecutionReport process(ExecutionReport message) {
		Integer lastProccessedSeq = (Integer) memory.get("last_processed_sequence", message.getTargetCompID());
		if (lastProccessedSeq == null) {
			lastProccessedSeq = 0;
		}
		memory.put("last_processed_sequence", message.getTargetCompID(), ++lastProccessedSeq);
		message.setLastMsgSeqNumProcessed(lastProccessedSeq);
		return message;
	}
	
}
