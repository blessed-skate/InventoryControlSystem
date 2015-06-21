package mx.com.icsp.service;

import mx.com.icsc.common.Property;

public interface PropertyService {

	void getProperty(String idTransaction);
	public Property[] getPropertys(String idTransaction);
	public int insertProperty(String idTransaction, Property property);

}
