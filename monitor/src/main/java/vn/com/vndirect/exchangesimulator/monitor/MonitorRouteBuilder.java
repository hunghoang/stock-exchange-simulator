
package vn.com.vndirect.exchangesimulator.monitor;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.TcpSender;
import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorageService;
import vn.com.vndirect.exchangesimulator.fixconvertor.FixConvertor;

@Component
public class MonitorRouteBuilder extends RouteBuilder {
	private static final String MONITOR_LINK = "http://0.0.0.0:9005/exchange-simulator/monitor";
	private static final String MANUAL_MATCHING_LINK = "http://0.0.0.0:9005/exchange-simulator/manualMatching";
	private static final String ORDER_MANAGER_LINK = "http://0.0.0.0:9005/exchange-simulator/orderManager";
	private static final String ALL_ORDER_LINK = "http://0.0.0.0:9005/exchange-simulator/allOrder";
	private static final Logger LOGGER = Logger.getLogger(MonitorRouteBuilder.class);
	
	@Autowired
	public MonitorService monitorService;
	
	@Autowired
	public OrderManagerService orderManagerService;
	
	@Autowired
	public TcpSender tcpSender;
	
	@Autowired
	private SessionManagerService sessionManager;
	
	@Autowired
	private FixConvertor fixConvertor;
	
	@Autowired
	private OrderStorageService orderStorageService;
	
	
	@Autowired
	public MonitorRouteBuilder(MonitorService monitorService, SessionManagerService sessionManager, TcpSender tcpSender) {
		this.monitorService = monitorService;
		this.sessionManager = sessionManager;
		this.tcpSender = tcpSender;
	}
	
	@PostConstruct
	public void init() throws Exception {
		Main main = new Main();
		main.enableHangupSupport();
		main.addRouteBuilder(this);
		main.run();
	}
	
	@Override
	public void configure() throws Exception {
		from("jetty:" + ALL_ORDER_LINK).process(new Processor() {
			@Override	
			public void process(Exchange exc) throws Exception {
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setBody(orderStorageService.getAllOrder().toString());
			}
			
		});
		
		from("jetty:" + MONITOR_LINK).process(new Processor() {
			@Override	
			public void process(Exchange exc) throws Exception {
				Object session = exc.getIn().getHeader("session");
				Object fixmsg = exc.getIn().getHeader("fixmsg");
				if (session != null && !session.toString().isEmpty()) {
					sessionManager.setSession(session.toString());
				} 
				
				if (fixmsg != null && !fixmsg.toString().isEmpty()) {
					sendFixMsgToClient(fixmsg);
				}
				
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setBody(monitorService.buildMonitorContent());
			}

		});
		
		from("jetty:" + ORDER_MANAGER_LINK).process(new Processor() {
			@Override	
			public void process(Exchange exc) throws Exception {
				String orderId = (String)exc.getIn().getHeader("orderId");
				LOGGER.info("UnUsed");
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setBody(orderManagerService.buildContent());
			}

		});
		
		from("jetty:" + MANUAL_MATCHING_LINK).process(new Processor() {
			@Override	
			public void process(Exchange exc) throws Exception {
				String orderId = (String)exc.getIn().getHeader("orderId");
				String symbol = (String)exc.getIn().getHeader("symbol");
				String priceStr = (String)exc.getIn().getHeader("price");
				String quantityStr = (String)exc.getIn().getHeader("quantity");
				
				if(StringUtils.isNotEmpty(orderId) && StringUtils.isNotEmpty(symbol)
						&& StringUtils.isNotEmpty(priceStr) && StringUtils.isNotEmpty(quantityStr)) {
					double price = Double.valueOf(priceStr);
					int quantity = Integer.valueOf(quantityStr);
					LOGGER.info("Order Matching Info: " + orderId + " " + symbol + " " + price + " " + quantity);
				}
				
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setBody(monitorService.buildManualMatchingContent());
			}

		});
	}

	private void sendFixMsgToClient(Object msg) throws IOException {
		String userId = "0211GW";
		String standartFixMessage = ((String) msg).replaceAll("\\^A", "");
		tcpSender.manualSend(userId, standartFixMessage);
	}
}
