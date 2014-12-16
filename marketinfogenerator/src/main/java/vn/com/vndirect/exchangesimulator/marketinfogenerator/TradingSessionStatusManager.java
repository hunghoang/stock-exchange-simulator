package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;


public interface TradingSessionStatusManager {
	TradingSessionStatus getPreOpenSession();
	TradingSessionStatus getOpen1Session();
	TradingSessionStatus getIntermissionSession();
	TradingSessionStatus getOpen2Session();
	TradingSessionStatus getCloseSession();
	TradingSessionStatus getCloseBlSession();
	TradingSessionStatus getPtSession();
	TradingSessionStatus getEndOfDaySession();
	void setSession(TradingSessionStatus sessionStatus);
	TradingSessionStatus getCurrentSession();
}
