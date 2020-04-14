package com.json.jsonComparator.jparepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.json.jsonComparator.dto.JsonData;

@Repository
public interface JpaCurdRepo  extends JpaRepository<JsonData, Integer> {
	 
	
}
