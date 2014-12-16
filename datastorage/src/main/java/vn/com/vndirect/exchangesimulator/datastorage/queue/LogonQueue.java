package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.Logon;

@SuppressWarnings("serial")
@Component("LogonQueue")
public class LogonQueue extends AbstractQueueService<Logon>
		implements Serializable, QueueService<Logon> {

}
