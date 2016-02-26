package vn.com.vndirect.exchangesimulator.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.SocketClient;
import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.TradingSessionStatusRequestQueue;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManager;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.TradSesStatus;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;

@Component
public class TradingSessionStatusController {

	private static final Logger log = Logger
			.getLogger(TradingSessionStatusController.class);

	private QueueOutService<HnxMessage> queueOutService;
	private TradingSessionStatusManager tradingSessionStatusManager;
	private InMemory memory;
	private Matcher matcher;

	protected boolean isActive;

	@Autowired
	public TradingSessionStatusController(
			TradingSessionStatusRequestQueue tradingQueueIn,
			QueueOutService queueOutService,
			TradingSessionStatusManager tradingSessionStatusManager,
			InMemory memory, Matcher matcher) {
		this.queueOutService = queueOutService;
		this.memory = memory;
		this.tradingSessionStatusManager = tradingSessionStatusManager;
		this.matcher = matcher;
	}

	@PostConstruct
	public void start() {
		isActive = true;
		new Thread() {
			@Override
			public void run() {
				try {
					TradingSessionStatus tradingSessionStatus = tradingSessionStatusManager
							.getCurrentSession();
					while (isActive) {
						try {
							if (tradingSessionStatus != tradingSessionStatusManager
									.getCurrentSession()) {
								tradingSessionStatus = tradingSessionStatusManager
										.getCurrentSession();
								notifyCore(tradingSessionStatus);
								notifyAllClient(tradingSessionStatus);
							} else {
								Thread.sleep(1000);
							}
						} catch (InterruptedException e) {
							log.error(e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					log.error("Exception when change session", e);
				}
			}

		}.start();
	}

	private void notifyCore(TradingSessionStatus tradingSessionStatus) {
		log.info("Current session is: "
				+ tradingSessionStatus.getTradingSessionCode());
		if (TradSesStatus.PREOPEN.equals(tradingSessionStatus
				.getTradingSessionCode())) {
			log.info("Reset all data when session is PREOPEN");
			matcher.reset();
		} else if (TradSesStatus.ATC1.equals(tradingSessionStatus
				.getTradingSessionCode())) {
			matcher.beginATC();
		} else if (TradSesStatus.PTCLOSE.equals(tradingSessionStatus
				.getTradingSessionCode())) {
			List<ExecutionReport> matchedExecutionReports = matcher.endATC();
			for (ExecutionReport report : matchedExecutionReports) {
				queueOutService.add(report);
			}
			matcher.reset();
		}
	}

	private void notifyAllClient(TradingSessionStatus tradingSessionStatus) {
		log.info("Notify all client when change session: ");
		Object clients = memory.get("SocketClientList", "");
		if (clients != null) {
			for (SocketClient socketClient : (List<SocketClient>) clients) {
				if (socketClient == null)
					continue;
				tradingSessionStatus.setTargetCompID(socketClient.getUserId());
				queueOutService.add(tradingSessionStatus);
				if (tradingSessionStatus.getTradingSessionCode() == TradSesStatus.LO || tradingSessionStatus.getTradingSessionCode() == TradSesStatus.ENDOFDAY) {
					TradingSessionStatus tradingSessionStatusUpcom = new TradingSessionStatus();
					tradingSessionStatusUpcom.setText(tradingSessionStatus.getText());
					tradingSessionStatusUpcom.setPossDupFlag(tradingSessionStatus.isPossDupFlag());
					tradingSessionStatusUpcom.setTradSesReqID(tradingSessionStatus.getTradSesReqID().replace("LIS", "UPC"));
					tradingSessionStatusUpcom.setTradingSessionID(tradingSessionStatus.getTradingSessionID().replace("LIS", "UPC"));
					tradingSessionStatusUpcom.setTradSesMode(tradingSessionStatus.getTradSesMode());
					tradingSessionStatusUpcom.setTradSesStatus(tradingSessionStatus.getTradSesStatus());
					tradingSessionStatusUpcom.setTradSesStartTime(tradingSessionStatus.getTradSesStartTime());
					queueOutService.add(tradingSessionStatusUpcom);
				}
			}
		}
	}

	public void stop() {
		isActive = false;
	}

}
