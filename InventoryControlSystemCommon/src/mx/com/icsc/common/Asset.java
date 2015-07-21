package mx.com.icsc.common;

import java.util.Date;

public class Asset {
	private long id;
	
	private long idLedger;
	private String idSubclass;
	private String subclass;
	
	private String description;
	private String brand;
	private String model;
	private String serialNumber;
	private String material;
	private String color;
	private String supplier;
	private String generalManager;
	private String directlyResponsible;
	private long tag;
	private String bill;
	private Date billingDate;
	private String location;
	private Date useDate;
	private float price;
	private String place;
	private String generalLocation;
	private String secure;
	private String start;
	private Date registerDate;
	private Date lastUpdate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdLedger() {
		return idLedger;
	}
	public void setIdLedger(long idLedger) {
		this.idLedger = idLedger;
	}
	public String getIdSubclass() {
		return idSubclass;
	}
	public void setIdSubclass(String idSubclass) {
		this.idSubclass = idSubclass;
	}
	public String getSubclass() {
		return subclass;
	}
	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getGeneralManager() {
		return generalManager;
	}
	public void setGeneralManager(String generalManager) {
		this.generalManager = generalManager;
	}
	public String getDirectlyResponsible() {
		return directlyResponsible;
	}
	public void setDirectlyResponsible(String directlyResponsible) {
		this.directlyResponsible = directlyResponsible;
	}
	public long getTag() {
		return tag;
	}
	public void setTag(long tag) {
		this.tag = tag;
	}
	public String getBill() {
		return bill;
	}
	public void setBill(String bill) {
		this.bill = bill;
	}
	public Date getBillingDate() {
		return billingDate;
	}
	public void setBillingDate(Date billingDate) {
		this.billingDate = billingDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getUseDate() {
		return useDate;
	}
	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getGeneralLocation() {
		return generalLocation;
	}
	public void setGeneralLocation(String generalLocation) {
		this.generalLocation = generalLocation;
	}
	public String getSecure() {
		return secure;
	}
	public void setSecure(String secure) {
		this.secure = secure;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}	
}
