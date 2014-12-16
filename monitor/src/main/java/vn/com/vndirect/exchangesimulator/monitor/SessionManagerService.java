package vn.com.vndirect.exchangesimulator.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManager;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;

@Component
public class SessionManagerService {
	
	public TradingSessionStatusManager tradingSessionStatusManager;
	
	@Autowired
	public SessionManagerService(TradingSessionStatusManager tradingSessionStatusManager) {
		this.tradingSessionStatusManager = tradingSessionStatusManager;
	}
	
	public void setSession(String session) {
		tradingSessionStatusManager.setSession(getSession(session));
	}
	
	protected TradingSessionStatus getSession(String session) {
		TradingSessionStatus tradingSessionStatus = new TradingSessionStatus();
		switch (session) {
		case "preopen":
			tradingSessionStatus = tradingSessionStatusManager.getPreOpenSession();
			break;
		case "open1" :
			tradingSessionStatus = tradingSessionStatusManager.getOpen1Session();
			break;
		case "intermission" :
			tradingSessionStatus = tradingSessionStatusManager.getIntermissionSession();
			break;
		case "open2" :
			tradingSessionStatus = tradingSessionStatusManager.getOpen2Session();
			break;
		case "close" :
			tradingSessionStatus = tradingSessionStatusManager.getCloseSession();
			break;
		case "closebl" :
			tradingSessionStatus = tradingSessionStatusManager.getCloseBlSession();
			break;
		case "pt" :
			tradingSessionStatus = tradingSessionStatusManager.getPtSession();
			break;
		case "endofday" :
			tradingSessionStatus = tradingSessionStatusManager.getEndOfDaySession();
			break;
		default:
			break;
		}
		return tradingSessionStatus;
	}
}
