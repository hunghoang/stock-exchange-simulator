package vn.com.vndirect.exchangesimulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;

@Component("SecurityService")
public class SecurityServiceImpl implements SecurityService {
	@Autowired
	private InMemory inMemory;
	
	@Override
	public SecurityStatus getSecurityBySymbol(String symbol) {
		return (SecurityStatus)inMemory.get("securitystatus", symbol);
	}

}
