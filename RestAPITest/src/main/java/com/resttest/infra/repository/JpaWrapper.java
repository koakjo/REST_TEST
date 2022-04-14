package com.resttest.infra.repository;

import java.util.Objects;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public abstract class JpaWrapper<FIRST, SECOND> {

	protected Class<FIRST> entityClass;
	protected Class<SECOND> pkClass;
	
	@Resource 
	public UserTransaction utx;
	
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
    
    public void nativeQueryExecute(String sql) {
    	getEntityManager().createNativeQuery(sql);
    }
   
    
    /*
     * UserTransaction-TEST---------------------------------------------------------------------------------------------
     */
    
    
    public FIRST findByIdUtx(Object id) {
	   	try {
	   		utx.begin();
	   		FIRST outcome = getEntityManager().find(entityClass, id);
			utx.commit();
			return outcome;
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException | NotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
   
    public FIRST findByIdUtxWithTimeout(Object id, int timeoutSec, int txTimeoutSec) {
	   	try {
	   		utx.setTransactionTimeout(txTimeoutSec);
	   		utx.begin();
	   		Thread.sleep(timeoutSec * 1000);
	   		FIRST outcome = getEntityManager().find(entityClass, id);
			utx.commit();
			return outcome;
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException | NotSupportedException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	   
    }
   
    public void onlyUtx(int timeoutSec , int txTimeoutSec) {
	   	try {
	   		utx.setTransactionTimeout(txTimeoutSec);
	   		utx.begin();
	   		Thread.sleep(timeoutSec * 1000);
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException | NotSupportedException | InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void  nativeQueryExecuteUtxWithSleep(String sql) {
	   	try {
	   		utx.begin();
	   		getEntityManager().createNativeQuery(sql);
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException | NotSupportedException e) {
			e.printStackTrace();
		}
    }
}
