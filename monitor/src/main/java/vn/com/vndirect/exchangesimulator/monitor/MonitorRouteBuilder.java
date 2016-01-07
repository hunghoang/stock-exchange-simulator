package vn.com.vndirect.exchangesimulator.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.TcpSender;
import vn.com.vndirect.exchangesimulator.constant.Side;
import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorageService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.CrossOrderQueue;
import vn.com.vndirect.exchangesimulator.fixconvertor.FixConvertor;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.SecurityStatusManagerImpl;
import vn.com.vndirect.exchangesimulator.model.GroupSide;
import vn.com.vndirect.exchangesimulator.model.NewOrderCross;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.lib.commonlib.file.FileUtils;

@Component
public class MonitorRouteBuilder extends RouteBuilder {
	private static final String MONITOR_LINK = "http://0.0.0.0:9005/exchange-simulator/monitor";
	private static final String UPDATE_PRICE_LINK = "http://0.0.0.0:9005/exchange-simulator/updatePrice";
	private static final String MANUAL_MATCHING_LINK = "http://0.0.0.0:9005/exchange-simulator/manualMatching";
	private static final String ORDER_MANAGER_LINK = "http://0.0.0.0:9005/exchange-simulator/orderManager";
	private static final String ALL_CROSS_ORDER_FAKED_BUYER_LINK = "http://0.0.0.0:9005/exchange-simulator/crossOrders-fakedbuyer";
	private static final String ALL_CROSS_ORDER_FAKED_SELLER_LINK = "http://0.0.0.0:9005/exchange-simulator/crossOrders-fakedseller";
	private static final String CREATE_CROSS_ORDER_ACTION_LINK = "http://0.0.0.0:9005/exchange-simulator/crossOrders-fakedseller-create";
	private static final String CROSS_ORDER_ACTION_LINK = "http://0.0.0.0:9005/exchange-simulator/crossOrderAction";
	private static final String CROSS_ORDER_SCREEN_LINK = "http://0.0.0.0:9005/exchange-simulator/crossOrderManual";
	private static final String ALL_ORDER_LINK = "http://0.0.0.0:9005/exchange-simulator/allOrder";

	private static final Logger LOGGER = Logger
			.getLogger(MonitorRouteBuilder.class);

	@Autowired
	public MonitorService monitorService;

	@Autowired
	public OrderManagerService orderManagerService;

	@Autowired
	public TcpSender tcpSender;

	@Autowired
	private SessionManagerService sessionManager;

	@Autowired
	private SecurityStatusManagerImpl securityStatusManager;

	@Autowired
	private FixConvertor fixConvertor;

	@Autowired
	private OrderStorageService orderStorageService;

