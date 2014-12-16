package vn.com.vndirect.exchangesimulator.controller;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.datastorage.queue.AbstractQueueService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueListener;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;

public abstract class GatewayMessageController<I, O> implements QueueListener {
	
	private static final Logger log = Logger.getLogger(GatewayMessageController.class);
	
	protected AbstractQueueService<I> queueIn;
	
	protected QueueOutService<O> queueOutService;
	
	protected boolean isActive;
	
	public GatewayMessageController(AbstractQueueService<I> queueIn, QueueOutService<O> queueOutService) {
		this.queueIn = queueIn;
		this.queueOutService = queueOutService;
		queueIn.addListener(this);
	}
	
	public void onEvent(Object source) {
		O messageOut = process((I) source);
		response(messageOut);
	}
	
	protected abstract O process(I messageIn);
	
	protected void response(O messageOut) {
		queueOutService.add(messageOut);
	}
	
	public void stop() {
		isActive = false;
	}
}
