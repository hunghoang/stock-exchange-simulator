package vn.com.vndirect.exchangesimulator.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitorService {
	

	@Autowired
	private SessionManagerService sessionManager;
	
	public String buildMonitorContent() {
		StringBuilder strBuilder = new StringBuilder();
		String header = buildHeader();
		String body = builderData();
		strBuilder.append("<html>");
		strBuilder.append(header);
		strBuilder.append(body);
		strBuilder.append("</html>");
		return strBuilder.toString();
	}
	
	public String buildManualMatchingContent(){
		StringBuilder strBuilder = new StringBuilder();
		String header = buildHeader();
		String body = builderManualMatchingContent();
		strBuilder.append("<html>");
		strBuilder.append(header);
		strBuilder.append(body);
		strBuilder.append("</html>");
		return strBuilder.toString();
	}
	
	private String builderManualMatchingContent() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("<b>")
		.append("Manual matching: ")
		.append("</b>")
		.append("<br>")
		.append("<br><form action='manualMatching'>")
		.append("Order Id: <input name='orderId' type='text' value=''>")
		.append("<br>")
		.append("<br>")
		.append("Symbol: <input name='symbol' type='text' value=''>")
		.append("<br>")
		.append("<br>")
		.append("Price: <input name='price' type='text' value=''>")
		.append("<br>")
		.append("<br>")
		.append("Quantity:<input name='quantity' type='text' value=''>")
		.append("<br>")
		.append("<br>")
		.append("<input type='submit' value='Send'>")
		.append("</form>");
		return strBuilder.toString();
	}

	private String buildHeader() {
		StringBuilder headerBuilder = new StringBuilder();
		headerBuilder.append("<header>");
		headerBuilder.append("<title>Exchange Simulator Monitor</title>");
		headerBuilder.append("</header>");
		return headerBuilder.toString();
	}
	
	private String builderData() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("<b>");
		strBuilder.append("Current Session: " + sessionManager.getCurrentSession());
		strBuilder.append("</b>");
		strBuilder.append("<br>");
		strBuilder.append("<b>");
		strBuilder.append("Set Session: ");
		strBuilder.append("</b>");
		strBuilder.append("<input type='button' value='Pre Open' onclick=\"if(confirm('Are you sure you want to set Pre Open Session?')) window.location.href='monitor?session=preopen'\">");
		strBuilder.append("<input type='button' value='Open1' onclick=\"if(confirm('Are you sure you want to set Open1 Session?')) window.location.href='monitor?session=open1'\">");
		strBuilder.append("<input type='button' value='Intermission' onclick=\"if(confirm('Are you sure you want to set Intermission Session?')) window.location.href='monitor?session=intermission'\">");
		strBuilder.append("<input type='button' value='Open2' onclick=\"if(confirm('Are you sure you want to set Open2 Session?')) window.location.href='monitor?session=open2'\">");
		strBuilder.append("<input type='button' value='Close' onclick=\"if(confirm('Are you sure you want to set Close Session?')) window.location.href='monitor?session=close'\">");
		strBuilder.append("<input type='button' value='CloseBL' onclick=\"if(confirm('Are you sure you want to set Close BL Session?')) window.location.href='monitor?session=closebl'\">");
		strBuilder.append("<input type='button' value='PT' onclick=\"if(confirm('Are you sure you want to set PT Session?')) window.location.href='monitor?session=pt'\">");
		strBuilder.append("<input type='button' value='EndOfDay' onclick=\"if(confirm('Are you sure you want to set End of Day Session?')) window.location.href='monitor?session=endofday'\">");
		strBuilder.append("<br>");
		strBuilder.append("<br><form action='monitor'>Message: <textarea name='fixmsg'></textarea><br><input type='submit' value='Send To CTCK'></form>");
		return strBuilder.toString();
	}
	
}
