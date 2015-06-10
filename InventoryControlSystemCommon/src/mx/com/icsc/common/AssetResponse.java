package mx.com.icsc.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AssetResponse {

	private int responseCode;
	private String responseMessage;
	
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
}
