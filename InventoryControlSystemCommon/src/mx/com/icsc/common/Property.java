package mx.com.icsc.common;

public class Property {
	
	private String key;
	private String value;
	private String defaultValue;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		if(value == null || value.trim().equals(""))
			return defaultValue;
		else 
			return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
