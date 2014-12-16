package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.vndirect.exchangesimulator.model.TradSesStatus;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;

@Component("TradingSessionStatusManager")
public class TradingSessionStatusManagerImpl implements TradingSessionStatusManager{
	private static final String SOURCE = "tradingsessionstatus.csv";
	private ObjectConvertor convertor;
	List<TradingSessionStatus> listMesg;
	private TradingSessionStatus currentTradingSessionStatus;
	
	@Autowired
	public TradingSessionStatusManagerImpl(ObjectConvertor objConverter) {
		this.convertor = objConverter;
		loadDataFromCSV();
	}
	
	private void loadDataFromCSV(){
		listMesg = convertor.convertFromCSVFile(SOURCE, TradingSessionStatus.class);
		currentTradingSessionStatus = getPreOpenSession();
	}
	
	
	
	@Override
	public TradingSessionStatus getPreOpenSession() {
		for (TradingSessionStatus tradingSessionStatus : listMesg) {
			if (tradingSessionStatus.getTradSesStatus() == 90){
				tradingSessionStatus.setTradingSessionCode(TradSesStatus.PREOPEN);
				return tradingSessionStatus;
			}
		}
		return null;
	}
	
	@Override
	public TradingSessionStatus getOpen1Session() {
		for (TradingSessionStatus tradingSessionStatus : listMesg) {
			if ("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID()) && tradingSessionStatus.getTradSesStatus() == 1){
				tradingSessionStatus.setTradingSessionCode(TradSesStatus.LO);
				return tradingSessionStatus;
			}
		}
		return null;
	}
	
	@Override
	public TradingSessionStatus getIntermissionSession() {
		for (TradingSessionStatus tradingSessionStatus : listMesg) {
			if ("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID()) && tradingSessionStatus.getTradSesStatus() == 2){
				tradingSessionStatus.setTradingSessionCode(TradSesStatus.INTERMISSION);
				return tradingSessionStatus;
			}
		}
		return null;
	}
	
	@Override
	public TradingSessionStatus getOpen2Session() {
		for (TradingSessionStatus tradingSessionStatus : listMesg) {
			if ("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID()) && tradingSessionStatus.getTradSesStatus() == 1){
				tradingSessionStatus.setTradingSessionCode(TradSesStatus.LO);
				return tradingSessionStatus;
			}
		}
		return null;
	}
	
	@Override
	public TradingSessionStatus getCloseSession() {
		for (TradingSessionStatus tradingSessionStatus : listMesg) {
			if ("LIS_AUC_C_NML".equals(tradingSessionStatus.getTradingSessionID()) && tradingSessionStatus.getTradSesStatus() == 1){
				tradingSessionStatus.setTradingSessionCode(TradSesStatus.ATC1);
				return tradingSessionStatus;
			}
		}
		return null;
	}
	
	@Override
	public TradingSessionStatus getCloseBlSession() {
		for (TradingSessionStatus tradingSessionStatus : listMesg) {
			if ("LIS_AUC_C_NML_LOC".equals(tradingSessionStatus.getTradingSessionID()) && tradingSessionStatus.getTradSesStatus() == 1){
				tradingSessionStatus.setTradingSessionCode(TradSesStatus.ATC2);
				return tradingSessionStatus;
			}
		}
		return null;
	}
	
	@Override
	public TradingSessionStatus getPtSession() {
		for (TradingSessionStatus tradingSessionStatus : listMesg) {
			if ("LIS_PTH_P_NML".equals(tradingSessionStatus.getTradingSessionID()) && tradingSessionStatus.getTradSesStatus() == 1){
				tradingSessionStatus.setTradingSessionCode(TradSesStatus.PTCLOSE);
				return tradingSessionStatus;
			}
		}
		return null;
	}
	
	@Override
	public TradingSessionStatus getEndOfDaySession() {
		for (TradingSessionStatus tradingSessionStatus : listMesg) {
			if (tradingSessionStatus.getTradSesStatus() == 97){
				tradingSessionStatus.setTradingSessionCode(TradSesStatus.ENDOFDAY);
				return tradingSessionStatus;
			}
		}
		return null;
	}
	
	@Override
	public void setSession(TradingSessionStatus sessionStatus) {
		this.currentTradingSessionStatus = sessionStatus;
	}

	@Override
	public TradingSessionStatus getCurrentSession() {
		return currentTradingSessionStatus;
	}
}
