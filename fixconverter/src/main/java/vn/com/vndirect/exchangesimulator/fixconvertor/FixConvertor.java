package vn.com.vndirect.exchangesimulator.fixconvertor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import vn.com.vndirect.exchangesimulator.model.CrossOrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.Group;
import vn.com.vndirect.exchangesimulator.model.GroupSide;
import vn.com.vndirect.exchangesimulator.model.Logon;
import vn.com.vndirect.exchangesimulator.model.NewOrderCross;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.model.SecurityStatusRequest;
import vn.com.vndirect.exchangesimulator.model.TestRequest;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatusRequest;
import vn.com.vndirect.exchangesimulator.utils.Constant;
import vn.com.vndirect.lib.commonlib.file.FileUtils;
import vn.com.vndirect.lib.commonlib.properties.OrderedProperties;

@Component
public class FixConvertor {
	private static final Logger log = Logger.getLogger(FixConvertor.class);
	private static final String FIXTAS_PATH = "config/fixtags.properties";

	private Properties properties;
	private Map<String, String> mapFixTags;
	private static final SimpleDateFormat CURRENT_FORMAT_DATETIME = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss zzz yyyy");
	private static final SimpleDateFormat HNX_FORMAT_DATETIME = new SimpleDateFormat(
			"yyyyMMdd-HH:mm:ss");

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
		String message = createBody(obj);
		return appendHeaderAndTail(message);
	}

	private String createBody(Object obj) {
		Enumeration<?> e = properties.propertyNames();
		StringBuilder fixBuilder = new StringBuilder();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String tagNumber = properties.getProperty(key);
			String value;
			try {
				value = BeanUtils.getProperty(obj, key);
				value = convertAndFormatDateTime(value);

			} catch (Exception exception) {
				continue;
			}
			if (value != null) {
				fixBuilder.append(properties.getProperty(key))
						.append(Constant.EQUAL).append(value)
						.append(Constant.SOH);
			}
			if (isGroup(tagNumber)) {
				addGroup(fixBuilder, obj);
			}
		}

		String message = fixBuilder.toString();
		return message;
	}

	private String appendHeaderAndTail(String message) {
		message = Constant.BEGINMESS + Constant.SOH + "#" + message;
		int l = Constant.BEGINMESS.length() + Constant.SOH.length() + 8;
		message = message.replace("#", "9=" + (message.length() - l)
				+ Constant.SOH);
		return message;
	}

	private void addGroup(StringBuilder fixBuilder, Object obj) {
		if (NewOrderCross.class.isInstance(obj)) {
			fixBuilder.append(createBody(((NewOrderCross) obj).getGroupSides().get(0)));
			fixBuilder.append(createBody(((NewOrderCross) obj).getGroupSides().get(1)));
		}
		
	}

	private String convertAndFormatDateTime(String value) {
		if (value.contains(".0")) {
			return value.replace(".0", "");
		}
		try {
			Date date = CURRENT_FORMAT_DATETIME.parse(value);
			return HNX_FORMAT_DATETIME.format(date);
		} catch (ParseException e) {

		}

		return value;
	}

	public Object convertFixToObject(String message) {
		if (isLogonMessage(message)) {
			return convertFixToObject(message, Logon.class);
		}

		if (isSecurityStatusRequestMessage(message)) {
			return convertFixToObject(message, SecurityStatusRequest.class);
		}

		if (isTradingSessionStatusRequestMessage(message)) {
			return convertFixToObject(message,
					TradingSessionStatusRequest.class);
		}

		if (isNewOrderSingle(message)) {
			return convertFixToObject(message, NewOrderSingle.class);
		}

		if (isOrderReplaceRequest(message)) {
			return convertFixToObject(message, OrderReplaceRequest.class);
		}

		if (isOrderCancelRequest(message)) {
			return convertFixToObject(message, OrderCancelRequest.class);
		}

		if (isTestRequestMessage(message)) {
			return convertFixToObject(message, TestRequest.class);
		}
		
		if (isNewOrderCrossMessage(message)) {
			return convertFixToObject(message, NewOrderCross.class);
		}

		if (isCrossOrderCancelRequest(message)) {
			return convertFixToObject(message, CrossOrderCancelRequest.class);
		}

		return message;
	}

	private boolean isTestRequestMessage(String message) {
		return message.indexOf("35=1") > 0;
	}

	private boolean isOrderCancelRequest(String message) {
		return message.indexOf("35=F") > 0;
	}

	private boolean isOrderReplaceRequest(String message) {
		return message.indexOf("35=G") > 0;
	}

	private boolean isNewOrderSingle(String message) {
		return message.indexOf("35=D") > 0;
	}

	private boolean isTradingSessionStatusRequestMessage(String message) {
		return message.indexOf("35=g") > 0;
	}

	private boolean isSecurityStatusRequestMessage(String message) {
		return message.indexOf("35=e") > 0;
	}

	private boolean isNewOrderCrossMessage(String message) {
		return message.indexOf("35=s") > 0;
	}

	private boolean isCrossOrderCancelRequest(String message) {
		return message.indexOf("35=u") > 0;
	}

	private boolean isLogonMessage(String message) {
		return message.indexOf("35=A") > 0;
	}

	public <T> T convertFixToObject(String fixMesg, Class<T> clazz) {
		Object obj;
		Object objectInGroup = null;
		int groupIndex = 0;
		int groupNumber = 0;
		try {
			obj = clazz.newInstance();
		} catch (Exception ex) {
			return null;
		}
		String[] fields = fixMesg.split(Constant.SOH);
		boolean isGroup = false;
		for (String field : fields) {
			if (field.isEmpty()) {
				continue;
			}
			String[] split = field.split(Constant.EQUAL);
			String tagNumber = split[0];
			String value = "";
			if (split.length > 1) {
				value = field.split(Constant.EQUAL)[1];
			}

			String key = getTagName(tagNumber);
			if (isGroup(tagNumber)) {
				isGroup = true;
				objectInGroup = createGroup(tagNumber);
				groupNumber = Integer.parseInt(value);
				continue;
			}

			if (key != null) {
				try {
					if (isGroup) {
						if (isFieldNotSet(objectInGroup, key)) {
							BeanUtils.setProperty(objectInGroup, key, value);
						} else {
							if (groupIndex < groupNumber) {
								Method m = obj.getClass().getMethod("addGroup", Group.class);
								m.invoke(obj, objectInGroup);
								objectInGroup = objectInGroup.getClass()
										.newInstance();
								BeanUtils.setProperty(objectInGroup, key, value);
								groupIndex++;
							} else {
								isGroup = false;
								BeanUtils.setProperty(obj, key, value);
							}
						}
					} else {
						BeanUtils.setProperty(obj, key, value);
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		return (T) obj;
	}

	private boolean isFieldNotSet(Object objectInGroup, String key) {
		try {
			Field field = objectInGroup.getClass().getDeclaredField(key);
			return BeanUtils.getProperty(objectInGroup, key) == null;
		} catch (NoSuchFieldException | NoSuchMethodException
				| InvocationTargetException | SecurityException
				| IllegalAccessException e) {
			return false;
		}
	}

	private Object createGroup(String tagNumber) {
		if ("552".equals(tagNumber)) {
			return new GroupSide();
		}
		return null;
	}

	private boolean isGroup(String tagNumber) {
		return ("552".equals(tagNumber));
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
