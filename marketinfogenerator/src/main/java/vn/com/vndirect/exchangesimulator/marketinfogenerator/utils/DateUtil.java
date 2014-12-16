package vn.com.vndirect.exchangesimulator.marketinfogenerator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private static final String YYYYMMDD_FORMAT = "yyyy/MM/dd";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(YYYYMMDD_FORMAT);
	
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
	
	public static String getCurrentDate(SimpleDateFormat sDateFormat) {
		return sDateFormat.format(new Date());
	}
	
	public static Date createDateTime(String time) throws ParseException {
		return dateTimeFormat.parse(String.format("%s %s", getCurrentDate(dateFormat), time));
	}
}
