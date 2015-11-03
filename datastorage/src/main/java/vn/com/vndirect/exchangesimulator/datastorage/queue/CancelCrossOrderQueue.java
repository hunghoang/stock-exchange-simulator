package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component("CancelCrossOrderQueue")
public class CancelCrossOrderQueue extends AbstractQueueService<Object>
		implements Serializable, QueueService<Object> {

}
