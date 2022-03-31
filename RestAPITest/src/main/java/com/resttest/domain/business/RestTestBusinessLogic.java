package com.resttest.domain.business;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.resttest.domain.entity.REST_TEST;
import com.resttest.domain.entity.REST_TEST_PK;
import com.resttest.infra.repository.RestTestResource;

@Dependent
public class RestTestBusinessLogic {
	
	@PostConstruct
	public void construct() {
		
	}
	
	@Inject
	RestTestResource resource;
	
	
	public String getName(String id) throws Exception{
		
		REST_TEST_PK pkEntity = new REST_TEST_PK();
		pkEntity.setId(id);
		REST_TEST entity = resource.findById(pkEntity);
		
		if (entity.getName().equals("")) {
			return "no records";
		} else {
			return entity.getName();
		}
	}
	
	@Transactional
	public String getTimeOutException(String id) throws Exception{
		
		
		//120秒のスリープ
		Thread.sleep(100000);;
		
		return id;
	}
	

}
