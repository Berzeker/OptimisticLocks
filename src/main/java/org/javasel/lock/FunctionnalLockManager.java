package org.javasel.lock;

import org.javasel.models.FunctionnalLock;

public interface FunctionnalLockManager {
	FunctionnalLock createLock(String name);
	boolean destroyLock(FunctionnalLock functionnalLock);
	FunctionnalLock getFunctionnalLock(String name);
	FunctionnalLock activateLock(FunctionnalLock lock);
	FunctionnalLock desactivateLock(FunctionnalLock lock);
	
}
