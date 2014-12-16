package vn.com.vndirect.exchangesimulator.processor;

import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;

public interface Processor {
	public List<ExecutionReport> process(HnxMessage message);
}
