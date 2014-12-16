package vn.com.vndirect.exchangesimulator.datastorage.memory;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InMemoryKey implements Serializable {
	private String inMemoryType;
	private Object inMemoryKey;

	public InMemoryKey(String type, Object key) {
		inMemoryType = type;
		inMemoryKey = key;
	}

	public String getInMemoryType() {
		return inMemoryType;
	}

	public void setInMemoryType(String inMemoryType) {
		this.inMemoryType = inMemoryType;
	}

	public Object getInMemoryKey() {
		return inMemoryKey;
	}

	public void setInMemoryKey(Object inMemoryKey) {
		this.inMemoryKey = inMemoryKey;
	}

	

	@Override
	public String toString() {
		return "InMemoryKey [inMemoryType=" + inMemoryType + ", inMemoryKey="
				+ inMemoryKey + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inMemoryKey == null) ? 0 : inMemoryKey.hashCode());
		result = prime * result
				+ ((inMemoryType == null) ? 0 : inMemoryType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InMemoryKey other = (InMemoryKey) obj;
		if (inMemoryKey == null) {
			if (other.inMemoryKey != null)
				return false;
		} else if (!inMemoryKey.equals(other.inMemoryKey))
			return false;
		if (inMemoryType == null) {
			if (other.inMemoryType != null)
				return false;
		} else if (!inMemoryType.equals(other.inMemoryType))
			return false;
		return true;
	}

}
