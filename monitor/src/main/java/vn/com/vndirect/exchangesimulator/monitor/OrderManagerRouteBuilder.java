package vn.com.vndirect.exchangesimulator.monitor;

import javax.annotation.PostConstruct;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderManagerRouteBuilder extends RouteBuilder {
	private static final String ORDER_MANAGER_LINK = "http://0.0.0.0:9005/exchange-simulator/orderManager";
	private static final Logger LOGGER = Logger.getLogger(OrderManagerRouteBuilder.class);
	
	@Autowired
	public OrderManagerService orderManagerService;
	
	@Autowired
	public OrderManagerRouteBuilder(OrderManagerService orderManagerService) {
		this.orderManagerService = orderManagerService;
	}
	
	//@PostConstruct
	public void init() throws Exception {
		Main main = new Main();
		main.enableHangupSupport();
		main.addRouteBuilder(this);
		main.run();
	}
	
	@Override
	public void configure() throws Exception {
		from("jetty:" + ORDER_MANAGER_LINK).process(new Processor() {
			@Override	
			public void process(Exchange exc) throws Exception {
				String orderId = (String)exc.getIn().getHeader("orderId");
				
				if(StringUtils.isNotEmpty(orderId)){
					LOGGER.info("Order Id: " + orderId);
				}
				
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setBody(orderManagerService.buildContent());
			}

		});
		
	}

}
