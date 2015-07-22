package mx.com.icsp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.icsc.common.User;
import mx.com.icsc.common.util.LogPattern;
import mx.com.icsp.service.UserService;
import mx.com.icsp.util.Constants;
import mx.com.icsp.util.Encoder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class UserAction extends DispatchAction{

	Logger log = Logger.getLogger(this.getClass());
	LogPattern logPattern = new LogPattern(Constants.domainCode,
			Constants.solutioNameCode, Constants.platform, Constants.tower,
			this.getClass().getName());

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	UserService userService;
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	public void getUsers(ActionMapping arg0, ActionForm arg1, HttpServletRequest request, HttpServletResponse response) {
		log.info("GetUsers...");
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();
		StringBuilder sb = new StringBuilder();		
		try {
			User[] users = userService.getUsers(idTransaction);
			if (users != null) {
				sb.append("<rows>");
				for (User user : users) {
					sb.append("<row id=\"" + user.getId() + "\">");
//					sb.append("<cell>").append(user.getId()).append("</cell>");
					sb.append("<cell>").append(user.getUsername()).append("</cell>");
					sb.append("<cell>").append(user.getAuthority()).append("</cell>");
					sb.append("<cell>").append(user.getName()).append("</cell>");
					sb.append("<cell>").append(user.getLastName()).append("</cell>");
					sb.append("<cell>").append(user.getSex()).append("</cell>");
					sb.append("<cell>").append(user.getBirth() != null ? sdf.format(user.getBirth()):"").append("</cell>");
					sb.append("<cell>").append(user.getRegisterDate() != null ? sdf.format(user.getRegisterDate()):"").append("</cell>");
					sb.append("<cell>").append(user.getLastUpdate() != null ? sdf.format(user.getLastUpdate()):"").append("</cell>");
					sb.append("</row>");
				}
				sb.append("</rows>");
				log.info("sb: " + sb.toString());
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

	public void insertUser(ActionMapping arg0, ActionForm arg1, HttpServletRequest request, HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();	
		try {
			Date date = sdf.parse("01/06/2015");
			String username = gerParameterString(request, "username");
			String password = gerParameterString(request, "passUser");
			String authority = gerParameterString(request, "authority");
			String enabled = gerParameterString(request, "statusUser");
			String name = gerParameterString(request,"nameUser");
			String lastName = gerParameterString(request,"lastnameUser");
			String sex = gerParameterString(request, "sexUser");
			Date birth = getParameterDate(request, "birthUser", date);
			
			String epassword = Encoder.encryptedPasswordAcegi(password, username);
			
			User user = new User();
			user.setUsername(username);
			user.setPassword(epassword);
			user.setAuthority(authority);
			user.setEnabled(enabled);
			user.setName(name);
			user.setLastName(lastName);
			user.setSex(sex.charAt(0));
			user.setBirth(birth);

			log.info(logPattern.buildPattern(methodName, idTransaction, "User", ToStringBuilder.reflectionToString(user)));

			int responseCode = userService.insertUser(idTransaction, user);
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
		} catch (ParseException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "ParseException", e.getMessage()), e);
			sb.append("<response>");
			sb.append("<responseCode>").append(102).append("</responseCode>");
			sb.append("<responseMsg>").append("Error al interpretar la fecha")
			.append("</responseMsg>");
			sb.append("</response>");
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

	public void updateUser(ActionMapping arg0, ActionForm arg1, HttpServletRequest request, HttpServletResponse response) {
		log.info("Updating user...");
		StringBuilder sb = new StringBuilder();		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();	
		try {
			Date date = sdf.parse("01/06/2015");
			int idUser = gerParameterInt(request, "idUser");
			String username = gerParameterString(request, "username");
			String password = gerParameterString(request, "passUser");
			String authority = gerParameterString(request, "authority");
			String enabled = gerParameterString(request, "statusUser");
			String name = gerParameterString(request,"nameUser");
			String lastName = gerParameterString(request,"lastnameUser");
			String sex = gerParameterString(request, "sexUser");
			Date birth = getParameterDate(request, "birthUser", date);
			
			String epassword = Encoder.encryptedPasswordAcegi(password, username);
			
			User user = new User();
			user.setId(idUser);
			user.setUsername(username);
			user.setPassword(epassword);
			user.setAuthority(authority);
			user.setEnabled(enabled);
			user.setName(name);
			user.setLastName(lastName);
			user.setSex(sex.charAt(0));
			user.setBirth(birth);

			log.info(logPattern.buildPattern(methodName, idTransaction, "User", ToStringBuilder.reflectionToString(user)));

			int responseCode = userService.updateUser(idTransaction, user);
			if (responseCode == 1) {
				sb.append("<response>");
				sb.append("<responseCode>").append(0).append("</responseCode>");
				sb.append("<responseMsg>").append("Se actualizo registro");
				sb.append("</responseMsg>");
				sb.append("</response>");
			} else {
				sb.append("<response>");
				sb.append("<responseCode>").append(101).append("</responseCode>");
				sb.append("<responseMsg>").append("Error en la BD al actualizar registro").append("</responseMsg>");
				sb.append("</response>");
			}
		} catch (ParseException e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "ParseException", e.getMessage()), e);
			sb.append("<response>");
			sb.append("<responseCode>").append(102).append("</responseCode>");
			sb.append("<responseMsg>").append("Error al interpretar la fecha")
			.append("</responseMsg>");
			sb.append("</response>");
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			sb.append("<response>");
			sb.append("<responseCode>").append(103).append("</responseCode>");
			sb.append("<responseMsg>").append("Error al actualizar registro")
			.append("</responseMsg>");
			sb.append("</response>");
		}
		setResponse(request, response, sb);
	}
	
	public void deleteUser(ActionMapping arg0, ActionForm arg1, HttpServletRequest request, HttpServletResponse response) {
		log.info("Deleting user...");
		StringBuilder sb = new StringBuilder();		
		String idTransaction = request.getSession().getId();
		String methodName = new Throwable().getStackTrace()[0].getMethodName();	
		try {
			int idUser = gerParameterInt(request, "idUser");
			log.info("idUser= " + idUser);
			int responseCode = userService.deleteUser(idTransaction, idUser);			
			if (responseCode == 1) {
				sb.append("<response>");
				sb.append("<responseCode>").append(0).append("</responseCode>");
				sb.append("<responseMsg>").append("Se elimino registro");
				sb.append("</responseMsg>");
				sb.append("</response>");
			} else {
				sb.append("<response>");
				sb.append("<responseCode>").append(101).append("</responseCode>");
				sb.append("<responseMsg>").append("Error en la BD al elminar registro").append("</responseMsg>");
				sb.append("</response>");
			}
		} catch (Exception e) {
			log.error(logPattern.buildPattern(methodName, idTransaction, "Exception", e.getMessage()), e);
			sb.append("<response>");
			sb.append("<responseCode>").append(103).append("</responseCode>");
			sb.append("<responseMsg>").append("Error al eliminar registro")
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
