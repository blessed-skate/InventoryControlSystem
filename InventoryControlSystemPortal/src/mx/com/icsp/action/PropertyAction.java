package mx.com.icsp.action;

import mx.com.icsp.service.PropertyService;

import org.apache.struts.actions.DispatchAction;

public class PropertyAction extends DispatchAction{
	
	PropertyService propertyService;
	public void setPropertyService(PropertyService propertyService){
		this.propertyService = propertyService;
	}

}
