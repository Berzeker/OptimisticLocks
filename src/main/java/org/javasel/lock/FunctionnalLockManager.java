package org.javasel.lock;

import org.javasel.dao.FunctionnalLockDao;
import org.javasel.models.FunctionnalLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

/**
 * Created by berzeker on 07/08/16.
 */
public class FunctionnalLockManager {

    @Autowired
    FunctionnalLockDao functionnalLockDao;

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
    FunctionnalLock activateLock(FunctionnalLock lock) {
        lock.setActif(Boolean.TRUE);
        return functionnalLockDao.save(lock);
    }

    FunctionnalLock desactivateLock(FunctionnalLock lock) {
        lock.setActif(Boolean.FALSE);
        return functionnalLockDao.save(lock);
    }

    void flush() {
        functionnalLockDao.flush();
    }

}
