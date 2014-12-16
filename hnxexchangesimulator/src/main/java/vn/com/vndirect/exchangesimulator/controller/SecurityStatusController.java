package vn.com.vndirect.exchangesimulator.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.SecurityStatusRequestQueue;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.SecurityStatusManager;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;
import vn.com.vndirect.exchangesimulator.model.SecurityStatusRequest;

@Component
public class SecurityStatusController extends GatewayMultiMessageOutController<SecurityStatusRequest, SecurityStatus> {
	
	private static final Logger log = Logger.getLogger(SecurityStatusController.class);
	
	private InMemory memory;
	private SecurityStatusManager securityStatusManager;

	@Autowired
	public SecurityStatusController(SecurityStatusRequestQueue queueIn, QueueOutService<SecurityStatus> queueOutService, SecurityStatusManager securityStatusManager, InMemory memory) {
		super(queueIn, queueOutService);
		this.securityStatusManager = securityStatusManager;
		this.memory = memory;
	}

	@PostConstruct
	public void start() {
		isActive = true;
		new Thread() {
			@Override
			public void run() {
				while (isActive) {
					try {
						SecurityStatusRequest message = queueIn.poll();
						if (message == null) {
							Thread.sleep(100);
							continue;
						}
						List<SecurityStatus> messageOutList = process(message);
						response(messageOutList);
					} catch (InterruptedException e ) {
						log.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				}
			}
		}.start();
	}

	protected List<SecurityStatus> process(SecurityStatusRequest message) {
		List<SecurityStatus> securityStatusList = new ArrayList<SecurityStatus>();
		Integer lastProccessedSeq = (Integer) memory.get("last_processed_sequence", message.getSenderCompID());
		memory.put("last_processed_sequence", message.getSenderCompID(), ++lastProccessedSeq);
		List<SecurityStatus> securityStatuss = securityStatusManager.getSecurityStatus();
		for (SecurityStatus securityStatus : securityStatuss) {
			securityStatus.setTargetCompID(message.getSenderCompID());
			securityStatusList.add(securityStatus);
		}
		return securityStatusList;
	}
	
	protected void response(List<SecurityStatus> messageOut) {
		for (SecurityStatus securityStatus : messageOut) {
			queueOutService.add(securityStatus);
		}
	}
}
