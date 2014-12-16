package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.TestRequest;

@SuppressWarnings("serial")
@Component("TestRequestQueue")
public class TestRequestQueue extends AbstractQueueService<TestRequest>
		implements Serializable, QueueService<TestRequest> {

}
