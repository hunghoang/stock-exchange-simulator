package vn.com.vndirect.exchangesimulator.service;

public interface SessionService {
	boolean isLO();

	boolean isATC1();

	boolean isATC2();

	boolean isPT();

	boolean isPreopen();

	boolean isClose();

	boolean isIntermission();

}
