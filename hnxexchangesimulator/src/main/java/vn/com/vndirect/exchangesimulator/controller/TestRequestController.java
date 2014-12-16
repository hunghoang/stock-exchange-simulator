package vn.com.vndirect.exchangesimulator.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.TestRequestQueue;
import vn.com.vndirect.exchangesimulator.model.Heartbeat;
import vn.com.vndirect.exchangesimulator.model.TestRequest;

@Component
public class TestRequestController extends GatewayMessageController<TestRequest, Heartbeat>{

	@Autowired
	public TestRequestController(TestRequestQueue queueIn, QueueOutService<Heartbeat> queueOutService) {
		super(queueIn, queueOutService);
	}

	@Override
	protected Heartbeat process(TestRequest message) {
		Heartbeat heartMessage = new Heartbeat();
		heartMessage.setTargetCompID(message.getSenderCompID());
		heartMessage.setTestSeqID(message.getTestSeqID());
		return heartMessage;
	}

}
