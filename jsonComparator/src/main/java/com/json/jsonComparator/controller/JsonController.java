package com.json.jsonComparator.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.json.jsonComparator.model.InputData;
import com.json.jsonComparator.service.JsonService;

@RestController
@RequestMapping(value="/api/v1/json")
public class JsonController {
	@Autowired
	JsonService js ;
	@RequestMapping(method=RequestMethod.POST)
	public boolean save(@RequestBody InputData data) {
		js.saveData(data);
		return true;
	}
	
	@RequestMapping(value="compare-json",method=RequestMethod.POST)
	public HashMap<String, String> compareJson(@RequestBody InputData data) throws JsonParseException, JsonMappingException, IOException {
		return js.compare(data);
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public boolean deleteData(@RequestParam("id") int id) throws JsonParseException, JsonMappingException, IOException {
		js.deleteDateById(id);
		return true;
	}
	
}
