package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import vn.com.vndirect.lib.commonlib.file.FileUtils;

@Component
public class ObjectConvertor {
	
	private static final Logger log = Logger.getLogger(ObjectConvertor.class);
	
	public <T> List<T> convertFromCSVFile(String sourceFile, Class<T> clazz) {
		List<T> listObj = new ArrayList<T>();
	    BufferedReader reader = null;
	    InputStream in;
	    String line;
	    String header = "";
		try {
			in = FileUtils.getInputStream(sourceFile);
			reader = new BufferedReader(new InputStreamReader(in));
			while ((line = reader.readLine()) != null) {
				if (header.isEmpty()) {
					header = line;
				} else {
					T obj = convertObject(clazz, header, line);
					if (obj != null) {
						listObj.add(obj);
					}
				}
			}
		} catch (IOException e) {
			log.error(e);
			return null;
		}
		
		return listObj;
	}
	
	
	
	public <T> T convertObject(Class<T> clazz, String header, String data) {
		T obj;
		DateConverter converter = new DateConverter();
		converter.setPattern("yyyyMMdd-HH:mm:ss");
		ConvertUtils.register(converter, Date.class);		
		
		try {
			obj = clazz.newInstance();
			String[] headers = header.split(",");
			String[] properties = data.split(",");
			for (int i = 0; i < properties.length; i++) {
				if (properties[i].isEmpty()) {
					continue;
				}
				try {
					BeanUtils.setProperty(obj, headers[i], properties[i]);
				} catch (Exception e) {
					throw new Exception("Exception when set property " + headers[i] +  " with value: '" + properties[i] + "'", e);
				}
			}
		} catch (Exception e) {
			log.error(e);
			return null;
		}
		return obj;
	}
}
