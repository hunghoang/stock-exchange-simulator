package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;

@Component("SecurityStatusManager")
public class SecurityStatusManagerImpl implements SecurityStatusManager{
	private static final String SOURCE = "securitystatus.csv";
	private static final String STORE_SECURITY_TYPE = "securitystatus";
	
	private ObjectConvertor convertor;
	private List<SecurityStatus> listMesg;
	
	@Autowired
	private InMemory inmemory;
	
	@Autowired
	public SecurityStatusManagerImpl(ObjectConvertor objConverter) {
		this.convertor = objConverter;
		loadDataFromCSV();
	}
	
	@PostConstruct
	public void pushSecurityStatusToInMemory() {
		for (SecurityStatus securityStatus : listMesg) {
			inmemory.put(STORE_SECURITY_TYPE, securityStatus.getSymbol(), securityStatus);
		}
	}

	private void loadDataFromCSV(){
		listMesg = convertor.convertFromCSVFile(SOURCE, SecurityStatus.class);
	}

	@Override
	public List<SecurityStatus> getSecurityStatus() {
		return listMesg;
	}
	
	public void setInmemory(InMemory inMemory) {
		this.inmemory = inMemory;
	}
}
