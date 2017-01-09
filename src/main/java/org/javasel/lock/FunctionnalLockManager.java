package org.javasel.lock;

import org.javasel.models.FunctionnalLock;

public interface FunctionnalLockManager {
	
	/**
     * This method create an optimistic lock by the given name.
     * When the lock is created, by default he's not activated
     * 
     * @param name The name of the lock
     * @return FunctionnalLock the lock 
     */
	FunctionnalLock createLock(String name);
	
	/**
     * This method delete a lock by the given name.
	 * @throws NoLockFoundException No Lock founded
     */
	boolean destroyLock(FunctionnalLock functionnalLock) throws NoLockFoundException;
	
	/**
     * This method search an optimistic lock by the given name.
     * If no lock founded, the method return NULL.
     * 
     * @param name The name of the lock
     * @return FunctionnalLock the lock 
	 * @throws NoLockFoundException No Lock founded
     */
	FunctionnalLock getFunctionnalLock(String name) throws NoLockFoundException;
	
	/**
     * This method activate a lock
     * 
     * @param lock The lock for activating
     * @return The lock activated
	 * @throws AlreadyLockedException Lock already in use 
	 * @throws NoLockFoundException No Lock founded
     */
	FunctionnalLock activateLock(FunctionnalLock lock) throws AlreadyLockedException, NoLockFoundException;
	
	/**
    * This method activate a lock by it's name
    * 
    * @param name The name of the lock
    * @return The lock activated
    * @throws AlreadyLockedException Lock already in use
	 * @throws NoLockFoundException No Lock founded
    */
	FunctionnalLock activateLockByName(String name) throws AlreadyLockedException, NoLockFoundException;
	
	/**
     * This method deactivate a lock
     * 
     * @param lock The lock for deactivate
     * @return The lock deactivate
     */
	FunctionnalLock deactivateLock(FunctionnalLock lock);
	
}
