package vn.com.vndirect.exchangesimulator.datastorage.queue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.CrossOrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.Logon;
import vn.com.vndirect.exchangesimulator.model.NewOrderCross;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.model.SecurityStatusRequest;
import vn.com.vndirect.exchangesimulator.model.TestRequest;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatusRequest;

@Component("queueIn")
public class QueueInServiceImpl extends AbstractQueueService<Object> implements
		QueueInService<Object>, QueueListener {

	private static final Logger log = Logger
			.getLogger(QueueInServiceImpl.class);

	@Autowired
	private OrderQueue orderQueue;

	@Autowired
	private LogonQueue logonQueue;

	@Autowired
	private SecurityStatusRequestQueue securityStatusRequestQueue;

	@Autowired
	private TradingSessionStatusRequestQueue tradingSessionStatusRequestQueue;
	
	@Autowired
	private TestRequestQueue testRequestQueue;
	

	@Autowired
	private CrossOrderQueue crossOrderQueue;

	@Autowired
	private OthersQueue othersQueue;

	private boolean enableRoute;

	public QueueInServiceImpl() {
		super();
		enableRoute = true;
		addListener(this);
	}

	public QueueInServiceImpl(boolean enableRoute) {
		this();
		this.enableRoute = enableRoute;
	}

	@Override
	public void route(Object objToRoute) {
		if (NewOrderSingle.class.isInstance(objToRoute) || OrderCancelRequest.class.isInstance(objToRoute) || OrderReplaceRequest.class.isInstance(objToRoute)) {
			orderQueue.add(objToRoute);
		} else if (NewOrderCross.class.isInstance(objToRoute) || CrossOrderCancelRequest.class.isInstance(objToRoute)) {
			crossOrderQueue.add(objToRoute);
		} else if (Logon.class.isInstance(objToRoute)) {
			logonQueue.add((Logon) objToRoute);
		} else if (SecurityStatusRequest.class.isInstance(objToRoute)) {
			securityStatusRequestQueue.add((SecurityStatusRequest) objToRoute);
		} else if (TradingSessionStatusRequest.class.isInstance(objToRoute)) {
			tradingSessionStatusRequestQueue
					.add((TradingSessionStatusRequest) objToRoute);
		} else if (TestRequest.class.isInstance(objToRoute)) {
			testRequestQueue.add((TestRequest) objToRoute);
		} else {
			log.info("Unknow Type Object: " + objToRoute);
			othersQueue.add(objToRoute);
		}
	}

	@Override
	public void onEvent(Object source) {
		route(source);
	}

	public boolean isEnableRoute() {
		return enableRoute;
	}

	public void setEnableRoute(boolean enableRoute) {
		this.enableRoute = enableRoute;
	}

	protected void setOthersQueue(OthersQueue othersQueue) {
		this.othersQueue = othersQueue;
	}

	protected void setSecurityStatusRequestQueue(
			SecurityStatusRequestQueue securityStatusRequestQueue) {
		this.securityStatusRequestQueue = securityStatusRequestQueue;
	}

	protected void setTradingSessionStatusRequestQueue(
			TradingSessionStatusRequestQueue tradingSessionStatusRequestQueue) {
		this.tradingSessionStatusRequestQueue = tradingSessionStatusRequestQueue;
	}

	public OrderQueue getOrderQueue() {
		return orderQueue;
	}

	public void setOrderQueue(OrderQueue orderQueue) {
		this.orderQueue = orderQueue;
	}

	public LogonQueue getLogonQueue() {
		return logonQueue;
	}

	public void setLogonQueue(LogonQueue logonQueue) {
		this.logonQueue = logonQueue;
	}

}
