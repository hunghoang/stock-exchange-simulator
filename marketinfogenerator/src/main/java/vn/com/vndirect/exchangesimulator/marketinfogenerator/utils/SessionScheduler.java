package vn.com.vndirect.exchangesimulator.marketinfogenerator.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManager;
import vn.com.vndirect.lib.commonlib.file.FileUtils;

@Component
public class SessionScheduler {
	
	private Timer timer;
	
	@Autowired
	private TradingSessionStatusManager tradingSessionStatusManager;
	
	private Properties properties;
	public SessionScheduler() throws IOException {
    	timer = new Timer();
    	properties = new Properties();
    	properties.load(FileUtils.getInputStream("timeconfig.properties"));
	}
	
	public void setTradingSessionStatusManager (TradingSessionStatusManager tradingSessionStatusManager) {
		this.tradingSessionStatusManager = tradingSessionStatusManager;
	}
	
	@PostConstruct
	public void initSessionFromPreOpen() throws ParseException{
		timer.schedule(new RemindPreOpenTask(), 15000l);
	}
	
	public void init() throws ParseException{
		initPreOpen();
		initOpenAM();
		initIntermission();
		initOpenPM();
    	initClose();
    	initCloseBl();
    	initPT();
    	initEndOfDay();
	}
	
    private void initPreOpen() throws ParseException {	
        timer.schedule(new RemindPreOpenTask(), DateUtil.createDateTime(properties.getProperty("PRE_OPEN")));
    }
    
    private void initOpenAM() throws ParseException {
        timer.schedule(new RemindOpenAMTask(), DateUtil.createDateTime(properties.getProperty("OPEN_1")));
    }
    
    private void initIntermission() throws ParseException {    	
    	timer.schedule(new RemindIntermissionTask(), DateUtil.createDateTime(properties.getProperty("INTERMISSION")));
    }
    
    private void initOpenPM() throws ParseException {
        timer.schedule(new RemindOpenPMTask(), DateUtil.createDateTime(properties.getProperty("OPEN_2")));
    }
    
    
    private void initClose() throws ParseException {
        timer.schedule(new RemindCloseTask(), DateUtil.createDateTime(properties.getProperty("CLOSE")));
    }
    
    private void initCloseBl() throws ParseException {
        timer.schedule(new RemindCloseBlTask(), DateUtil.createDateTime(properties.getProperty("CLOSE_BL")));
    }
    
    private void initPT() throws ParseException {    	
        timer.schedule(new RemindPtTask(), DateUtil.createDateTime(properties.getProperty("PT")));
    }
    
    private void initEndOfDay() throws ParseException {
        timer.schedule(new RemindEndOfDayTask(), DateUtil.createDateTime(properties.getProperty("END_OF_DAY")));
    }
    
    class RemindPreOpenTask extends TimerTask {
        public void run() {
        	tradingSessionStatusManager.setSession(tradingSessionStatusManager.getPreOpenSession());
        }
    }
    
    class RemindOpenAMTask extends TimerTask {
        public void run() {
        	tradingSessionStatusManager.setSession(tradingSessionStatusManager.getOpen1Session());
        }
    }
    
    class RemindIntermissionTask extends TimerTask {
        public void run() {
        	tradingSessionStatusManager.setSession(tradingSessionStatusManager.getIntermissionSession());
        }
    }
    
    class RemindOpenPMTask extends TimerTask {
        public void run() {
        	tradingSessionStatusManager.setSession(tradingSessionStatusManager.getOpen2Session());
        }
    }
    
    class RemindCloseTask extends TimerTask {
        public void run() {
        	tradingSessionStatusManager.setSession(tradingSessionStatusManager.getCloseSession());
        }
    }
    
    class RemindCloseBlTask extends TimerTask {
        public void run() {
        	tradingSessionStatusManager.setSession(tradingSessionStatusManager.getCloseBlSession());
        }
    }
    
    class RemindPtTask extends TimerTask {
        public void run() {
        	tradingSessionStatusManager.setSession(tradingSessionStatusManager.getPtSession());
        }
    }
    
    class RemindEndOfDayTask extends TimerTask {
        public void run() {
        	tradingSessionStatusManager.setSession(tradingSessionStatusManager.getEndOfDaySession());
        }
    }
}
