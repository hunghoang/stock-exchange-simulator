package vn.com.vndirect.exchangesimulator.controller;

import java.util.List;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.datastorage.queue.AbstractQueueService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;

public abstract class GatewayMultiMessageOutController<I, O> {
	
	private static final Logger log = Logger.getLogger(GatewayMultiMessageOutController.class);
	
	protected AbstractQueueService<I> queueIn;
	
	protected QueueOutService<O> queueOutService;
	
	protected boolean isActive;
	
	public GatewayMultiMessageOutController(AbstractQueueService<I> queueIn, QueueOutService<O> queueOutService) {
		this.queueIn = queueIn;
		this.queueOutService = queueOutService;
	}
	
	public void start() {
		isActive = true;
		new Thread() {
			@Override
			public void run() {
				while (isActive) {
					try {
						I message = queueIn.poll();
						if (message == null) {
							Thread.sleep(100);
							continue;
						}
						List<O> messageOutList = process(message);
						response(messageOutList);
					} catch (InterruptedException e ) {
						log.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				}
			}
		}.start();
	}
	
	protected abstract List<O> process(I messageIn);
	
	protected void response(List<O> messageOutList) {
		for (O messageOut : messageOutList) {
			queueOutService.add(messageOut);
		}
	}
	
	public void stop() {
		isActive = false;
	}
}
