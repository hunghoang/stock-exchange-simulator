package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;

@Component("SecurityStatusManager")
public class SecurityStatusManagerImpl implements SecurityStatusManager {
	
	private static final String SOURCE = "securitystatus.csv";
	private static final String STORE_SECURITY_TYPE = "securitystatus";

	private ObjectConvertor convertor;
	private List<SecurityStatus> listMesg;

	@Autowired
	private InMemory inmemory;

	private ObjectMapper objectMapper;

	@Autowired
	public SecurityStatusManagerImpl(ObjectConvertor objConverter) {
		this.convertor = objConverter;
		loadDataFromCSV();
		objectMapper = new ObjectMapper();
	}

	@PostConstruct
	public void pushSecurityStatusToInMemory() {
		for (SecurityStatus securityStatus : listMesg) {
			inmemory.put(STORE_SECURITY_TYPE, securityStatus.getSymbol(),
					securityStatus);
		}
	}

	private void loadDataFromCSV() {
		listMesg = convertor.convertFromCSVFile(SOURCE, SecurityStatus.class);
	}

	@Override
	public List<SecurityStatus> getSecurityStatus() {
		return listMesg;
	}

	public void setInmemory(InMemory inMemory) {
		this.inmemory = inMemory;
	}

	public void loadFromLink(String pricelink) {
		URL price;
		try {
			price = new URL(pricelink);
			URLConnection connection = price.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				sb.append(inputLine);
			in.close();

			System.out.println(sb.toString());
			JsonNode root = new ObjectMapper().readTree(sb.toString());
			JsonNode data = root.get("data");
			Iterator<JsonNode> nodes = data.getElements();
			while (nodes.hasNext()) {
				JsonNode sec = nodes.next();
				String symbol = sec.get("symbol").asText();
				double floorPx = Double
						.parseDouble(sec.get("floorPx").asText());
				double ceilingPx = Double.parseDouble(sec.get("ceilingPx")
						.asText());
				Object securityStatus = inmemory.get(STORE_SECURITY_TYPE, symbol);
				if (securityStatus != null) {
					((SecurityStatus) securityStatus).setHighPx(ceilingPx);
					((SecurityStatus) securityStatus).setLowPx(floorPx);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);

		}
	}

}
