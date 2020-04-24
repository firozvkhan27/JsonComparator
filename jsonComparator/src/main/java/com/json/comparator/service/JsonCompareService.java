package com.json.comparator.service;



import java.io.IOException;
import java.util.HashMap;
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
import com.json.comparator.dto.JsonData;
import com.json.comparator.exceptions.JsonComparatorExceptions;
import com.json.comparator.exceptions.JsonNotFound;
import com.json.comparator.jparepo.JpaCurdRepo;
import com.json.comparator.model.InputData;

@Service
public class JsonCompareService {

	@Autowired
	private JpaCurdRepo jpaCurdRepo;
	public void saveData(InputData in) throws JsonComparatorExceptions {
		try{
		JsonData js = new JsonData();
		js.setJsonData(in.getInputJson().toString());
		jpaCurdRepo.save(js);
		}catch(Exception e){
			throw new JsonComparatorExceptions("Unable to save");
		}
	}

	public Map<String, Object> compare(InputData data) throws  IOException, JsonComparatorExceptions {
		try{
		Optional<JsonData> findById = jpaCurdRepo.findById(data.getBaseJsonID());
		JsonData jsonData = findById.isPresent()? findById.get(): null;
		String jsonData2 = null;
		if(null !=  jsonData)
		 jsonData2 = jsonData.getJsonData();
		
		JSONObject js1 = new JSONObject(jsonData2.replace("=", ":"));
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
		Map<String, Object> result = new HashMap<>();
		
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
		}catch (Exception e){
			throw new JsonComparatorExceptions("something went wrong");
		}
			}

	public void deleteDateById(int id) throws JsonNotFound {
		try{
		jpaCurdRepo.deleteById(id);
		}catch(Exception e){
			throw new JsonNotFound("not found");
		}
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
				}catch(Exception e){
					
				}
				i=i-1;
				}
				Object createJson = createJson(parent, str[i]);
				if(createJson instanceof JSONObject){
					Map<String, Object> map = ((JSONObject) createJson).toMap();
					
					map.putAll(child.toMap());
					parent.put(str[i], map);
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
							}
					}
					
					
				} catch (Exception e) {
					parent.put(str[i], child);
				}
			}
		}
		return parent;
	}

	
	public static Object  createJson(Object object, String str) {
		Object element =null;
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
		        	element=(JSONObject) keyvalue;
		        	return (JSONObject) keyvalue;
	        	 }
	        	element=  createJson((JSONObject)keyvalue ,str);
	        }
	        if (keyvalue instanceof JSONArray){
        		if(str.trim().equalsIgnoreCase(keyStr.trim())){
	        	element=(JSONArray) keyvalue;
	        	return (JSONArray) keyvalue;
        	 }else{
        		 
        	 }
        	element =    createJson(keyvalue ,str);
        }}
	        
	    }if(object instanceof JSONArray){
	    	JSONArray jsonArray = (JSONArray)object;
	    }
		return element;
	}
	
	public static JSONArray  getJsonArray(JSONObject jsonObj, String str) {
		JSONArray element =null;
	    for (Object key : jsonObj.keySet()) {
	        //based on you key types
	        String keyStr = (String)key;
	        Object keyvalue = jsonObj.get(keyStr);
	        
	        //Print key and value
	        //for nested objects iteration if required
	        if (keyvalue instanceof JSONArray){
	        		if(str.trim().equalsIgnoreCase(keyStr.trim())){
		        	return (JSONArray) keyvalue;
	        	 }
	        	element=   getJsonArray((JSONObject)keyvalue ,str);
	        }
	        
	    }
		return element;
	}
	
	}
