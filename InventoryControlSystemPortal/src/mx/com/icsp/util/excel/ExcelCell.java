package mx.com.icsp.util.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExcelCell {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String value = "";
	private String bgColor = "";
	private String textColor = "";
	private String bold = "";
	private String italic = "";
	private String align = "";

	public void parse(Node parent) {
		//(REV)we may have empty cell, which will return null for getFirstChild
		value = parent.getFirstChild().getNodeValue();
		Element el = (Element) parent;
		bgColor = (el.hasAttribute("bgColor")) ? el.getAttribute("bgColor") : "";
		textColor = (el.hasAttribute("textColor")) ? el.getAttribute("textColor") : "";
		bold = (el.hasAttribute("bold")) ? el.getAttribute("bold") : "";
		italic = (el.hasAttribute("italic")) ? el.getAttribute("italic") : "";
		align = (el.hasAttribute("align")) ? el.getAttribute("align") : "";
	}

	public String getValue() {
		return value;
	}
	
	public long getLongValue() {
		if(isLong())
			return Long.parseLong(value);
		else 
			return 0;
	}
	
	public float getFloatValue() {
		if(isFloat())
			return Float.parseFloat(value);
		else 
			return 0;
	}
	
	public Date getDateValue() {
		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getBgColor() {
		return bgColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public Boolean getBold() { //(REV)why we store string , and not boolean?
		if (bold.equals("bold"))
			return true;
		else
			return false;
	}

	public Boolean getItalic() { //(REV)why we store string , and not boolean?
		if (italic.equals("italic"))
			return true;
		else
			return false;
	}

	public String getAlign() {
		return align;
	}
	
	private boolean isLong(){
		try{
			Long.parseLong(value);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	private boolean isFloat(){
		try{
			Float.parseFloat(value);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

}