package com.edu.ccsu.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.edu.ccsu.models.LcdRequest;
import com.edu.ccsu.models.LedRequest;
import com.edu.ccsu.models.ResponseMessage;
import com.edu.ccsu.services.DeviceAndSensorMaker;
import com.edu.ccsu.services.LedService;

/**
 * Controller to handle requests for LightEnabledDevices.
 * Will capture different requests and redirect to appropriate service
 * @author Adrian, Ga Young, Kim
 *
 */
@RestController
@RequestMapping("/led")
public class LedController {
	
	@Autowired
	private LedService ledService;
	
	@Autowired
	private DeviceAndSensorMaker makerService;
	
	@RequestMapping(value = "/makeLed", method = RequestMethod.POST, produces = {"application/JSON"})
	public ResponseMessage makeLed(@RequestBody LedRequest ledRequest) {
		return makerService.makeLed(ledRequest.getName(), ledRequest.getPortNumber());
	}
	
	@RequestMapping(value = "/makeLcd", method = RequestMethod.POST, produces = {"application/JSON"})
	public ResponseMessage makeLcd(@RequestBody LcdRequest lcdRequest) {
		return makerService.makeLcd(lcdRequest.getName(), lcdRequest.getPortNumber(),null);
	}
	
	@RequestMapping(value = "/automate/{name}", method = RequestMethod.GET)
	public ResponseMessage automate(@PathVariable(value = "name") String name) {
		return ledService.automate(name);
	}
	
	@RequestMapping(value = "/on/{name}", method = RequestMethod.GET)
	public ResponseMessage on(@PathVariable(value = "name") String name) {
		return ledService.turnOn(name);
	}
	
	@RequestMapping(value = "/off/{name}", method = RequestMethod.GET)
	public ResponseMessage off(@PathVariable(value = "name") String name) {
		return ledService.turnOff(name);
	}
	
	@RequestMapping(value = "/remove/{name}", method = RequestMethod.DELETE)
	public ResponseMessage remove(@PathVariable(value = "name") String name) {
		return makerService.remove(name);
	}
	
	@RequestMapping(value = "/blink/{name}/{duration}", method = RequestMethod.GET)
	public ResponseMessage blink(@PathVariable(value = "name") String name, @PathVariable(value = "duration") int duration) {
		return ledService.blink(name, duration);
	}
	
	@RequestMapping(value = "/adjustBrightness/{name}/{brightness}", method = RequestMethod.GET)
	public ResponseMessage adjustBrightness(@PathVariable(value = "name") String name, @PathVariable(value = "brightness") int brightness) {
		return ledService.adjustBrightness(name, brightness);
	}
	
	@RequestMapping(value = "/changeColor/{name}/{color}", method = RequestMethod.GET)
	public ResponseMessage changeColor(@PathVariable(value = "name") String name, @PathVariable(value = "color") String color) {
		return ledService.changeColor(name, color);
	}
	
	@RequestMapping(value = "/printMessage", method = RequestMethod.POST,produces = {"application/JSON"}, consumes = {"application/JSON"})
	public ResponseMessage printMessage(@RequestBody LcdRequest lcdRequest) {
		if(!StringUtils.isEmpty(lcdRequest.getColor())) {
			return ledService.printMessage(lcdRequest.getName(), lcdRequest.getMessage(), lcdRequest.getColor());
		}
		else {
			return ledService.printMessage(lcdRequest.getName(), lcdRequest.getMessage(), null);
		}
	}
	
	@RequestMapping(value = "/printMessage/{name}/{message}/{color}", method = RequestMethod.POST)
	public ResponseMessage write(@PathVariable(value = "name") String name,@PathVariable(value = "message") String message,@PathVariable(value = "color") String color) {
		return ledService.printMessage(name,message,color);
	}
	
	@RequestMapping(value = "getWeather/{screenName}/{city}", method = RequestMethod.GET)
	public ResponseMessage getWeather(@PathVariable(value = "screenName") String screenName,
									  @PathVariable(value = "city") String city) {
		ResponseMessage msg = null;
		try {
			msg = ledService.printWeather(screenName, city).get();
		} 
		catch (InterruptedException | ExecutionException e) {
			msg = new ResponseMessage.ResponseBuilder(screenName)
					.withStatus(500)
					.andError(e.getMessage())
					.deviceType("Lcd")
					.build();
		}
		return msg;
	}
}