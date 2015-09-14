package net.gabert.sdx.model;

import spark.Request;

public class DeviceModel {

	private String deviceName;
	private String devicePath;
	
	public DeviceModel(Request req) {
		this.deviceName = req.queryParams("deviceName");
		this.devicePath = req.queryParams("devicePath");
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDevicePath() {
		return devicePath;
	}
	public void setDevicePath(String devicePath) {
		this.devicePath = devicePath;
	}
	
}
