package com.json.comparator.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JSON_DB")
@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
public class JsonDomain {
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "json_data")
	private String jsonData;

	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	
	
}
