package com.edu.ccsu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.edu.ccsu.services.FanService;
import com.edu.ccsu.services.LedService;
/**
 * Controller to return different views within application.
 * Will also contain methods to update content dynamically to the view.
 * We wanted to make use of a RESTful services to separate the 
 * frontend from the backend.  Having the view controller allows
 * us the flexiblity to plug in different front ends as we desire.
 * @author Adrian, Ga Young, Kim
 *
 */
@Controller
public class ViewController {
	
	@Autowired
	private LedService ledService;
	@Autowired
	private FanService fanService;

	@RequestMapping(value = "index")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "nav")
	public String nav() {
		return "fragments/navigationBar";
	}
	
	@RequestMapping(value = "fan")
	public String fan() {
		return "fan";
	}
	
	@RequestMapping(value = "led")
	public String led() {
		return "led";
	}

	@RequestMapping(value = "lcd")
	public String lcd() {
		return "lcd";
	}
	
	@RequestMapping(value = "listOfLeds", method = RequestMethod.GET)
	public String getLeds(Model model) {
		model.addAttribute("leds", ledService.getLeds());	
		return "fragments/activeLeds :: deviceList";
	}
	
	@RequestMapping(value = "listOfLcds", method = RequestMethod.GET)
	public String getLCDs(Model model) {
		model.addAttribute("lcds", ledService.getLcds());
		return "fragments/activeLcds :: activeLcds";
	}
	
	@RequestMapping(value = "listOfFans", method = RequestMethod.GET)
	public String getFans(Model model) {
		model.addAttribute("fans", fanService.getFans());
		return "fragments/activeFans:: deviceList";
	}
}