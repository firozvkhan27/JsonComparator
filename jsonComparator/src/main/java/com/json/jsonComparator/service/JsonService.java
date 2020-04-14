package com.json.jsonComparator.service;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.json.jsonComparator.dto.JsonData;
import com.json.jsonComparator.jparepo.JpaCurdRepo;
import com.json.jsonComparator.model.InputData;

@Service
public class JsonService {

	@Autowired
	JpaCurdRepo jp;

	public void saveData(InputData in) {
		JsonData js = new JsonData();
		js.setJsonData(in.getInputJson().toString());
		jp.save(js);
	}

	public HashMap<String, String> compare(InputData data) throws JsonParseException, JsonMappingException, IOException {
		Optional<JsonData> findById = jp.findById(data.getBaseJsonID());
		JsonData jsonData = findById.get();
		String jsonData2 = jsonData.getJsonData();
		
		JSONObject js1 = new JSONObject(jsonData2.toString().replace("=", ":"));
		JSONObject js2 = new JSONObject(data.getInputJson().toString().replace("=", ":"));
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {};
		Map<String, Object> leftMap = mapper.readValue(js1.toString(), type);
		Map<String, Object> rightMap = mapper.readValue(js2.toString(), type);
		Map<String, Object> leftFlatMap = FlatMapUtil.flatten(leftMap);
		Map<String, Object> rightFlatMap = FlatMapUtil.flatten(rightMap);
		MapDifference<String, Object> difference1 = Maps.difference(leftFlatMap, rightFlatMap);
		HashMap<String , String> hm = new HashMap<>();
		hm.put("Left Side different entries ", difference1.entriesOnlyOnLeft().toString());
		hm.put("Right Side different entries ", difference1.entriesOnlyOnRight().toString());
		hm.put("Both Side different values ", difference1.entriesDiffering().toString());
		return hm;
	}

	public void deleteDateById(int id) {
		jp.deleteById(id);
	}
	
}
