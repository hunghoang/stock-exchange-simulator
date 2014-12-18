package vn.com.vndirect.exchangesimulator.processor;

import org.junit.Assert;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;

public class VerifyRejectReport {
	public static void verify(ExecutionReport report) {
		Assert.assertEquals("ExecType ", '8', report.getExecType());
		Assert.assertEquals("OrdStatus ", '8', report.getOrdStatus());
		Assert.assertEquals("OrdRejReason ", "5", report.getOrdRejReason());
		Assert.assertEquals("Underlying quantity ", 0, report.getUnderlyingLastQty(), 0);
	}
}
