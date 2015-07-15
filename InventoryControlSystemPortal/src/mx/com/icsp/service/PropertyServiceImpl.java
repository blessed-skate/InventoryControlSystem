package mx.com.icsp.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.icsc.common.Property;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.persistence.dao.PropertyDao;
import mx.com.icsp.util.Constants;

import org.apache.log4j.Logger;

public class PropertyServiceImpl implements PropertyService{

	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	PropertyDao propertyDao;
	public void setPropertyDao(PropertyDao propertyDao){
		this.propertyDao = propertyDao;
	}
	
	public static Map<String, Property> map;
	
	@Override
	public void getProperty(String idTransaction) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		try{
			map = propertyDao.getProperty();
			if(map != null && map.size() > 0){
				log.info(logPattern.buildPattern(methodName, idTransaction, "Se recuperaron propiedades", String.valueOf(map.size())));
				Collection<String> collection = map.keySet();
				Iterator<String> it = collection.iterator();
				while(it.hasNext()){
					String key = it.next();
					Property value = map.get(key);
					log.info(logPattern.buildPattern(methodName, idTransaction, key, value.getValue()));
				}
			}
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
	}

	@Override
	public Property[] getPropertys(String idTransaction) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		List<Property> list = null;
		Property[] propertyArray = null;
		try{
			list = propertyDao.getPropertys();
			propertyArray = list.toArray(new Property[list.size()]);
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return propertyArray;
	}

	@Override
	public int insertProperty(String idTransaction, Property property) {
		String methodName = new Throwable().getStackTrace()[0].getMethodName();		
		int reponseCode = -1;
		try{
			reponseCode = propertyDao.insertProperty(property);
			log.info(logPattern.buildPattern(methodName, idTransaction, "reponseCode", String.valueOf(reponseCode)));
		}catch(Exception e){
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
		return reponseCode;
	}

}
