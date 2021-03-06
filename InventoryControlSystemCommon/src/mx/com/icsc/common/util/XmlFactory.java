package mx.com.icsc.common.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import mx.com.icsc.common.AssetResponse;

import org.apache.log4j.Logger;

public class XmlFactory {
	
	static Logger log = Logger.getLogger(XmlFactory.class);
	static LogPattern logPattern = new LogPattern(Constants.domainCode, Constants.solutioNameCode, 
			Constants.platform, Constants.tower, XmlFactory.class.getName());
	
	public static String getXml(String idTransaction, AssetResponse object) {
		JAXBContext jaxbContext;		
		StringWriter sw = new StringWriter();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		
		try {
			jaxbContext = JAXBContext.newInstance(AssetResponse.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(object, sw);
//			log.info(logPattern.buildPattern(methodName, idTransaction, "AssetResponse", sw.toString()));
		} catch (PropertyException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "ParseException", e.getMessage()), e);
		} catch (JAXBException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "ParseException", e.getMessage()), e);
		}

		return sw.toString();
	}

}
