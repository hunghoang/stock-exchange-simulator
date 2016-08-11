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
		return checkSession(TradSesStatus.LO);
	}

	@Override
	public boolean isATC1() {
		return checkSession(TradSesStatus.ATC1);
	}

	@Override
	public boolean isATC2() {
		return checkSession(TradSesStatus.ATC2);
	}
	
	@Override
	public boolean isPT() {
		return checkSession(TradSesStatus.PTCLOSE);
	}

	@Override
	public boolean isPreopen() {
		return checkSession(TradSesStatus.PREOPEN);
	}

	@Override
	public boolean isClose() {
		return checkSession(TradSesStatus.ENDOFDAY);
	}

	@Override
	public boolean isIntermission() {
		return checkSession(TradSesStatus.INTERMISSION);
	}
	
	private boolean checkSession(TradSesStatus status) {
		TradingSessionStatus currTradingSessionStatus = tradingSessionStatusManager.getCurrentSession();
		return status == currTradingSessionStatus.getTradingSessionCode();
	}

}
