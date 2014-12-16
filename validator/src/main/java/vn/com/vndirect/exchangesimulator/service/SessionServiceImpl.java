package vn.com.vndirect.exchangesimulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManagerImpl;
import vn.com.vndirect.exchangesimulator.model.TradSesStatus;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;

@Component("SessionService")
public class SessionServiceImpl implements SessionService {

	@Autowired
	private TradingSessionStatusManagerImpl tradingSessionStatusManager;
	
	@Override
	public boolean isLO() {
		TradingSessionStatus currTradingSessionStatus = tradingSessionStatusManager.getCurrentSession();
		return TradSesStatus.LO == currTradingSessionStatus.getTradingSessionCode() ? true : false;
	}

	@Override
	public boolean isATC1() {
		TradingSessionStatus currTradingSessionStatus = tradingSessionStatusManager.getCurrentSession();
		return TradSesStatus.ATC1 == currTradingSessionStatus.getTradingSessionCode() ? true : false;
	}

	@Override
	public boolean isATC2() {
		TradingSessionStatus currTradingSessionStatus = tradingSessionStatusManager.getCurrentSession();
		return TradSesStatus.ATC2 == currTradingSessionStatus.getTradingSessionCode() ? true : false;
	}

}
