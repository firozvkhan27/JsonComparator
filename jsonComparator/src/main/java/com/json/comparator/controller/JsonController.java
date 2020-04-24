package com.json.comparator.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.json.comparator.model.InputData;
import com.json.comparator.service.JsonCompareService;

@RestController
@RequestMapping(value="/api/v1/jsons")
public class JsonController {
	@Autowired
	private JsonCompareService jsonCompare ;
	@PostMapping()
	public boolean save(@RequestBody InputData data) {
		jsonCompare.saveData(data);
		return true;
	}
		
	@DeleteMapping()
	public boolean deleteData(@RequestParam("id") int id) throws Exception {
		jsonCompare.deleteDateById(id);
		return true;
	}
	
}
