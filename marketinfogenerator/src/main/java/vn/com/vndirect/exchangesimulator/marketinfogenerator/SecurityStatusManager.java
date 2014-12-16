package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import java.util.List;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;

public interface SecurityStatusManager {
	List<SecurityStatus> getSecurityStatus();
	void setInmemory(InMemory inMemory);
}
