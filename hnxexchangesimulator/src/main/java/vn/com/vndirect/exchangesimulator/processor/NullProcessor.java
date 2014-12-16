package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;

public class NullProcessor implements Processor {
	public List<ExecutionReport> process(HnxMessage message) {
		return new ArrayList<>();
	}
}
