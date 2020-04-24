package com.json.comparator.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.json.comparator.exceptions.JsonComparatorExceptions;
import com.json.comparator.model.InputData;
import com.json.comparator.service.JsonCompareService;

@RestController
@RequestMapping(value="/api/v1/compare/")
public class JsonComapareController {
	@Autowired
	private JsonCompareService jsonCompare ;
	
	@PostMapping(value="jsons")
	public Map<String, Object> compareJson(@RequestBody InputData inputData) throws IOException, JsonComparatorExceptions {
		return jsonCompare.compare(inputData);
	}


}