	@Autowired
	private CrossOrderQueue crossOrderQueue;

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	public MonitorRouteBuilder(MonitorService monitorService,
			SessionManagerService sessionManager, TcpSender tcpSender) {
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
				List<NewOrderSingle> allOrder = orderStorageService
						.getAllOrder();
				StringBuilder sb1 = new StringBuilder();
				StringBuilder sb2 = new StringBuilder();
				for (NewOrderSingle newOrderSingle : allOrder) {
					if (newOrderSingle.getSide() == '1') {
						sb1.append("<br>");
						sb1.append(newOrderSingle);
					} else {
						sb2.append("<br>");
						sb2.append(newOrderSingle);
					}

				}
				String content = "<html>Buys: " + sb1.toString();
				content += "<br>";
				content += "Sells: " + sb2.toString() + "</html>";
				exc.getOut().setBody(content);
			}

		});

		from("jetty:" + ALL_CROSS_ORDER_FAKED_BUYER_LINK).process(
				new Processor() {
					@Override
					public void process(Exchange exc) throws Exception {
						exc.getOut()
								.setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
						exc.getOut().setHeader(Exchange.CONTENT_TYPE,
								"application/json");
						List<NewOrderCross> listNewOrderCross = new ArrayList<NewOrderCross>();
						listNewOrderCross.addAll(getCrossOrderFakedBuyer());
						List<CrossOrder> crossOrders = convertToCrossOrder(listNewOrderCross);
						exc.getOut().setBody(
								mapper.writeValueAsString(crossOrders));
					}
				});

		from("jetty:" + ALL_CROSS_ORDER_FAKED_SELLER_LINK).process(
				new Processor() {
					@Override
					public void process(Exchange exc) throws Exception {
						exc.getOut()
								.setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
						exc.getOut().setHeader(Exchange.CONTENT_TYPE,
								"application/json");
						List<NewOrderCross> listNewOrderCross = new ArrayList<NewOrderCross>();
						listNewOrderCross.addAll(getCrossOrderFakedSeller());
						List<CrossOrder> crossOrders = convertToCrossOrder(listNewOrderCross);
						for (CrossOrder crossOrder : crossOrders) {
							crossOrder.fakedSeller = true;
						}
						exc.getOut().setBody(
								mapper.writeValueAsString(crossOrders));
					}

				});

		from("jetty:" + CROSS_ORDER_ACTION_LINK).process(new Processor() {
			@Override
			public void process(Exchange exc) throws Exception {
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				Map<String, Object> map = new HashMap<String, Object>();
				String action = (String) exc.getIn().getHeader("action");
				Object data = exc.getIn().getHeader("data");
				map.put("action", action);
				map.put("data", data);
				crossOrderQueue.add(map);
			}
		});

		from("jetty:" + CREATE_CROSS_ORDER_ACTION_LINK).process(
				new Processor() {
					int id = 1000;
					
					String initId = String.format("%tl%tM", Calendar.getInstance(), Calendar.getInstance());
					
					@Override
					public void process(Exchange exc) throws Exception {
						exc.getOut()
								.setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
						NewOrderCross cross = createCrossOrderFromRequest(exc);
						crossOrderQueue.add(cross);
					}

					private NewOrderCross createCrossOrderFromRequest(
							Exchange exc) {
						String symbol = exc.getIn().getHeader("symbol",
								String.class);
						Double price = exc.getIn().getHeader("price",
								Double.class);
						int quantity = exc.getIn().getHeader("quantity",
								Integer.class);
						String account1 = exc.getIn().getHeader("account1",
								String.class);
						String account2 = exc.getIn().getHeader("account2",
								String.class);
						String firm1 = exc.getIn().getHeader("firm1",
								String.class);
						String firm2 = exc.getIn().getHeader("firm2",
								String.class);
						NewOrderCross cross = new NewOrderCross();
						cross.setCrossType("1");
						GroupSide groupSide1 = new GroupSide();
						groupSide1.setAccount(account1);
						groupSide1.setOrderQty(quantity);
						groupSide1.setAccountType("");
						groupSide1.setClOrdID(initId + (id++) + "");
						groupSide1.setNoPartyIDs(1);
						groupSide1.setPartyID(firm1);
						groupSide1.setSide(Side.SELL.side());

						GroupSide groupSide2 = new GroupSide();
						groupSide2.setAccount(account2);
						groupSide2.setOrderQty(quantity);
						groupSide2.setAccountType("");
						groupSide2.setClOrdID(initId + id + "");
						groupSide2.setNoPartyIDs(1);
						groupSide2.setPartyID(firm2);
						groupSide2.setSide(Side.BUY.side());

						cross.setSymbol(symbol);
						cross.setPrice(price);
						cross.setCrossID(id + "");
						cross.setNoSides(2);
						cross.addGroup(groupSide1);
						cross.addGroup(groupSide2);
						return cross;
					}

				});

		from("jetty:" + CROSS_ORDER_SCREEN_LINK).process(new Processor() {

			@Override
			public void process(Exchange exc) throws Exception {
				List<String> contents = FileUtils
						.readFileByLine("crossorder.html");
				StringBuilder sb = new StringBuilder();
				for (String line : contents) {
					sb.append(line);
				}
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setHeader(Exchange.CONTENT_TYPE,
						"text/html; charset=utf-8");
				exc.getOut().setBody(sb.toString());
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

		from("jetty:" + UPDATE_PRICE_LINK).process(new Processor() {
			@Override
			public void process(Exchange exc) throws Exception {
				String pricelink = exc.getIn().getHeader("pricelink",
						String.class);

				if (pricelink != null && !pricelink.toString().isEmpty()) {
					securityStatusManager.loadFromLink(pricelink);
				}
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setBody(monitorService.buildMonitorContent());
			}

		});

		from("jetty:" + ORDER_MANAGER_LINK).process(new Processor() {
			@Override
			public void process(Exchange exc) throws Exception {
				String orderId = (String) exc.getIn().getHeader("orderId");
				LOGGER.info("UnUsed");
				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setBody(orderManagerService.buildContent());
			}

		});

		from("jetty:" + MANUAL_MATCHING_LINK).process(new Processor() {
			@Override
			public void process(Exchange exc) throws Exception {
				String orderId = (String) exc.getIn().getHeader("orderId");
				String symbol = (String) exc.getIn().getHeader("symbol");
				String priceStr = (String) exc.getIn().getHeader("price");
				String quantityStr = (String) exc.getIn().getHeader("quantity");

				if (StringUtils.isNotEmpty(orderId)
						&& StringUtils.isNotEmpty(symbol)
						&& StringUtils.isNotEmpty(priceStr)
						&& StringUtils.isNotEmpty(quantityStr)) {
					double price = Double.valueOf(priceStr);
					int quantity = Integer.valueOf(quantityStr);
					LOGGER.info("Order Matching Info: " + orderId + " "
							+ symbol + " " + price + " " + quantity);
				}

				exc.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
				exc.getOut().setBody(
						monitorService.buildManualMatchingContent());
			}

		});
	}

	private void sendFixMsgToClient(Object msg) throws IOException {
		String userId = "0211GW";
		String standartFixMessage = ((String) msg).replaceAll("\\^A", "");
		tcpSender.manualSend(userId, standartFixMessage);
	}

	/**
	 * Cross Order with faked buyer that mean order send from known senderid to
	 * faked buyer So we get this kind of order base on sendercompid != null
	 * 
	 * @return
	 */
	private Collection<? extends NewOrderCross> getCrossOrderFakedBuyer() {
		List<NewOrderCross> crossOrders = new ArrayList<NewOrderCross>();
		for (NewOrderCross cross : orderStorageService.getAllCrossOrder()) {
			if (cross.getSenderCompID() != null) {
				crossOrders.add(cross);
			}
		}
		return crossOrders;
	}

	/**
	 * Cross Order with faked sender that means order is created from exchange,
	 * so senderid is null So we get this kind of order base on sendercompid ==
	 * null
	 * 
	 * @return
	 */
	private Collection<? extends NewOrderCross> getCrossOrderFakedSeller() {
		List<NewOrderCross> crossOrders = new ArrayList<NewOrderCross>();
		for (NewOrderCross cross : orderStorageService.getAllCrossOrder()) {
			if (cross.getSenderCompID() == null) {
				crossOrders.add(cross);
			}
		}
		return crossOrders;
	}

	private List<CrossOrder> convertToCrossOrder(
			List<NewOrderCross> allCrossOrder) {
		List<CrossOrder> crossOrders = new ArrayList<CrossOrder>();
		for (NewOrderCross cross : allCrossOrder) {
			CrossOrder crossOrder = new CrossOrder();
			crossOrder.msgType = cross.getMsgType();
			crossOrder.symbol = cross.getSymbol();
			crossOrder.price = cross.getPrice();
			crossOrder.orderId = cross.getCrossID();
			crossOrder.orderStatus = cross.getCurrentStatus();
			crossOrder.quantity = cross.getGroupSides().get(0).getOrderQty();
			crossOrder.account1 = cross.getGroupSides().get(0).getAccount();
			crossOrder.account2 = cross.getGroupSides().get(1).getAccount();
			crossOrder.firm1 = cross.getGroupSides().get(0).getPartyID();
			crossOrder.firm2 = cross.getGroupSides().get(1).getPartyID();
			crossOrders.add(0, crossOrder);
		}
		return crossOrders;
	}

	public static void main(String[] args) {
		System.out.println(String.format("%tl%tM", Calendar.getInstance(), Calendar.getInstance()));
	}
}
