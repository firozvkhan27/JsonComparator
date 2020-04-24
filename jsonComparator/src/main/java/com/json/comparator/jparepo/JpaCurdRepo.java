package com.json.comparator.jparepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.json.comparator.dto.JsonData;

@Repository
public interface JpaCurdRepo  extends JpaRepository<JsonData, Integer> {
	 
	
}
