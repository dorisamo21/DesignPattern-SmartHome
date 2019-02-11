package com.edu.ccsu.models;

/**
 * Object used to marshal out JSON request to create a fan
 * @author Adrian
 *
 */
public class FanRequest {

	private String name;
	private String portNumber;
	
	public FanRequest() {
		//default constructor
	}
	
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
}