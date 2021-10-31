package com.resttest.infra.repository;

import java.util.Objects;

import javax.persistence.EntityManager;

public abstract class JpaWrapper<FIRST, SECOND> {

	protected Class<FIRST> entityClass;
	protected Class<SECOND> pkClass;
	
	
	protected abstract EntityManager getEntityManager();
	
	
	public FIRST findById(Object id) {
        return getEntityManager().find(entityClass, id);
    }
	
	public void create(FIRST entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }
	
	public void saveAndFlush(FIRST entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }
	
	public boolean existsById(FIRST entity) {
    	try {
	    	FIRST opt = getEntityManager().find(entityClass, entity);
	    	if (! Objects.isNull(opt)) {return true;} else {return false;}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }

    public void remove(FIRST entity) {
        getEntityManager().remove(entity);
    }

}
