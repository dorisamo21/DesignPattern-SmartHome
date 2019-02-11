package com.edu.ccsu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.edu.ccsu.models.FanRequest;
import com.edu.ccsu.models.ResponseMessage;
import com.edu.ccsu.services.DeviceAndSensorMaker;
import com.edu.ccsu.services.FanService;

/**
 * Method to create fan objects
 * @author Adrian
 *
 */
@RestController
@RequestMapping("/fan")
public class FanController {

	@Autowired
	private FanService fanService;
	
	@Autowired
	private DeviceAndSensorMaker makerService;
	
	@RequestMapping(value = "make", method = RequestMethod.POST, produces = {"application/JSON"})
	public ResponseMessage makeFan(@RequestBody FanRequest fanRequest) {
		return makerService.makeFan(fanRequest.getName(), fanRequest.getPortNumber());
	}
	
	@RequestMapping(value = "remove/{name}", method = RequestMethod.DELETE)
	public ResponseMessage remove(@PathVariable(value = "name") String name) {
		return makerService.remove(name);
	}
	
	@RequestMapping(value = "/on/{name}", method = RequestMethod.GET)
	public ResponseMessage on(@PathVariable(value = "name") String name) {
		return fanService.turnOn(name);
	}
	
	@RequestMapping(value = "/off/{name}", method = RequestMethod.GET)
	public ResponseMessage off(@PathVariable(value = "name") String name) {
		return fanService.turnOff(name);
	}
	
	@RequestMapping(value = "/adjustBrightness/{name}/{brightness}", method = RequestMethod.GET)
	public ResponseMessage adjustBrightness(@PathVariable(value = "name") String name, @PathVariable(value = "brightness") int brightness) {
		return fanService.adjustBrightness(name, brightness);
	}
}