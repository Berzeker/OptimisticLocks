#OptimisticLocks
OptimisticLocks is an API providing software locks based on the principle of optimistic's lock using hibernate/jpa, Spring and H2 in memory database.<br/>
Once a lock is activated, the code between the two methods **activateLock** and **deactivateLock** can be executed by only one thread.<br/>
If the lock is already activated, any thread trying to activate again the same lock will throw the exception **AlreadyLockedException**

##Creating a lock
A lock can be created by given him a name. When a lock is creating, it's by default not activated.

    FunctionnalLock lock = functionnalLockManager.createLock("MYLOCK");

##Activate a lock     
A lock can be activated by given the reference of the lock, or by the name of the lock. Be aware, always save the lock returned by this methods for next operation like deactivating or destroying.<br/>
Activating by reference :
    
    lock = functionnalLockManager.activateLock(lock);
    
Activating by name :   

    lock = functionnalLockManager.activateLockByName("MYLOCK");
    
##Deactivate a lock
When a treatment is ended, the lock have to be deactivated to let another thread execute the treatment again.
    
    lock = functionnalLockManager.deactivateLock(lock");
    
##Destroy a lock
A lock can be removed from the in-memory database

    functionnalLockManager.destroyLock(lock);

##Race condition, exception => lock bad status ...
You can change the status of the lock directly in database, if an exception occur leaving the lock on a bad status.
By connecting in the embedded webserver console

> http://localhost:8807
    
update the status of the lock, by replacing <name of lock> with the name of the specified lock.

	update FUNCTIONNAL_LOCK set ACTIF = 'FALSE' where name = '<name of lock>'
	commit;


# Full exemple :
    ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:**/optimisticlocks-Context*.xml");
    FunctionnalLockManager functionnalLockManager = (FunctionnalLockManager) context.getBean("functionnalLockManager");
    FunctionnalLock lock = functionnalLockManager.createLock("LOCK");
    try {
        lock = functionnalLockManager.activateLock(lock);
        //traitement executed by one thread
        lock = functionnalLockManager.deactivateLock(lock);
        functionnalLockManager.destroyLock(lock);
    } catch (AlreadyLockedException e) {
        //Traitement if concurrency
    }