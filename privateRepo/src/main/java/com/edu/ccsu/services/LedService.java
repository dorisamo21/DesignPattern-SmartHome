package com.edu.ccsu.services;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.edu.ccsu.componentsContainer.GrovePiComponents;
import com.edu.ccsu.models.ResponseMessage;

import ccsu.edu.grovepicomponents.LcdScreen;
import ccsu.edu.grovepicomponents.Led;
import ccsu.edu.grovepicomponents.LightSensor;
import ccsu.edu.grovepicomponents.Sensor;
import edu.ccsu.error.IncompatibleSensorError;
import edu.ccsu.interfaces.Device;
import edu.ccsu.interfaces.LightEnabledDevice;
import edu.ccsu.interfaces.ScreenEnabledDevice;
import edu.cs505.finalproject.group4.patterns.factory.weatherfactory.InvalidWeatherProviderException;
import edu.cs505.finalproject.group4.patterns.factory.weatherfactory.WeatherService;
import edu.cs505.finalproject.group4.patterns.factory.weatherfactory.WeatherServiceCreatorFactory;
import edu.cs505.finalproject.group4.patterns.factory.weatherfactory.WeatherServiceFactory;
import edu.cs505.finalproject.group4.patterns.strategy.weatherparsing.JSONWeatherParser;
import edu.cs505.finalproject.group4.patterns.strategy.weatherparsing.WeatherData;
import edu.cs505.finalproject.group4.patterns.strategy.weatherparsing.WeatherParser;
import edu.cs505.finalproject.group4.patterns.template.weatherprinter.WeatherDataPrinterInterface;
import edu.cs505.finalproject.group4.patterns.template.weatherprinter.WeatherPrinterInUnits;

/**
 * Service class used to handle interactions with our 
 * custom GrovePi library
 * @author Adrian, Ga Young, Kim
 *
 */
