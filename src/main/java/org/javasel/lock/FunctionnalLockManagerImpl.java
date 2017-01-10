package org.javasel.lock;

import javax.persistence.NoResultException;

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
    
    private static final FunctionnalLockManager FUNCTIONNAL_LOCK_MANAGER = new FunctionnalLockManagerImpl();
    
    private FunctionnalLockManagerImpl() {}
    
    /**
     * This method return an instance of the functional lock manager
     * @return FunctionnalLockManagerImpl an instance of functional lock manager
     */
    public static FunctionnalLockManager getInstance() {
		return FUNCTIONNAL_LOCK_MANAGER;
	}

    /**
     * {@inheritDoc}
     */
    @Transactional
    public FunctionnalLock createLock(String name) {
    	
        FunctionnalLock functionnalLock = null;
		try {
			functionnalLock = getFunctionnalLock(name);
		} catch (NoLockFoundException e) {
			functionnalLock = new FunctionnalLock(name);
            functionnalLock = functionnalLockDao.save(functionnalLock);
		}
        return functionnalLock;
    }

    /**
     * {@inheritDoc}
     * @throws NoLockFoundException 
     */
    public FunctionnalLock getFunctionnalLock(String name) throws NoLockFoundException {

        FunctionnalLock functionnalLock = functionnalLockDao.findByName(name);

        if (functionnalLock == null) {
            throw new NoLockFoundException(name);
        }
        return functionnalLock;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public boolean destroyLock(FunctionnalLock functionnalLock) throws NoLockFoundException {
        functionnalLockDao.delete(functionnalLock);
        FunctionnalLock functionnalLockTmp = getFunctionnalLock(functionnalLock.getName());
        if (functionnalLockTmp != null)
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public FunctionnalLock activateLock(FunctionnalLock lock) throws AlreadyLockedException, NoLockFoundException {
    		
    	FunctionnalLock functionnalLock = getFunctionnalLock(lock.getName());
    	
    	
    	if (functionnalLock != null && !functionnalLock.getActif()) {
    		functionnalLock.setActif(Boolean.TRUE);
            try {
            	functionnalLock = functionnalLockDao.save(functionnalLock);
            } catch (ObjectOptimisticLockingFailureException ex) {
            	throw new AlreadyLockedException(functionnalLock.getName());
            }
    	}
    	if (functionnalLock != null && functionnalLock.getActif()) {
    		throw new AlreadyLockedException(functionnalLock.getName());
    	}
        return functionnalLock;
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional
    public FunctionnalLock activateLockByName(String name) throws AlreadyLockedException, NoLockFoundException {
    		
    	FunctionnalLock functionnalLock = getFunctionnalLock(name);
    	if (functionnalLock != null && !functionnalLock.getActif()) {
    		functionnalLock.setActif(Boolean.TRUE);
            try {
            	functionnalLock = functionnalLockDao.save(functionnalLock);
            } catch (ObjectOptimisticLockingFailureException ex) {
            	throw new AlreadyLockedException(functionnalLock.getName());
            }
    	}
    	if (functionnalLock != null && functionnalLock.getActif()) {
    		throw new AlreadyLockedException(functionnalLock.getName());
    	}
        return functionnalLock;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public FunctionnalLock deactivateLock(FunctionnalLock lock) {
    	FunctionnalLock functionnalLock = null;
        lock.setActif(Boolean.FALSE);
        try {
        	functionnalLock = functionnalLockDao.save(lock);
        } catch (ObjectOptimisticLockingFailureException ex) {
        	System.out.println("lock " + lock.getName() + " already deactivated");
        	functionnalLock = null;
        }
        return functionnalLock;
    }

}
