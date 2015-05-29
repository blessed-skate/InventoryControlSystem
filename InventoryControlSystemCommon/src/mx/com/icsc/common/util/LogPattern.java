package mx.com.icsc.common.util;

/*
 * LogPattern.java 1.0 Beta 2 08/10/12
 * 
 * @author SE - Alejandro Alzaga Liera.
 *
 * (C) Copyright 2012 by IBM Corporation
 * All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of the IBM Corporation, ("Confidential Information").  Redistribution
 * of the source code or binary form is not permitted without prior
 * authorization from the IBM Corporation.
 */


/**
 * Class to represent the log pattern:
 * Idtransaction|DomainCode|SolutioNameCode|Platform|
 * Development area code(Tower)|Component code|ClassName|
 * MethodName or Flow Name|MDN=VALUE|Message
 * Example:
 * 999999999999|08|000|WPS|B|0000|parameters|getparameters|ACTVSUBS|MDN=5530301528|hello word
 * @author AALIERA
 *
 */
public class LogPattern {
	
	/**
	 * Field separator
	 */
	private char FS = '|'; 
	
	/**
	 * It is the EAF domain ID as identified in System Applications Map.
	 * 1 - Product Lifecycle Mgt (PLM)
	 * 2 - Selfcare
	 * 3 - Sales & Campaign Management
	 * ...
	 * 8 - Provisioning
	 * ...
	 * 19 - Service Assurance
	 */
	private String domainCode;
	
	/**
	 * Solution name as identified in Systems Application Map 
	 */
	private String solutioNameCode;
	
	/**
	 * Acronym of the platform where the exception was suited. For example:
	 * WAS � WebSphere Application Server
	 * WPS � WebSphere Process Server
	 * ESB � WebSphere Enterprise Service Bus
	 * JVM � Standalone Java Virtual Machine
	 * JWS � JBoss Web Service
	 */
	private String platform;
	
	/**
	 * Identifier of the development area:
	 * ID - Area - Description
	 * A - Tower A - PVS / Front End
	 * B - Tower B - Billing / Middleware
	 * C - Tower C - Administration / Datawarehouse / Logistics
	 * D - Tower D - Commercial / Mobile
	 */
	private String tower;
	
	
	/**
	 * Name of the class from which writes the log message
	 */
	private String className;

	public LogPattern(String domainCode,
			String solutioNameCode, String platform, String tower, String className) {
		
		this.domainCode = domainCode;
		this.solutioNameCode = solutioNameCode;
		this.platform = platform;
		this.tower = tower;
		this.className = className;
	}

	/**
	 * Set class name
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Return a string with the next pattern:
	 * Idtransaction|DomainCode|SolutioNameCode|Platform|
	 * Development area code(Tower)|Component code|ClassName|
	 * MethodName or Flow Name|MDN=VALUE|Message
	 * return a string with the log pattern.
	 */	
	
	public String buildPattern(String methodName, String idTransaction, String logParameter, String logParameterValue, String message)
	{
		StringBuilder sB = new StringBuilder ();
		sB.append(idTransaction).append(this.FS);
		sB.append(this.domainCode).append(this.FS);
		sB.append(this.solutioNameCode).append(this.FS);
		sB.append(this.platform).append(this.FS);
		sB.append(this.tower).append(this.FS);
		sB.append(this.className).append(this.FS);
		sB.append(methodName).append(this.FS);
		sB.append(logParameter).append("=");
		sB.append(logParameterValue).append(this.FS);
		sB.append(message);		
		return sB.toString();
	}
	
	public String buildPattern(String methodName, String idTransaction, String logParameter, String logParameterValue)
	{	
		StringBuilder sB = new StringBuilder ();
		sB.append(idTransaction).append(this.FS);
		sB.append(this.domainCode).append(this.FS);
		sB.append(this.solutioNameCode).append(this.FS);
		sB.append(this.platform).append(this.FS);
		sB.append(this.tower).append(this.FS);
		sB.append(this.className).append(this.FS);
		sB.append(methodName).append(this.FS);
		sB.append(logParameter).append("=");
		sB.append(logParameterValue);
		return sB.toString();
	}
	
	public String buildPattern(String methodName, String idTransaction, String message)
	{	
		StringBuilder sB = new StringBuilder ();
		sB.append(idTransaction).append(this.FS);
		sB.append(this.domainCode).append(this.FS);
		sB.append(this.solutioNameCode).append(this.FS);
		sB.append(this.platform).append(this.FS);
		sB.append(this.tower).append(this.FS);
		sB.append(this.className).append(this.FS);
		sB.append(methodName).append(this.FS);
		sB.append(message);		
		return sB.toString();
	}
}
