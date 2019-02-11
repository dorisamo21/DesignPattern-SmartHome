package com.edu.ccsu.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Class to be used to marshall out requests to 
 * @author Adrian
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LedRequest {

	private String name;
	private String portNumber;
	private String message;
	private String color;
	
	public LedRequest() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}