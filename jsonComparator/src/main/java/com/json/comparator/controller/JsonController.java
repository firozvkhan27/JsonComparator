package com.json.comparator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.json.comparator.dto.JsonDomain;
import com.json.comparator.exceptions.JsonComparatorExceptions;
import com.json.comparator.exceptions.JsonNotFound;
import com.json.comparator.model.InputData;
import com.json.comparator.service.JsonCompareService;

@RestController
@RequestMapping(value="/api/v1/jsons")
public class JsonController {
	@Autowired
	private JsonCompareService jsonCompare ;
	@PostMapping()
	public boolean save(@RequestBody InputData data) throws JsonComparatorExceptions {
		jsonCompare.saveData(data);
		return true;
	}
		
	@DeleteMapping("/{basejJsonId}")
	public boolean deleteData(@PathVariable("basejJsonId") int basejJsonId) throws Exception {
		jsonCompare.deleteDateById(basejJsonId);
		return true;
	}
	@GetMapping("/{basejJsonId}")
	public JsonDomain getJsonData(@PathVariable("basejJsonId") int basejJsonId) throws JsonNotFound {
		return jsonCompare.getJsonInfo(basejJsonId);
		
	}
	
}
