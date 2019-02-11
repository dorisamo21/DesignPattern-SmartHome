package com.edu.ccsu.componentsContainer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ccsu.edu.grovepicomponents.Sensor;
import edu.ccsu.interfaces.Device;
import edu.ccsu.utility.PortManagement;

/**
 * Class used to keep track of devices and sensors currently in use
 * @author Adrian, Ga Young, Kim
 *
 */
@Component
public class GrovePiComponents {

	private static Map<String, Device> devices = new HashMap<>();
	
	private static Map<String, Sensor> sensors = new HashMap<>();
	
	private static GrovePiComponents grovePiComponents = new GrovePiComponents();
	
	private static PortManagement portManagement = PortManagement.getInstance();
	
	private GrovePiComponents() {
		//default constructor
	}
	
	public static GrovePiComponents getInstance() {
		return grovePiComponents;
	}
	
	public Map<String, Device> getDevices(){
		return devices;
	}
	
	public Map<String, Sensor> getSensors(){
		return sensors;
	}
	
	/**
	 * Will remove device from the map of active objects
	 * and will alert the singleton in public repo that the 
	 * corresponding port is open for use
	 * @param name name of device removed
	 * @return true if device is successfully removed / false if there is no device with the name
	 */
	public boolean removeDevice(String name) {
		if(!StringUtils.isEmpty(name) && devices.containsKey(name)) {
			Device device = devices.get(name);
			device.turnOff();
			String portNumber = device.getPortNumber();
			devices.remove(name);
			portManagement.remove(portNumber);
			return true;
		}
		return false;
	}
	
	/**
	 * Will remove sensor from the map of active objects
	 * and will alert the singleton in public repo that the 
	 * corresponding port is open for use
	 * @param name
	 * @return
	 */
	public boolean removeSensor(String name) {
		if(sensors.containsKey(name) && !StringUtils.isEmpty(name)) {
			Sensor sensor = sensors.get(name);
			String portNumber = sensor.getName();
			sensors.remove(name);
			portManagement.remove(portNumber);
			return true;
		}
		return false;
	}
}