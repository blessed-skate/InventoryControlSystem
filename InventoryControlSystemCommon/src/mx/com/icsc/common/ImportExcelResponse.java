package mx.com.icsc.common;

public class ImportExcelResponse {

	private boolean state;
	private boolean header;
	private String name;
	private String param;
	private String info;
	private String fileName;
	private String sheetName;
	private long fileSize;
	
	private int columnLength;
	
	private Asset[] asset;
	
	private int responseCode;
	private String responseMessage;
	
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.replace("\"", "\\\"");;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param.replace("\"", "\\\"");;
				
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info.replace("\"", "\\\"");
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName.replace("\"", "\\\"");;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName.replace("\"", "\\\"");;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public int getColumnLength() {
		return columnLength;
	}
	public void setColumnLength(int columnLength) {
		this.columnLength = columnLength;
	}
	public Asset[] getAsset() {
		return asset;
	}
	public void setAsset(Asset[] asset) {
		this.asset = asset;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public boolean isHeader() {
		return header;
	}
	public void setHeader(boolean header) {
		this.header = header;
	}

}
