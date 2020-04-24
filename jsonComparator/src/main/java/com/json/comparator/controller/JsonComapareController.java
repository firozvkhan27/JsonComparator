package com.json.comparator.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.json.comparator.model.InputData;
import com.json.comparator.service.JsonCompareService;

@RestController
@RequestMapping(value="/api/v1/compare/")
public class JsonComapareController {
	@Autowired
	private JsonCompareService jsonCompare ;
	
	@RequestMapping(value="jsons",method=RequestMethod.POST)
	public Map<String, Object> compareJson(@RequestBody InputData inputData) throws JsonParseException, JsonMappingException, IOException {
		return jsonCompare.compare(inputData);
	}


}
