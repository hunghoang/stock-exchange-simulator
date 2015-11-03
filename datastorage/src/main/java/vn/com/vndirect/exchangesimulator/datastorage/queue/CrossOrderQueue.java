package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component("CrossOrderQueue")
public class CrossOrderQueue extends AbstractQueueService<Object>
		implements Serializable, QueueService<Object> {

}
