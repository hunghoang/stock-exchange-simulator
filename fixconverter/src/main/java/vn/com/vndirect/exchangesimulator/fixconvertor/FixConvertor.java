package vn.com.vndirect.exchangesimulator.fixconvertor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.utils.Constant;
import vn.com.vndirect.lib.commonlib.file.FileUtils;
import vn.com.vndirect.lib.commonlib.properties.OrderedProperties;

@Component
public class FixConvertor {
	private static final Logger log = Logger.getLogger(FixConvertor.class);
	private static final String FIXTAS_PATH = "config/fixtags.properties";

	private Properties properties;
	private Map<String, String> mapFixTags;
	private static final SimpleDateFormat CURRENT_FORMAT_DATETIME = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	private static final SimpleDateFormat HNX_FORMAT_DATETIME = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");

	public FixConvertor() throws IOException {
		loadProperties();
	}

	private void loadProperties() throws IOException {
		properties = new OrderedProperties();
		properties.load(FileUtils.getInputStream(FIXTAS_PATH));
		mapFixTags = new HashMap<String, String>();
		setMapTagNumber();
	}

	public String convertObjectToFix(Object obj) {
		Enumeration<?> e = properties.propertyNames();
		StringBuilder fixBuilder = new StringBuilder();
		fixBuilder.append(Constant.BEGINMESS);
		fixBuilder.append(Constant.SOH);
		fixBuilder.append("#");
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value;
			try {
				value = BeanUtils.getProperty(obj, key);
				value = convertAndFormatDateTime(value);
				
			} catch (Exception exception) {
				// log.error(exception);
				continue;
			}
			if (value != null && !value.isEmpty()) {
				fixBuilder.append(properties.getProperty(key)).append(Constant.EQUAL).append(value).append(Constant.SOH);
			}
		}

		String message = fixBuilder.toString();
		int l = Constant.BEGINMESS.length() + Constant.SOH.length() + 8;
		message = message.replace("#", "9=" + (message.length() - l) + Constant.SOH);
		return message;
	}

	
	private String convertAndFormatDateTime(String value) {
		if (value.contains(".0")){
			return value.replace(".0", "");
		}
		try {
			Date date = CURRENT_FORMAT_DATETIME.parse(value);
			return HNX_FORMAT_DATETIME.format(date);
		} catch (ParseException e) {
			
		}
		
		return value;
	}

	public <T> T convertFixToObject(String fixMesg, Class<T> clazz) {
		T obj;
		try {
			obj = clazz.newInstance();
		} catch (Exception ex) {
			return null;
		}
		String[] fields = fixMesg.split(Constant.SOH);
		for (String field : fields) {
			if (field.isEmpty()) {
				continue;
			}
			String tagNumber = field.split(Constant.EQUAL)[0];
			String value = field.split(Constant.EQUAL)[1];

			String key = getTagName(tagNumber);
			if (key != null) {
				try {
					BeanUtils.setProperty(obj, key, value);
				} catch (Exception e) {
					continue;
				}
			}
		}
		return obj;
	}

	private String getTagName(String tagNumber) {
		if (mapFixTags.containsKey(tagNumber)) {
			return mapFixTags.get(tagNumber);
		}
		return null;
	}

	private void setMapTagNumber() {
		Enumeration<?> e = properties.propertyNames();
		while (e.hasMoreElements()) {
			String tagName = (String) e.nextElement();
			String tagNumber = properties.getProperty(tagName);
			mapFixTags.put(tagNumber, tagName);
		}
	}
}
