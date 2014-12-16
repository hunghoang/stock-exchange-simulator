package vn.com.vndirect.exchangesimulator.fixconvertor;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;

/**
 * This Test is used to check report in diff case has enough tags
 */
public class BOFixMsgTest {
	
	private ExecutionReport genReplaceBothPriceAndVolReport(){
		// simlar to replace price but:
		// (check below)
		ExecutionReport executionReport = new ExecutionReport();
		executionReport.setSenderCompID("HNX");
		executionReport.setTargetCompID("0211GW");
		executionReport.setExecType('5');
		executionReport.setOrdStatus('3');
		executionReport.setOrderID("replacevol1237");
		// new quantity after replace
		executionReport.setLastQty(1600);
		// = New Quantity - Origin Quantity
		executionReport.setLeavesQty(1400);
		// price after replaced
		executionReport.setLastPx(16.0); // 
		executionReport.setClOrdID("VND0211GW82");
		executionReport.setOrigClOrdID("VND0211GW82");
		executionReport.setSymbol("VND");
		executionReport.setSide('1');
		executionReport.setOrdType('2');
		executionReport.setAccount("021C000228");
		// extra may be not important
		executionReport.setMsgSeqNum(123);
		executionReport.setLastMsgSeqNumProcessed(123);
		executionReport.setSendingTime("20141031-02:19:04");
		executionReport.setTransactTime(new Date());
		return executionReport;
	}
	
	private ExecutionReport genReplacePriceReport(){
		// replace report
		ExecutionReport executionReport = new ExecutionReport();
		executionReport.setSenderCompID("HNX");
		executionReport.setTargetCompID("0211GW");
		executionReport.setExecType('5');
		executionReport.setOrdStatus('3');
		executionReport.setOrderID("1231231231232");
		executionReport.setLastQty(800);
		executionReport.setLeavesQty(0);
		executionReport.setLastPx(16.0); // 
		executionReport.setClOrdID("VND0211GW78");
		executionReport.setOrigClOrdID("VND0211GW78");
		executionReport.setSymbol("VND");
		executionReport.setSide('1');
		executionReport.setOrdType('2');
		executionReport.setAccount("021C000228");
		// extra may be not important
		executionReport.setMsgSeqNum(123);
		executionReport.setLastMsgSeqNumProcessed(123);
		executionReport.setSendingTime("20141031-02:19:04");
		executionReport.setTransactTime(new Date());
		return executionReport;
	}
	
	private ExecutionReport genMatchedReportSell(){
		ExecutionReport report = new ExecutionReport();
		// this field is always id of seller' order
		report.setOrigClOrdID("CAP0211GW86");
		report.setSymbol("CAP");
		report.setLastPx(28.1);
		String matchedOrderID = UUID.randomUUID().toString().substring(0, 7); // id cua lenh khop tu sinh ra moi 
		report.setOrderID(matchedOrderID);
		report.setLastQty(900);
		report.setSenderCompID("HNX");
		report.setTargetCompID("GW");
		report.setExecID(matchedOrderID);
		report.setOrdStatus('2');
		report.setExecType('3');
		report.setSide('2');		
		// this field is always id of buyers' order
		report.setSecondaryClOrdID(UUID.randomUUID().toString().substring(0, 7));
		// may not be important
		report.setMsgSeqNum(123);
		report.setLastMsgSeqNumProcessed(123);
		report.setSendingTime("20141031-02:19:04");
		report.setTransactTime(new Date());
		return report;	
	}
	
	private ExecutionReport genMatchedReportBuy(){
		ExecutionReport report = new ExecutionReport();
		// this field is always id of seller' order
		report.setOrigClOrdID("VND0211GW82");
		report.setSymbol("VND");
		report.setLastPx(16.0);
		String matchedOrderID = UUID.randomUUID().toString().substring(0, 7); // id cua lenh khop tu sinh ra moi 
		report.setOrderID(matchedOrderID);
		report.setLastQty(1000);
		report.setSenderCompID("HNX");
		report.setTargetCompID("GW");
		report.setExecID(matchedOrderID);
		report.setOrdStatus('2');
		report.setExecType('3');
		report.setSide('1');		
		// this field is always id of buyers' order
		report.setSecondaryClOrdID("replacevol1237");
		// may not be important
		report.setMsgSeqNum(123);
		report.setLastMsgSeqNumProcessed(123);
		report.setSendingTime("20141031-02:19:04");
		report.setTransactTime(new Date());
		return report;
	}
	
	private Set<String> sampleTags(String msg){		
		String[] tags = msg.split("");
		Set<String> set = new HashSet<>();
		for (String tag : tags){
			set.add(tag.split("=")[0]);
		}
		return set;
	}
	
	private String genFix(ExecutionReport report) throws IOException{		
		FixConvertor converter = new FixConvertor();	
		String fixMsg = converter.convertObjectToFix(report);		
		return fixMsg;
	}
	
	/**
	 * Convert to ^ to paste on browser
	 * @param msg
	 * @return
	 */
	private String toHTMLFriendly(String msg){
		return msg.replace("", "^A");
	}
	
	private void assertTagsComplete(String sampleMsg, String actualMsg){
		Set<String> sampleTags = sampleTags(sampleMsg);
		Set<String> actual = sampleTags(actualMsg);
		for(String tag : sampleTags){
			Assert.assertTrue("Must contain: " + tag, actual.contains(tag));
		}
	}
	
	@Test
	public void tagsReplaceExecutionReport() throws IOException{
		String msg = "8=FIX.4.49=18935=849=HNX56=021.01GW34=2137369=90252=20141031-02:19:04150=539=311=SHA0000000841=SHA0000000837=SHA0000000960=20141031-02:19:041=021C04403855=SHA54=140=232=50031=5600151=010=090";
		assertTagsComplete(msg, genFix(genReplacePriceReport()));		
	}
	
	@Test
	public void tagsMatchedExecutionReport() throws IOException{
		String msg = "8=FIX.4.49=18135=849=HNX56=021.01GW34=1143369=8152=20141031-02:00:13150=339=241=SGD00000001526=SGD0000000237=SGD0000000160=20141031-02:00:1332=80031=1050017=SGD0000000155=SGD54=210=022";
		assertTagsComplete(msg, genFix(genMatchedReportBuy()));
	}
	
	@Test
	public void printReplaceReport() throws IOException{
//		System.out.println(toHTMLFriendly(genFix(genReplacePriceReport())));
//		System.out.println(toHTMLFriendly(genFix(genReplaceVolReport())));
//		System.out.println(toHTMLFriendly(genFix(genMatchedReport())));
		System.out.println(toHTMLFriendly(genFix(genMatchedReportSell())));
	}

}
