package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.Role;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.service.RoleService;
import mx.com.icsp.util.Constants;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class RoleAction extends DispatchAction{
	
	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	RoleService roleService;
	public void setRoleService(RoleService roleService){
		this.roleService = roleService;
	}
	
	public void getRoles(ActionMapping arg0, ActionForm arg1, HttpServletRequest request, HttpServletResponse response) {
		log.info("GetRoles...");
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		StringBuilder sb = new StringBuilder();		
		try {
			Role[] roles = roleService.getRoles(idTransaction);			
			if (roles != null) {
				sb.append("<rows>");
				for (Role role : roles) {
					sb.append("<row id=\"" + role.getId() + "\">");
					sb.append("<cell>").append(role.getId()).append("</cell>");
					sb.append("<cell>").append(role.getRole()).append("</cell>");
					sb.append("<cell>").append(role.getDescription()).append("</cell>");
					sb.append("<cell>").append(sdf.format(role.getRegisterDate())).append("</cell>");
					sb.append("<cell>").append(sdf.format(role.getLastUpdate())).append("</cell>");
					sb.append("</row>");
				}
				sb.append("</rows>");
			} else {
				sb.append("<error>No se encontraron registros</error>");
			}
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			sb.append("<response>");
			sb.append("<responseCode>").append(100).append("</responseCode>");
			sb.append("<responseMsg>").append("Error al consultar catalogo").append("</responseMsg>");
			sb.append("</response>");
		}
		setResponse(request, response, sb);
	}
	
	

	public void insertRole(ActionMapping arg0, ActionForm arg1, HttpServletRequest request, HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();	
		try {
			String vrole = gerParameterString(request, "valueRole");
			String description = gerParameterString(request, "descriptionRole");
			
			Role role = new Role();
			role.setRole(vrole);
			role.setDescription(description);
		
			log.info(logPattern.buildPattern(methodName, idTransaction, "Role", ToStringBuilder.reflectionToString(role)));

			int responseCode = roleService.insertRole(idTransaction, role);
			if (responseCode == 1) {
				sb.append("<response>");
				sb.append("<responseCode>").append(0).append("</responseCode>");
				sb.append("<responseMsg>").append("Se inserto registro");
				sb.append("</responseMsg>");
				sb.append("</response>");
			} else {
				sb.append("<response>");
				sb.append("<responseCode>").append(101).append("</responseCode>");
				sb.append("<responseMsg>").append("Error en la BD al insertar registro").append("</responseMsg>");
				sb.append("</response>");
			}

		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			sb.append("<response>");
			sb.append("<responseCode>").append(103).append("</responseCode>");
			sb.append("<responseMsg>").append("Error al insertar registro")
			.append("</responseMsg>");
			sb.append("</response>");
		}
		setResponse(request, response, sb);
	}

	public void setResponse(HttpServletRequest request, HttpServletResponse response, StringBuilder sb){
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();		
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setContentType("application/xml");
		response.setHeader("Pragma", "no-cache");// set HTTP/1.0 no-cache
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(sb.toString());
		} catch (IOException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
		}
	}
	
	public String gerParameterString(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? request.getParameter(name) : null;
	}	
	public String gerParameterString(HttpServletRequest request, String name, String def){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? request.getParameter(name) : def;
	}
	public int gerParameterInt(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Integer.parseInt(request.getParameter(name)) : -1;
	}	
	public long gerParameterLong(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Long.parseLong(request.getParameter(name)) : -1;
	}	
	public float gerParameterFloat(HttpServletRequest request, String name){
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? Float.parseFloat(request.getParameter(name)) : -1;
	}	
	public Date getParameterDate(HttpServletRequest request, String name) throws ParseException{
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? sdf.parse(request.getParameter(name)) : null;
	}	
	public Date getParameterDate(HttpServletRequest request, String name, Date def) throws ParseException{
		return request.getParameter(name) != null && !request.getParameter(name).trim().equals("") ? sdf.parse(request.getParameter(name)) : def;
	}
	
}
