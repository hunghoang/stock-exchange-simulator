package vn.com.vndirect.exchangesimulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.TradingSessionStatusRequestQueue;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManager;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatusRequest;

@Component
public class TradingSessionStatusRequestController extends GatewayMessageController<TradingSessionStatusRequest, TradingSessionStatus> {
	
	private TradingSessionStatusManager tradingSessionStatusManager;
	private InMemory memory;

	@Autowired
	public TradingSessionStatusRequestController(TradingSessionStatusRequestQueue tradingQueueIn, QueueOutService<TradingSessionStatus> queueOutService, TradingSessionStatusManager tradingSessionStatusManager, InMemory memory) {
		super(tradingQueueIn, queueOutService);
		this.memory = memory;
		this.tradingSessionStatusManager = tradingSessionStatusManager;
	}
	
	@Override
	protected TradingSessionStatus process(TradingSessionStatusRequest message) {
		Integer lastProccessedSeq = (Integer) memory.get("last_processed_sequence", message.getSenderCompID());
		if (lastProccessedSeq == null) {
			lastProccessedSeq = 0;
		}
		memory.put("last_processed_sequence", message.getSenderCompID(), ++lastProccessedSeq);
		TradingSessionStatus tradingSessionStatus = tradingSessionStatusManager.getCurrentSession();
		tradingSessionStatus.setTargetCompID(message.getSenderCompID());
		return tradingSessionStatus;
	}
}
