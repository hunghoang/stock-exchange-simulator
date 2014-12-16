package vn.com.vndirect.exchangesimulator.monitor;

import org.springframework.stereotype.Component;

@Component
public class OrderManagerService {
	
	public String buildContent() {
		StringBuilder strBuilder = new StringBuilder();
		String header = buildHeader();
		String body = builderData();
		strBuilder.append("<html>");
		strBuilder.append(header);
		strBuilder.append(body);
		strBuilder.append("</html>");
		return strBuilder.toString();
	}
	
	private String buildHeader() {
		StringBuilder headerBuilder = new StringBuilder();
		headerBuilder.append("<heade>");
		headerBuilder.append("<title>Exchange Simulator Order Namager</title>");
		headerBuilder.append("</heade>");
		return headerBuilder.toString();
	}
	
	private String builderData() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Order Manager: ")
		.append("</b>")
		.append("<br>")
		.append("<br><form action='orderManager'>")
		.append("Order Id: <input name='orderId' type='text' value=''>")
		.append("<br>")		
		.append("<br>")
		.append("<input type='submit' value='Send'>")
		.append("</form>");
		return strBuilder.toString();
	}

}
