package vn.com.vndirect.exchangesimulator.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorageService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.CrossOrderQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueListener;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.model.CrossOrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.GroupSide;
import vn.com.vndirect.exchangesimulator.model.NewOrderCross;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;

@Component
public class CrossOrderController implements QueueListener {
	private static final Logger LOGGER = Logger
			.getLogger(CrossOrderController.class);

	private CrossOrderQueue queueIn;

	private OrderStorageService orderStorageService;

	private QueueOutService<Object> queueOut;

	private Map<String, Method> methodMap = new HashMap<String, Method>();

	@Autowired
	public CrossOrderController(CrossOrderQueue queueIn,
			OrderStorageService orderStorageService,
			QueueOutService<Object> queueOut) {
		this.queueIn = queueIn;
		this.orderStorageService = orderStorageService;
		this.queueIn.addListener(this);
		this.queueOut = queueOut;
		mappingMethodWithName();
	}

	private void mappingMethodWithName() {
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			methodMap.put(method.getName(), method);
		}
	}

	@Override
	public void onEvent(Object order) {
		LOGGER.info(order);
		if (NewOrderCross.class.isInstance(order)) {
			if (isNewOrder((NewOrderCross) order)) {
				generateCrossID((NewOrderCross) order);
				updateNewStatus((NewOrderCross) order);
				orderStorageService.addCrossOrder((NewOrderCross) order);
				sendAckIfFakedSellerOrder((NewOrderCross) order);
			} else if (confirmOrder((NewOrderCross) order)) {
				updateOrderCross((NewOrderCross) order);
				acceptTwoFirm(((NewOrderCross) order).getCrossID());
			} else if (rejectOrder((NewOrderCross) order)) {
				updateOrderCross((NewOrderCross) order);
				rejectTwoFirm(((NewOrderCross) order).getCrossID());
			}
		} else if (CrossOrderCancelRequest.class.isInstance(order)) {
			orderStorageService
					.addCancelCrossOrder((CrossOrderCancelRequest) order);
			if (cancelRequest((CrossOrderCancelRequest) order)) {
				sendCancelAck((CrossOrderCancelRequest) order);
			} else if (cancelIsAcceptFromPartner((CrossOrderCancelRequest) order)) {
				updateCancelPartnerAccept((CrossOrderCancelRequest) order);
			} else if (cancelIsRejectFromPartner((CrossOrderCancelRequest) order)) {
				updateCancelPartnerReject((CrossOrderCancelRequest) order);
			}
		} else if (Map.class.isInstance(order)) {
			Map<String, Object> params = (Map<String, Object>) order;
			String action = (String) params.get("action");
			Object data = params.get("data");
			processAction(action, data);
		}
	}

	private void generateCrossID(NewOrderCross order) {
		String newCrossID = order.getSymbol() + "0000" + order.getCrossID();
		order.setCrossID(newCrossID);
	}

	private void updateOrderCross(NewOrderCross order) {
		NewOrderCross oldOrder = orderStorageService.getOrderCross(order.getCrossID());
		if (oldOrder == null) {
			LOGGER.error("No NewOrderCross found for: " + order.getCrossID());
			return;
		}
		orderStorageService.removeCrossOrder(oldOrder);	
		orderStorageService.addCrossOrder(order);	
	}

	private boolean isNewOrder(NewOrderCross order) {
		return order.getCrossType().equals("1");
	}

	private boolean confirmOrder(NewOrderCross order) {
		return order.getCrossType().equals("5");
	}

	private boolean rejectOrder(NewOrderCross order) {
		return order.getCrossType().equals("6");
	}


	private boolean cancelRequest(CrossOrderCancelRequest cancelOrder) {
		return "1".equals(cancelOrder.getCrossType());
	}
	
	private boolean cancelIsAcceptFromPartner(CrossOrderCancelRequest cancel) {
		return "5".equals(cancel.getCrossType());
	}

	private boolean cancelIsRejectFromPartner(CrossOrderCancelRequest cancel) {
		return "6".equals(cancel.getCrossType());
	}
	
	private void sendAckIfFakedSellerOrder(NewOrderCross order) {
		if (isFakedSeller(order)) {
			ackTwoFirmOrder(order.getCrossID());
		}
	}

	private boolean isFakedSeller(NewOrderCross order) {
		return order.getSenderCompID() == null;
	}

	private void updateNewStatus(NewOrderCross order) {
		order.setCurrentStatus("NEW");
	}

	private void updatePendingCancelNeedApproval(NewOrderCross order) {
		order.setCurrentStatus("CANCEL WAIT FOR APPROVAL");
	}
	private void updateRejectCancelNeedApproval(NewOrderCross order) {
		order.setCurrentStatus("REJECT CANCEL WAIT FOR APPROVAL");
	}

	private void updateFillStatus(NewOrderCross order) {
		order.setCurrentStatus("FILL");
	}

	public void processAction(String action, Object data) {
		LOGGER.info("processAction with action=" + action + " and data ="
				+ data);
		System.out.println("preprocessAction with action=" + action
				+ " and data =" + data);
		Method m = methodMap.get(action);
		if (m == null) {
			LOGGER.error("No method found for " + action);
			return;
		}
		try {
			m.invoke(this, data);
			System.out.println("postprocessAction with action=" + action
					+ " and data =" + data);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
	}

	protected void sendCancelAck(CrossOrderCancelRequest cancelOrder) {
		if (cancelRequest(cancelOrder)) {
			String crossID = cancelOrder.getCrossID();
			sendCancelAckWithCrossID(crossID);
		}
	}

	private void updateCancelPartnerAccept(CrossOrderCancelRequest cancel) {
		NewOrderCross cross = orderStorageService.getOrderCross(cancel
				.getCrossID());
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + cancel.getCrossID());
			return;
		}
		updatePendingCancelNeedApproval(cross);
	}
	

	private void updateCancelPartnerReject(CrossOrderCancelRequest cancel) {
		NewOrderCross cross = orderStorageService.getOrderCross(cancel.getCrossID());
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + cancel.getCrossID());
			return;
		}
		updateRejectCancelNeedApproval(cross);
		
	}

	protected void sendCancelAckWithCrossID(String crossID) {
		NewOrderCross cross = orderStorageService.getOrderCross(crossID);
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + crossID);
			return;
		}
		cross.setCurrentStatus("PENDING CANCEL");
		CrossOrderCancelRequest cancelOrderResponse = new CrossOrderCancelRequest();
		cancelOrderResponse.setCrossID(crossID);
		cancelOrderResponse.setTargetCompID(cross.getSenderCompID());
		cancelOrderResponse.setSymbol(cross.getSymbol());
		cancelOrderResponse.setOrigCrossID(cross.getCrossID());
		cancelOrderResponse.setCrossType("1");
		cancelOrderResponse.setOrderId(cross.getCrossID());
		queueOut.add(cancelOrderResponse);
	}

	public void acceptOneFirm(String crossID) {
		NewOrderCross cross = orderStorageService.getOrderCross(crossID);
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + crossID);
			return;
		}
		updateFillStatus(cross);
		ExecutionReport report = confirmCrossOrder(cross);
		queueOut.add(report);
	}

	private ExecutionReport confirmCrossOrder(NewOrderCross cross) {
		ExecutionReport report = new ExecutionReport();
		String targetCompId = getTargetCompIdToReply(cross);
		report.setTargetCompID(targetCompId);
		GroupSide group1 = cross.getGroupSides().get(0);
		report.setClOrdID(group1.getClOrdID());
		report.setAccount(group1.getAccount());
		report.setOrderID(cross.getCrossID());
		report.setSymbol(cross.getSymbol());
		report.setExecType(ExecType.FILL);
		report.setOrdStatus(OrdStatus.FILL);
		report.setOrdType('P');
		report.setSide('8');
		report.setLastPx(cross.getPrice());
		report.setLastQty(group1.getOrderQty());
		report.setOrigClOrdID(cross.getCrossID());
		report.setSecondaryClOrdID(cross.getCrossID());
		report.setExecID(report.getOrderID());
		return report;
	}

	private String getTargetCompIdToReply(NewOrderCross cross) {
		if (cross.getSenderCompID() != null) {
			return cross.getSenderCompID();
		}
		return cross.getGroupSides().get(1).getPartyID() + "1GW";
	}

	public void rejectFromExchangeWithCode(String crossIDAndrejectCode) {
		rejectFromExchange(crossIDAndrejectCode.split(",")[0],
				crossIDAndrejectCode.split(",")[1]);
	}

	public void rejectFromExchange(String crossID, String rejectCode) {
		NewOrderCross cross = orderStorageService.getOrderCross(crossID);
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + crossID);
			return;
		}
		cross.setCurrentStatus("REJECT");
		ExecutionReport report = new ExecutionReport();
		String targetCompId = getTargetCompIdToReply(cross);
		report.setTargetCompID(targetCompId);
		report.setMsgType("3");
		report.setSide('8');
		report.setRefMsgType(cross.getMsgType());
		report.setRefSeqNum(cross.getMsgSeqNum());
		report.setSessionRejectReason(rejectCode);
		report.setSymbol(cross.getSymbol());
		queueOut.add(report);
	}

	public void acceptCancelOneFirm(String orderId) {
		NewOrderCross cross = orderStorageService.getOrderCross(orderId);
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + orderId);
			return;
		}
		cross.setCurrentStatus("CANCEL");
		ExecutionReport report = new ExecutionReport();
		String targetCompId = getTargetCompIdToReply(cross);
		report.setTargetCompID(targetCompId);
		GroupSide group1 = cross.getGroupSides().get(0);
		report.setAccount(group1.getAccount());
		report.setClOrdID(group1.getClOrdID());
		report.setOrderID(cross.getCrossID());
		report.setSymbol(cross.getSymbol());
		report.setOrdType('P');
		report.setExecType(ExecType.CANCEL);
		report.setOrdStatus(OrdStatus.CANCELORREPLACE);
		report.setSide('8');
		report.setPrice(cross.getPrice());
		report.setLeavesQty(group1.getOrderQty());
		report.setOrigClOrdID(cross.getCrossID());
		report.setSecondaryClOrdID(cross.getCrossID());
		report.setExecID(report.getOrderID());
		queueOut.add(report);
	}

	public void rejectCancelOneFirm(String crossID) {
		acceptOneFirm(crossID);
	}

	public void ackTwoFirmOrder(String orderId) {
		NewOrderCross order = orderStorageService.getOrderCross(orderId);
		if (order == null) {
			LOGGER.error("No NewOrderCross found for: " + orderId);
			return;
		}
		GroupSide group2 = order.getGroupSides().get(1);
		group2.setAccount("");
		group2.setClOrdID("");
		if (isTwoFirmOrder(order)) {
			updateNewStatus(order);
			NewOrderCross orderResponse = new NewOrderCross();
			orderResponse.setGroupSides(order.getGroupSides());
			String targetCompId = getTargetCompIdToReply(order);
			orderResponse.setTargetCompID(targetCompId);
			orderResponse.setCrossID(orderId);
			orderResponse.setSymbol(order.getSymbol());
			orderResponse.setCrossType(order.getCrossType());
			orderResponse
					.setCrossPrioritization(order.getCrossPrioritization());
			orderResponse.setCrossType(order.getCrossType());
			orderResponse.setSettlType(order.getSettlType());
			orderResponse.setPrice(order.getPrice());
			queueOut.add(orderResponse);
		}
	}

	private boolean isTwoFirmOrder(NewOrderCross order) {
		GroupSide group = order.getGroupSides().get(1);
		return group.getAccount().equals("");
	}

	public void acceptTwoFirm(String orderId) {
		NewOrderCross cross = orderStorageService.getOrderCross(orderId);
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + orderId);
			return;
		}
		updateFillStatus(cross);
		ExecutionReport report = confirmCrossOrder(cross);
		queueOut.add(report);
	}

	public void acceptCancelFromBuyerTwoFirm(String orderId) {
		NewOrderCross cross = orderStorageService.getOrderCross(orderId);
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + orderId);
			return;
		}
		updatePendingCancelNeedApproval(cross);
		ExecutionReport report = new ExecutionReport();

		String targetCompId = getTargetCompIdToReply(cross);
		report.setTargetCompID(targetCompId);
		GroupSide group1 = cross.getGroupSides().get(0);
		report.setAccount(group1.getAccount());
		report.setClOrdID(group1.getClOrdID());
		report.setOrderID(cross.getCrossID());
		report.setSymbol(cross.getSymbol());
		report.setOrdType('P');
		report.setExecType(ExecType.CANCEL);
		report.setOrdStatus(OrdStatus.PENDING_NEW);
		report.setSide('8');
		report.setPrice(cross.getPrice());
		report.setLeavesQty(group1.getOrderQty());
		report.setOrigClOrdID(cross.getCrossID());
		report.setSecondaryClOrdID(cross.getCrossID());
		report.setExecID(report.getOrderID());
		queueOut.add(report);
	}

	public void acceptCancelFromExchangeTwoFirm(String orderId) {
		acceptCancelOneFirm(orderId);
	}

	public void rejectTwoFirm(String orderId) {
		NewOrderCross cross = orderStorageService.getOrderCross(orderId);
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + orderId);
			return;
		}
		cross.setCurrentStatus("CANCEL");
		ExecutionReport report = new ExecutionReport();

		String targetCompId = getTargetCompIdToReply(cross);
		report.setTargetCompID(targetCompId);
		report.setAccount(cross.getGroupSides().get(0).getAccount());
		report.setOrderID(cross.getCrossID());
		report.setClOrdID(cross.getCrossID());
		report.setOrigClOrdID(cross.getCrossID());
		report.setExecType(ExecType.CANCEL);
		report.setOrdStatus(OrdStatus.CANCELORREPLACE);
		report.setOrdType('P');
		report.setSymbol(cross.getSymbol());
		report.setSide('8');
		report.setPrice(cross.getPrice());
		report.setLeavesQty(cross.getGroupSides().get(0).getOrderQty());
		queueOut.add(report);
	}

	public void rejectCancelTwoFirmFromPartner(String orderId) {
		rejectCancelTwoFirmCrossOrder(orderId, null);
	}

	public void rejectCancelFromExchange(String orderIdAndrejectCode) {
		rejectCancelTwoFirmCrossOrder(orderIdAndrejectCode.split(",")[0],
				orderIdAndrejectCode.split(",")[1]);
	}

	public void rejectCancelTwoFirmCrossOrder(String orderId, String rejectCode) {
		NewOrderCross cross = orderStorageService.getOrderCross(orderId);
		if (cross == null) {
			LOGGER.error("No NewOrderCross found for: " + orderId);
			return;
		}
		updateFillStatus(cross);
		ExecutionReport report = confirmCrossOrder(cross);
		report.setOrdRejReason(rejectCode);
		queueOut.add(report);
	}
}
