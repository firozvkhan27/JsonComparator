package com.json.comparator.jparepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.json.comparator.dto.JsonDomain;

@Repository
public interface JpaCurdRepo  extends JpaRepository<JsonDomain, Integer> {
	 
	
}
