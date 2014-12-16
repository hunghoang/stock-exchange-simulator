package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.TradingSessionStatusRequest;

@SuppressWarnings("serial")
@Component("TradingSessionStatusRequestQueue")
public class TradingSessionStatusRequestQueue extends AbstractQueueService<TradingSessionStatusRequest>
		implements Serializable, QueueService<TradingSessionStatusRequest> {

}
