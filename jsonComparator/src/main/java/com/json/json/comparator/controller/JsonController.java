package com.json.json.comparator.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.json.json.comparator.model.InputData;
import com.json.json.comparator.service.JsonCompareService;

@RestController
@RequestMapping(value="/api/v1/json")
public class JsonController {
	@Autowired
	private JsonCompareService jsonCompare ;
	@RequestMapping(method=RequestMethod.POST)
	public boolean save(@RequestBody InputData data) {
		jsonCompare.saveData(data);
		return true;
	}
		
	@RequestMapping(method=RequestMethod.DELETE)
	public boolean deleteData(@RequestParam("id") int id) throws JsonParseException, JsonMappingException, IOException {
		jsonCompare.deleteDateById(id);
		return true;
	}
	
}
