package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;

@SuppressWarnings("serial")
@Component("ExecutionReportQueue")
public class ExecutionReportQueue extends AbstractQueueService<ExecutionReport>
		implements Serializable, QueueService<ExecutionReport> {

}
