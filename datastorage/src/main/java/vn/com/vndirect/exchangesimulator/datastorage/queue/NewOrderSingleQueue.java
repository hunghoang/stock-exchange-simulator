package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

@SuppressWarnings("serial")
@Component("NewOrderSingleQueue")
public class NewOrderSingleQueue extends AbstractQueueService<NewOrderSingle>
		implements Serializable, QueueService<NewOrderSingle> {

}
