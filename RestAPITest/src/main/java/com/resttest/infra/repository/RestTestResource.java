package com.resttest.infra.repository;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.resttest.domain.entity.REST_TEST;
import com.resttest.domain.entity.REST_TEST_PK;

@Dependent
public class RestTestResource extends JpaWrapper<REST_TEST, REST_TEST_PK>{
	
	public RestTestResource() {
		entityClass = REST_TEST.class;
		pkClass = REST_TEST_PK.class;
	}
	
	@PersistenceContext(unitName = "REST_TEST_MASTER")
    public EntityManager em;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
