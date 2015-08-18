package mx.com.icsc.common;

import java.util.Date;


public class Ledger {
	
	private int id;
	private String idSubclass;
	private String description;
	private Date lastUpdate;
	private Date registerDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdSubclass() {
		return idSubclass;
	}
	public void setIdSubclass(String idSubclass) {
		this.idSubclass = idSubclass;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	
}