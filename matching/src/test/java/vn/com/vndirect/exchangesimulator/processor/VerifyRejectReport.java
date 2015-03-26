package vn.com.vndirect.exchangesimulator.processor;

import org.junit.Assert;

import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;

public class VerifyRejectReport {
	public static void verify(ExecutionReport report) {
		Assert.assertEquals("MsgType ", "3", report.getMsgType());
		Assert.assertEquals("RefSeqNum ", 1, report.getRefSeqNum());
		Assert.assertEquals("ExecType ", ExecType.REJECT, report.getExecType());
		Assert.assertEquals("OrdStatus ", OrdStatus.REJECT, report.getOrdStatus());
		Assert.assertEquals("OrdRejReason ", "5", report.getOrdRejReason());
		Assert.assertEquals("Underlying quantity ", 0, report.getUnderlyingLastQty(), 0);
	}
}
