package com.edu.ccsu.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.ccsu.componentsContainer.GrovePiComponents;
import com.edu.ccsu.models.ResponseMessage;

import edu.ccsu.interfaces.Device;
import edu.ccsu.interfaces.Fan;

@Service
public class FanService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GrovePiComponents grovePiComponents;
	
	public FanService() {
		//default constructor
	}
	
	/**
	 * Used to turn on the fan
	 * @param name name of fan
	 * @return Returns a ResponseMessage object
	 */
	public ResponseMessage turnOn(String name) {
		ResponseMessage msg = null;	
		Fan fan = (Fan) grovePiComponents.getDevices().get(name);
		if(fan != null) {
			fan.turnOn();
			msg = new ResponseMessage.ResponseBuilder(name)
						.withStatus(200)
						.withMessage("Turned on " + name)
						.deviceType("Fan")
						.build();
			logger.info(msg.getMessage());
		}
		else {
			msg = new ResponseMessage.ResponseBuilder(name)
						.withStatus(404)
						.andError(name + " Not Found")
						.deviceType("Fan")
						.build();
			logger.warn(msg.getMessage());
		}
		return msg;
	}
	
	/**
	 * Used to turn off the fan
	 * @param name name of fan
	 * @return 	Returns a ResponseMessage object
	 */
	public ResponseMessage turnOff(String name) {
		ResponseMessage msg = null;
		Fan fan = (Fan) grovePiComponents.getDevices().get(name);
		if(fan != null) {
			fan.turnOff();
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200)
					.withMessage("Turned off " + name)
					.deviceType("Fan")
					.build();
			logger.info(msg.getMessage());
		}
		else {
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(404)
					.andError(name + " Not Found")
					.deviceType("Fan")
					.build();;
			logger.warn(msg.getMessage());
		}
		return msg;
	}
	
	/**
	 * Adjust the brightness of the fan
	 * @param name name of fan
	 * @param speed speed of fan
	 * @return Returns a ResponseMessage object
	 */
	public ResponseMessage adjustBrightness(String name, int speed) {
		ResponseMessage msg = null;
		Fan fan = (Fan) grovePiComponents.getDevices().get(name);
		if(fan != null) {
			fan.adjustSpeed(speed);;
			msg = new ResponseMessage.ResponseBuilder(name)
						.withStatus(200)
						.withMessage("Adjusted speed of " + name)
						.deviceType("Fan")
						.build();
			logger.info(msg.getMessage());
		}
		else {
			msg = new ResponseMessage.ResponseBuilder(name)
						.withStatus(404)
						.andError(name + " Not Found")
						.deviceType("Fan")
						.build();
			logger.info(msg.getMessage());
		}
		return msg;
	}
	/**
	 * 
	 * @return List of fans in grovepi components
	 */
	public List<Device> getFans(){
		Map<String, Device> ledMap = grovePiComponents.getDevices();
		List<Device> fans = new ArrayList<>();
		for(Device device: ledMap.values()) {
			if(device instanceof Fan) {
				fans.add(device);
			}
		}
		return fans;
	}
}