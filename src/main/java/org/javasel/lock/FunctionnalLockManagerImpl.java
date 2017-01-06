package org.javasel.lock;

import javax.persistence.NoResultException;

import org.javasel.dao.FunctionnalLockDao;
import org.javasel.models.FunctionnalLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by berzeker on 07/08/16.
 */
class FunctionnalLockManagerImpl implements FunctionnalLockManager {

    @Autowired
    private FunctionnalLockDao functionnalLockDao;
    
    private FunctionnalLockManagerImpl() {}
    
    public static FunctionnalLockManager getInstance() {
		return new FunctionnalLockManagerImpl();
	}

    public FunctionnalLock createLock(String name) {

        FunctionnalLock functionnalLock = getFunctionnalLock(name);
        if (functionnalLock == null) {
            functionnalLock = new FunctionnalLock(name);
            functionnalLock = functionnalLockDao.save(functionnalLock);
        }

        return functionnalLock;
    }

    public FunctionnalLock getFunctionnalLock(String name) {

        FunctionnalLock functionnalLock;
        try {
            functionnalLock = functionnalLockDao.findByName(name);
        }
        catch (NoResultException ex) {
            functionnalLock = null;
        }
        return functionnalLock;
    }

    public boolean destroyLock(FunctionnalLock functionnalLock) {
        functionnalLockDao.delete(functionnalLock);
        FunctionnalLock functionnalLockTmp = functionnalLockDao.findByName(functionnalLock.getName());
        if (functionnalLock != null)
            return false;
        return true;
    }

    @Transactional
    public FunctionnalLock activateLock(FunctionnalLock lock) {
    	FunctionnalLock functionnalLock = null;
        lock.setActif(Boolean.TRUE);
        try {
        	functionnalLock = functionnalLockDao.save(lock);
        } catch (ObjectOptimisticLockingFailureException ex) {
        	System.out.println("conflit_activate" + Thread.currentThread().getName());
        	functionnalLock = null;
        }
        return functionnalLock;
    }

    @Transactional
    public FunctionnalLock desactivateLock(FunctionnalLock lock) {
    	FunctionnalLock functionnalLock = null;
        lock.setActif(Boolean.FALSE);
        try {
        	functionnalLock = functionnalLockDao.save(lock);
        	System.out.println("Destroyed" + lock.getName());
        } catch (ObjectOptimisticLockingFailureException ex) {
        	System.out.println("conflit_desactivate" + Thread.currentThread().getName());
        	functionnalLock = null;
        }
        return functionnalLock;
    }

}
