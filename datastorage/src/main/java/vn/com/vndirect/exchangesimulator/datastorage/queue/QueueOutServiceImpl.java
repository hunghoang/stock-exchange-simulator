package vn.com.vndirect.exchangesimulator.datastorage.queue;

import org.springframework.stereotype.Component;

@Component("queueOut")
public class QueueOutServiceImpl extends AbstractQueueService<Object> implements
		QueueOutService<Object> {
}