@Service
public class LedService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GrovePiComponents grovePiComponents;
	
	public LedService() {
		//default constructor
	}
	
	/**
	 * Given a sensor will run automate mode for led
	 * @param name
	 * @param sensor
	 * @return
	 */
	public ResponseMessage automate(String name) {
		ResponseMessage msg = null;
		LightEnabledDevice led = (LightEnabledDevice) grovePiComponents.getDevices().get(name);
		//TODO - add check to make sure not to activate on none pwm ports
		Sensor sensor = new LightSensor("LightSensor", "A0");
		//NOTE temporary hack creating sensor on A0
		
		if(led != null) {
			try {
				led.automate(sensor);
			}
			catch(IncompatibleSensorError ex) {
				logger.error(ex.getMessage());
				//406 -> Not Acceptable
				msg = new ResponseMessage.ResponseBuilder(name)
						.withStatus(406)
						.andError("Using Incorrect sensor type for lightenableddevice " + name)
						.build();
				return msg;
			}
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200)
					.withMessage("Activated automate mode for " + name)
					.deviceType("LightEnabledDevice")
					.build();
			logger.info(msg.getMessage());
		}
		else {
			msg = notFound(name, "LightEnabledDevice");
			logger.info(msg.getErrorMessage());
		}
		return msg;
	}
	

	//NOTE- null checks done because technically we are writing a REST service...can't assume that some
	//other consumer of the service would pass in a name of a device that has been created
	//TODO -> could remove these checks we are the only ones using it
	/**
	 * Used to turn on the led
	 * @param name
	 * @return
	 */
	public ResponseMessage turnOn(String name) {
		ResponseMessage msg = null;	
		LightEnabledDevice led = (LightEnabledDevice) grovePiComponents.getDevices().get(name);
		if(led != null) {
			led.turnOn();
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200)
					.withMessage("Turned on " + name)
					.deviceType("LightEnabledDevice")
					.build();
			logger.info(msg.getMessage());
		}
		else {
			msg = notFound(name, "LightEnabledDevice");
			logger.warn(msg.getErrorMessage());
		}
		return msg;
	}
	
	/**
	 * Used to turn off the LightEnabledDevice
	 * @param name name of LightEnabledDevice
	 * @return ResponseMessage message regarding lightEnabledDevice is successfully turned off or not 
	 */
	public ResponseMessage turnOff(String name) {
		ResponseMessage msg = null;
		LightEnabledDevice led = (LightEnabledDevice) grovePiComponents.getDevices().get(name);
		if(led != null) {
			led.turnOff();
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200)
					.withMessage("Turned off " + name)
					.deviceType("LightEnabledDevice")
					.build();
			logger.info(msg.getMessage());
		}
		else {
			msg = notFound(name, "LightEnabledDevice");
			logger.warn(msg.getErrorMessage());
		}
		return msg;
	}
	
	//TODO- consider removing this method if we won't use it
	/**
	 * Used to make led blink for certain duration
	 * @param name name of LightEnabledDevice
	 * @param duration
	 * @return ResponseMessage holds message that LightEnabledDevice is blinking or error warning
	 */
	public ResponseMessage blink(String name, int duration) {
		ResponseMessage msg = null;
		LightEnabledDevice led = (LightEnabledDevice) grovePiComponents.getDevices().get(name);
		if(led != null) {
			led.blink(duration);
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200)
					.withMessage("Blinked " + name)
					.deviceType("LightEnabledDevice")
					.build();
			logger.info(msg.getMessage());
		}
		else {
			msg = notFound(name, "LightEnabledDevice");
			logger.warn(msg.getErrorMessage());
		}
		return msg;
	}
	
	/**
	 * Adjust the brightness of the LightEnabledDevice
	 * @param name name of device
	 * @param brightness brightness
	 * @return ResponseMessage holds message about adjusting brightness of LightEnabledDevice or error warning
	 */
	public ResponseMessage adjustBrightness(String name, int brightness) {
		ResponseMessage msg = null;
		LightEnabledDevice led = (LightEnabledDevice) grovePiComponents.getDevices().get(name);
		if(led != null) {
			led.adjustBrightness(brightness);
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200)
					.withMessage("Adjusted brightness of " + name)
					.deviceType("LightEnabledDevice")
					.build();
			msg.setStatus(200);
			msg.setMessage("Success");
			logger.info(msg.getMessage());
		}
		else {
			msg = notFound(name, "LightEnabledDevice");
			logger.info(msg.getErrorMessage());
		}
		return msg;
	}

	/**
	 * Change backlight color of Lcdscreen
	 * @param name name of LcdScreen
	 * @param color color to change
	 * @return ResponseMessage holds message about changing color of LCD or error warning
	 */
	public ResponseMessage changeColor(String name, String color) {
		ResponseMessage msg = null;
		ScreenEnabledDevice lcd = (ScreenEnabledDevice) grovePiComponents.getDevices().get(name);
		if(lcd != null) {
			lcd.printMessageColor("_", color);
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(200)
					.withMessage("Change color of " + name)
					.deviceType("ScreenEnabledDevice")
					.build();
			msg.setStatus(200);
			msg.setMessage("Changing color to "+color+" Succeeded");
			logger.info(msg.getMessage());
		}
		else {
			msg = new ResponseMessage.ResponseBuilder(name)
					.withStatus(404)
					.andError(name + " Not Found")
					.deviceType("ScreenEnabledDevice")
					.build();
			logger.info(msg.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * 
	 * @return List of lcds in grovepi components
	 */
	public List<Device> getLcds(){
		Map<String, Device> ledMap = grovePiComponents.getDevices();
		List<Device> devices = new ArrayList<>();
		for(Device device: ledMap.values()) {
			if(device instanceof LcdScreen) {
				devices.add(device);
			}
		}
		return devices;
	}
	
	
	/**
	 * getLcdScreen
	 * @return List of lcds in grovepi components
	 */
	public List<Device> getLcdScreen(){
		Map<String, Device> lcdMap = grovePiComponents.getDevices();
		List<Device> lcds = new ArrayList<>();
		for(Device device: lcdMap.values()) {
			if(device instanceof LcdScreen) {
				lcds.add(device);
			}
		}
		return lcds;
	}	
	
	/**
	 * 
	 * @return List of leds in grovepi components
	 */
	public List<Device> getLeds(){
		Map<String, Device> ledMap = grovePiComponents.getDevices();
		List<Device> leds = new ArrayList<>();
		for(Device device: ledMap.values()) {
			if(device instanceof Led) {
				leds.add(device);
			}
		}
		return leds;
	}

	/**
	 * 
	 * @return Device which has the name as lcdName in grovepi components
	 */
	public Device getLcd(String lcdName){
		Map<String, Device> ledMap = grovePiComponents.getDevices();
		for(Device device: ledMap.values()) {
			if(device.getName().equals(lcdName)) {
				return device;
			}
		}
		return null;
	}	

    /**
     * Method used to print message to Lcd screen.  Will use default color if
     * color parameter is null
     * @param name name of LcdScreen
     * @param message message on LcdScreen
     * @param color Color of LcdScreen
     * @return ResponseMessage 
     */
    public ResponseMessage printMessage(String name, String message, String color) {
            ResponseMessage msg = null;
            ScreenEnabledDevice lcd = (ScreenEnabledDevice) grovePiComponents.getDevices().get(name);
            if(lcd != null) {
                    if(color == null)
                            lcd.printMessage(message);
                    else
                            lcd.printMessageColor(message, color);
                    msg = new ResponseMessage.ResponseBuilder(name)
        					.withStatus(200)
        					.withMessage("Printed message to " + name)
        					.deviceType("Lcd")
        					.build();
                    logger.info(msg.getMessage());
            }
            else {
            	msg = notFound(name, "Lcd");
                logger.warn(msg.getErrorMessage());
            }
            return msg;
    }	
    
    /**
     * Given a city name, use group 4's public component to retrieve the weather data 
     * and print to the lcd screen if active
     * @param screenName
     * @param city
     * @return
     */
    @Async
    public CompletableFuture<ResponseMessage> printWeather(String screenName, String city) {
    	ResponseMessage msg = null;
    	//Before setup check that there is an lcd screen 
    	ScreenEnabledDevice lcd = (ScreenEnabledDevice) getLcd(screenName);
    	
    	if(lcd != null) {
    		try {
    			logger.info("Entering Async Call to get weather data");
    			//create weather service and get the data
    			WeatherServiceFactory creatorFactory = new WeatherServiceCreatorFactory();
    			WeatherService openWebMapsService = creatorFactory.getWeatherService("OWM"); 
    			String todayWeather = openWebMapsService.getWeatherData(city);
    			
    			logger.info(todayWeather);
    			//if todayWeather is empty or null then service call failed 
    			if(!StringUtils.isEmpty(todayWeather)) {
    				logger.info("Parsing weather data");
    				WeatherParser parser = new WeatherParser();
    				parser.setParser(new JSONWeatherParser());
    				WeatherData wd = parser.parseWeatherData(todayWeather);
    				//parse the data...we want units not the description based
    				WeatherDataPrinterInterface weatherPrinter = new WeatherPrinterInUnits();
    				weatherPrinter.printWeather(wd);
    				System.out.println("Weather for city " + city);
    				System.out.println(wd.toString());
    				logger.info(wd.toString());
    				Float degreesCelcius = wd.getAvgTemp();
    				DecimalFormat df = new DecimalFormat("#.00");
    				Float degreesFahrenheit = (degreesCelcius * (9/5)) + 32;
    				df.format(degreesFahrenheit);
    				lcd.printMessage("Temp in " + city + " is " + degreesFahrenheit + "F");
    				msg = new ResponseMessage.ResponseBuilder(screenName)
    						.withStatus(200)
    						.withMessage(todayWeather)
    						.deviceType("Lcd")
    						.build();
    				logger.info(msg.getMessage());
    			}
    			else {
    				msg = new ResponseMessage.ResponseBuilder(screenName)
    						.withStatus(404)
    						.andError("City " + city + " not found")
    						.build();
    				lcd.printMessage(msg.getErrorMessage());
    				logger.error(msg.getErrorMessage());
    				return CompletableFuture.completedFuture(msg);
    			}
    			
    		}
    		catch(ParseException | IOException | InvalidWeatherProviderException ex) {
    			logger.error(ex.getMessage(), ex);
    			//503 -> service failing, can see logs and error message for further details
    			msg = new ResponseMessage.ResponseBuilder(screenName)
    					.withStatus(503)
    					.andError(ex.getMessage())
    					.deviceType("Lcd")
    					.build();
    		}
    	}
    	else {
    		msg = notFound(screenName, "Lcd");
    		logger.warn(msg.getErrorMessage());
    	}
    	return CompletableFuture.completedFuture(msg);
    }

    /**
     * Generates not found response message 
     * @param name
     * @return
     */
    private ResponseMessage notFound(String name, String deviceType) {
    	return new ResponseMessage.ResponseBuilder(name)
				.withStatus(404)
				.andError(name + " Not Found")
				.deviceType(deviceType)
				.build();
    }
}