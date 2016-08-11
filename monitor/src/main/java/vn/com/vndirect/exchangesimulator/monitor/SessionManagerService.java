package vn.com.vndirect.exchangesimulator.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManager;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;

@Component
public class SessionManagerService {

	public TradingSessionStatusManager tradingSessionStatusManager;

	@Autowired
	public SessionManagerService(
			TradingSessionStatusManager tradingSessionStatusManager) {
		this.tradingSessionStatusManager = tradingSessionStatusManager;
	}

	public void setSession(String session) {
		tradingSessionStatusManager.setSession(getSession(session));
	}

	public String getCurrentSession() {
		TradingSessionStatus tradingSessionStatus = tradingSessionStatusManager
				.getCurrentSession();
		if (tradingSessionStatus.getTradSesStatus() == 90)
			return "preopen";
		if ("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID())
				&& tradingSessionStatus.getTradSesStatus() == 1) {
			return "open1";
		}

		if ("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID())
				&& tradingSessionStatus.getTradSesStatus() == 2)
			return "intermission";

		if ("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID())
				&& tradingSessionStatus.getTradSesStatus() == 1)
			return "open2";

		if ("LIS_AUC_C_NML".equals(tradingSessionStatus.getTradingSessionID())
				&& tradingSessionStatus.getTradSesStatus() == 1)
			return "close";
		if ("LIS_AUC_C_NML_LOC".equals(tradingSessionStatus
				.getTradingSessionID())
				&& tradingSessionStatus.getTradSesStatus() == 1)
			return "closebl";
		if ("LIS_PTH_P_NML".equals(tradingSessionStatus.getTradingSessionID())
				&& tradingSessionStatus.getTradSesStatus() == 1)
			return "pt";
		if (tradingSessionStatus.getTradSesStatus() == 97)
			return "endofday";
		return "NA";
	}

	protected TradingSessionStatus getSession(String session) {
		TradingSessionStatus tradingSessionStatus = new TradingSessionStatus();
		switch (session.toLowerCase()) {
		case "preopen":
			tradingSessionStatus = tradingSessionStatusManager
					.getPreOpenSession();
			break;
		case "open":
		case "open1":
			tradingSessionStatus = tradingSessionStatusManager
					.getOpen1Session();
			break;
		case "intermission":
			tradingSessionStatus = tradingSessionStatusManager
					.getIntermissionSession();
			break;
		case "open2":
			tradingSessionStatus = tradingSessionStatusManager
					.getOpen2Session();
			break;
		case "close":
			tradingSessionStatus = tradingSessionStatusManager
					.getCloseSession();
			break;
		case "closebl":
			tradingSessionStatus = tradingSessionStatusManager
					.getCloseBlSession();
			break;
		case "pt":
			tradingSessionStatus = tradingSessionStatusManager.getPtSession();
			break;
		case "endofday":
			tradingSessionStatus = tradingSessionStatusManager
					.getEndOfDaySession();
			break;
		default:
			break;
		}
		return tradingSessionStatus;
	}
}
