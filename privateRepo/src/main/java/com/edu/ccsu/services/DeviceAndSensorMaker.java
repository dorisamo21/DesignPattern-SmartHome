package com.edu.ccsu.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.edu.ccsu.componentsContainer.GrovePiComponents;
import com.edu.ccsu.models.ResponseMessage;

import edu.ccsu.error.PortInUseException;
import edu.ccsu.factory.DeviceAndSensorFactory;
import edu.ccsu.interfaces.Device;
import edu.ccsu.interfaces.Fan;
import edu.ccsu.interfaces.LightEnabledDevice;
import edu.ccsu.interfaces.ProductFactory;
import edu.ccsu.interfaces.ScreenEnabledDevice;

/**
 * Service class to manage creating devices and sensors
 * @author Adrian, Ga Young, Kim
 *
 */
@Service
public class DeviceAndSensorMaker {

	private ProductFactory productFactory = new DeviceAndSensorFactory();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GrovePiComponents grovePiComponents;
		
	public DeviceAndSensorMaker() {
		//default constructor}
	}
	
	/**
	 * Used to create an LED
	 * @param name name of Led
	 * @param portNumber number of port that the led is connected
	 * @return ResponseMessage message regarding led is successfully created on port, 
	 *         if PortInUseException is thrown error message will be written
	 */
	public ResponseMessage makeLed(String name, String portNumber) {
		ResponseMessage msg = null;
		try {
			//if not unique don't create it
			if(!isDeviceUnique(name, grovePiComponents.getDevices())) {
				return buildIsNotUniqueMessage(name, msg);				
			}
			LightEnabledDevice led = productFactory.makeLightEnabledDevice("LED", name, portNumber);
			grovePiComponents.getDevices().put(led.getName(), led);
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200).withMessage("Created LED "+ name + " on port " + portNumber)
					.deviceType("Led")
					.build();
			logger.info(msg.getMessage());
		}
		catch(PortInUseException ex) {
			//NOTE 202 means accepted but we count as error because port already in use is an exception
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(202)
					.andError("Port " + portNumber + " is in use. Use another port")
					.deviceType("Led")
					.build();
			logger.error(ex.getMessage(), ex);
		}
		return msg;
	}
	
	
	/**
	 * Create the lcd object. 
	 * @param name name of Lcd
	 * @param portNumber number of port that the lcd is connected
	 * @param color If color not null set the color.
	 * @return ResponseMessage message regarding lcd is successfully created on port, 
	 *         if PortInUseException is thrown error message will be written
	 */
	public ResponseMessage makeLcd(String name, String portNumber, String color) {
		ResponseMessage msg = null;
		try {
			//if not unique don't create it
			if(!isDeviceUnique(name, grovePiComponents.getDevices())) {
				return buildIsNotUniqueMessage(name, msg);				
			}
			ScreenEnabledDevice lcd = productFactory.makeScreenEnabledDevice("LCD", name, portNumber);

			if(!StringUtils.isEmpty(color))
				lcd.setColor(color);
			grovePiComponents.getDevices().put(lcd.getName(), lcd);
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200)
					.deviceType("LCD")
					.withMessage("Created Lcd " + name + "on port "+ portNumber)
					.build();
			logger.info(msg.getMessage());
		}
		catch(PortInUseException ex) {
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(202)
					.andError("Port " + portNumber + " is in use. Use another port")
					.deviceType("Lcd")
					.build();
			logger.error(ex.getMessage(), ex);
		}
		return msg;
	}
	
	/**
	 * Method to create fan object
	 * @param name name of fan
	 * @param portNumber number of port that the fan is connected
	 * @return ResponseMessage message regarding fan is successfully created on port, 
	 *         if PortInUseException is thrown error message will be written
	 */
	public ResponseMessage makeFan(String name, String portNumber) {
		ResponseMessage msg = null;
		
		try {
			//if not unique don't create it
			if(!isDeviceUnique(name, grovePiComponents.getDevices())) {
				return buildIsNotUniqueMessage(name, msg);				
			}
			Fan fan = productFactory.makeFan("minifan", name, portNumber);
			grovePiComponents.getDevices().put(fan.getName(), fan);
			msg = new ResponseMessage.ResponseBuilder(name)
						.withStatus(200).deviceType("Fan")
						.withMessage("Successfully created " + name)
						.build();
			logger.info(msg.getMessage());
		}
		catch(PortInUseException ex) {
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(202).andError("Port " + portNumber + " is in use. Use another port")
					.deviceType("Fan")
					.build();
			logger.error(ex.getMessage(), ex);
		}
		return msg;
	}
	/**
	 * Removes a device from the map of active devices and
	 * removes the devices portNumber from list of used portNumbers
	 * @param name name of device to be removed
	 * @return ResponseMessage message if device is successfully removed 
	 */
	public ResponseMessage remove(String name) {
		ResponseMessage msg = null;
		if(grovePiComponents.removeDevice(name)) {
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200).withMessage("Removed "+ name)
					.build(); 
			logger.info(msg.getMessage());
		}
		else {
			//NOTE- 202 means accepted but not completed
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200).withMessage(name + " Not Removed")
							.build();
			logger.warn(msg.getMessage());
		}
		return msg;
	}
	
	/**
	 * check if user uses duplicate name 
	 * @param name name of device 
	 * @param devices list of devices already made
	 * @return True if device name is unique, false otherwise
	 */
	private boolean isDeviceUnique(String name, Map<String, Device> devices) {
		//don't allow users to enter duplicate names...causes issues with map because
		//we are hashing by the unique name of each device
		for(Device device: devices.values()) {
			if(name.equals(device.getName())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Use to create ResponseMessage object for case of non-uniqueness
	 * @param name name of device
	 * @param msg ResponseMessage
	 * @return Returns a ResponseMessage object
	 */
	private ResponseMessage buildIsNotUniqueMessage(String name, ResponseMessage msg) {
		msg = new ResponseMessage.ResponseBuilder(name)
				.withStatus(304)
				.withMessage("Device with name " + name + " already exists.  Create a different name")
				.build();
		logger.warn(msg.getMessage());
		return msg;
	}
}