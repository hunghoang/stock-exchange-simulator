package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;

@SuppressWarnings("serial")
@Component("OrderReplaceRequestQueue")
public class OrderReplaceRequestQueue extends AbstractQueueService<OrderReplaceRequest>
		implements Serializable, QueueService<OrderReplaceRequest> {

}
