package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;
import java.util.LinkedList;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.SecurityStatusRequest;

@SuppressWarnings("serial")
@Component("SecurityStatusRequestQueue")
public class SecurityStatusRequestQueue extends AbstractQueueService<SecurityStatusRequest>
		implements Serializable, QueueService<SecurityStatusRequest> {

}
