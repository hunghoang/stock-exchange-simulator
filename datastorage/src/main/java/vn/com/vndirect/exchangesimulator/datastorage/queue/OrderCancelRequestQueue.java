package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;

@SuppressWarnings("serial")
@Component("OrderCancelRequestQueue")
public class OrderCancelRequestQueue extends AbstractQueueService<OrderCancelRequest>
		implements Serializable, QueueService<OrderCancelRequest> {

}
