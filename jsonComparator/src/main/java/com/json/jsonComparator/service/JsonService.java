package com.json.jsonComparator.service;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
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

	/*public HashMap<String, String> compare(InputData data) throws JsonParseException, JsonMappingException, IOException {
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
		
		String strin = "";
		hm.put("Right Side different entries ", difference1.entriesOnlyOnRight().toString());
		hm.put("Both Side different values ", difference1.entriesDiffering().toString());
		return hm;
	}

	public void deleteDateById(int id) {
		jp.deleteById(id);
	}*/
	public Map<String, Object> compare(InputData data) throws JsonParseException, JsonMappingException, IOException {
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
		Map<String, Object> entriesOnlyOnRight = difference1.entriesOnlyOnRight();
		Map<String, Object> entriesOnlyOnLeft = difference1.entriesOnlyOnLeft();
		Map<String, ValueDifference<Object>> entriesDiffering = difference1.entriesDiffering();
		Map<String, Object> result = new HashMap<String, Object>();
		
			JSONObject parent  = new JSONObject();
			entriesOnlyOnRight.forEach((k,v)->{
				String[] keySplits = k.substring(1).split("/");
				 getJson(keySplits,v,parent);
				 if(!k.substring(1).contains("/")){
					 parent.put(k.substring(1), v);
				 }
				
			});
			
			JSONObject left  = new JSONObject();
			entriesOnlyOnLeft.forEach((k,v)->{
				String[] keySplits = k.substring(1).split("/");
				 getJson(keySplits,v,left);
				 if(!k.substring(1).contains("/")){
					 left.put(k.substring(1), v);
				 }
				
			});
			
			JSONObject both  = new JSONObject();
			entriesDiffering.forEach((k,v)->{
				String[] keySplits = k.substring(1).split("/");
				 getJson(keySplits,v,both);
				 if(!k.substring(1).contains("/")){
					 parent.put(k.substring(1), v);
				 }
				
			});
			result.put("Left", left.toMap());
			result.put("Both", both.toMap());
			result.put("Right", parent.toMap());
			return result;	
			}

	public void deleteDateById(int id) {
		jp.deleteById(id);
	}
	
	
	private static JSONObject getJson(String[] str, Object v,JSONObject parent) {
		JSONObject child = null;
		for (int i = str.length-1; i >= 0; i--) {
			if(i==str.length-1){
				child = new JSONObject();
				child.put(str[i], v);
			}else if(i!=0){
				while(i!=0){
				Map<String, Object> childmap = child.toMap();
				try{
					Object createJson = createJson(parent, str[i]);
					if(null!= createJson){
						Map<String, Object> map = ((JSONObject) createJson).toMap();
						map.putAll(child.toMap());
						child = new JSONObject();
						child.put(str[i], map);
					}else{
						child = new JSONObject();
						child.put(str[i], childmap);
					}
					System.out.println();
				}catch(Exception e){
					
				}
				i=i-1;
				}
				Object createJson = createJson(parent, str[i]);
				if(createJson instanceof JSONObject){
					Map<String, Object> map = ((JSONObject) createJson).toMap();
					
					map.putAll(child.toMap());
					parent.put(str[i], map);
					System.out.println();

				}else{
					parent.put(str[i], child);
				}
				
			}else if(i ==0){
				try {
					try {
						JSONObject object = (JSONObject) parent.get(str[i]);	
						Map<String, Object> map = object.toMap();
						map.putAll(child.toMap());
						parent.put(str[i], map);
						System.out.println();
					} catch (Exception e) {
						Object createJson = createJson(parent, str[i]);
						if(null != createJson){
						if(createJson instanceof JSONObject){
							Map<String, Object> existing = ((JSONObject) createJson).toMap();
							existing.putAll(child.toMap());
							parent.toMap().putAll(existing);
							
						}
						}else {
							parent.put(str[i], child);
						}	// TODO: handle exception
					}
					
					
				} catch (Exception e) {
					parent.put(str[i], child);
				}
			}
		}
		return parent;
	}

	
	public static Object  createJson(Object object, String str) {
		Object j =null;
		if(object instanceof JSONObject){
			JSONObject jsonObj =(JSONObject) object;
	    for (Object key : jsonObj.keySet()) {
	        //based on you key types
	        String keyStr = (String)key;
	        Object keyvalue = jsonObj.get(keyStr);
	        
	        //Print key and value
	        //for nested objects iteration if required
	        if (keyvalue instanceof JSONObject){
	        		if(str.trim().equalsIgnoreCase(keyStr.trim())){
		        	j=(JSONObject) keyvalue;
		        	return (JSONObject) keyvalue;
	        	 }
	        	j=  createJson((JSONObject)keyvalue ,str);
	        }
	        if (keyvalue instanceof JSONArray){
        		if(str.trim().equalsIgnoreCase(keyStr.trim())){
	        	j=(JSONArray) keyvalue;
	        	return (JSONArray) keyvalue;
        	 }else{
        		 
        	 }
        	j =    createJson(keyvalue ,str);
        }}
	        
	    }if(object instanceof JSONArray){
	    	JSONArray jsonArray = (JSONArray)object;
	    	System.out.println();
	    }
		return j;
	}
	
	public static JSONArray  getJsonArray(JSONObject jsonObj, String str) {
		JSONArray j =null;
	    for (Object key : jsonObj.keySet()) {
	        //based on you key types
	        String keyStr = (String)key;
	        Object keyvalue = jsonObj.get(keyStr);
	        
	        //Print key and value
	        //for nested objects iteration if required
	        if (keyvalue instanceof JSONArray){
	        		if(str.trim().equalsIgnoreCase(keyStr.trim())){
		        	j=(JSONArray) keyvalue;
		        	return (JSONArray) keyvalue;
	        	 }
	        	j=   getJsonArray((JSONObject)keyvalue ,str);
	        }
	        
	    }
		return j;
	}
	
	}
