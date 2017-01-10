#OptimisticLocks
OptimisticLocks is an API implementing the optimistic's lock based on hibernate/jpa, Spring and H2 in memory database.

##exemple :
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