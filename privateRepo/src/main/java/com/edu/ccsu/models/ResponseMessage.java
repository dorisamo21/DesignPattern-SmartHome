package com.edu.ccsu.models;

import java.util.Date;

/**
 * Response message to be returned by application.
 * Makes use of simple builder to allow for 
 * dynamic creation of responses
 * @author Adrian
 *
 */
public class ResponseMessage {

	private int status;
	private String message;
	private String errorMessage;
	private String deviceType;
	private String deviceName;
	private Date date;
	
	//builder class
	public static class ResponseBuilder{
		private int status;
		private String message;
		private String errorMessage;
		private String deviceType;
		private String deviceName;
		private Date date;
		
		public ResponseBuilder(String deviceName) {
			this.deviceName = deviceName;
		}
		
		public ResponseBuilder withStatus(int status) {
			this.status = status;
			this.date = new Date();
			return this;
		}
		
		public ResponseBuilder withMessage(String message) {
			this.message = message;
			return this;
		}
		
		public ResponseBuilder andError(String errorMessage) {
			this.errorMessage = errorMessage;
			return this;
		}
		
		public ResponseBuilder deviceType(String deviceType) {
			this.deviceType = deviceType;
			return this;
		}
		
		public ResponseMessage build() {
			ResponseMessage rm = new ResponseMessage();
			rm.status = this.status;
			rm.message = this.message;
			rm.errorMessage = this.errorMessage;
			rm.deviceType = this.deviceType;
			rm.deviceName = this.deviceName;
			rm.date = this.date;
			return rm;
		}
	}
	
	private ResponseMessage() {
		//default constructor
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}