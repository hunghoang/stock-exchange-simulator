package vn.com.vndirect.exchangesimulator.model;

public class Logon extends HnxMessage {
	private Integer encryptMethod;
	private Integer heartBtInt;
	private String userName;
	private String text;
	private String password;
	
	public Logon() {
		setMsgType("A");		
	}
	
	public int getEncryptMethod() {
		return encryptMethod;
	}

	public void setEncryptMethod(int encryptMethod) {
		this.encryptMethod = encryptMethod;
	}

	public int getHeartBtInt() {
		return heartBtInt;
	}

	public void setHeartBtInt(int heartBtInt) {
		this.heartBtInt = heartBtInt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((encryptMethod == null) ? 0 : encryptMethod.hashCode());
		result = prime * result
				+ ((heartBtInt == null) ? 0 : heartBtInt.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		Logon other = (Logon) obj;
		if (encryptMethod == null) {
			if (other.encryptMethod != null)
				return false;
		} else if (!encryptMethod.equals(other.encryptMethod))
			return false;
		if (heartBtInt == null) {
			if (other.heartBtInt != null)
				return false;
		} else if (!heartBtInt.equals(other.heartBtInt))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
