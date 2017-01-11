package org.javasel.lock;

import org.javasel.models.FunctionnalLock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by berzeker on 16/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class FunctionnalLockManagerTest {
	
	private static int compteur = 0;

	@Autowired
    private FunctionnalLockManager functionnalLockManager;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Test
    public void createLock() throws InterruptedException {
        FunctionnalLock lock = functionnalLockManager.createLock("LOCK1");
        Assert.assertNotNull(lock);
    }
    
    @Test
    public void getFunctionnalLock() {
    	String lockName = "LOCK2";
    	FunctionnalLock lock = functionnalLockManager.createLock(lockName);
    	try {
			lock = functionnalLockManager.getFunctionnalLock(lockName);
			Assert.assertNotNull(lock);
		} catch (NoLockFoundException e) {
			Assert.fail(e.getMessage());
		}
    }

    @Test
    public void activateLock() throws AlreadyLockedException {
    	try {
	        FunctionnalLock lock = functionnalLockManager.createLock("LOCK3");
	        lock = functionnalLockManager.activateLock(lock);
	        Assert.assertEquals(true, lock.getActif());
    	} catch (NoLockFoundException e) {
    		Assert.fail(e.getMessage());
		}
    }
    
    @Test
    public void activateLockByName() throws AlreadyLockedException {
    	try {
    		String lockName = "LOCK4";
	        FunctionnalLock lock = functionnalLockManager.createLock(lockName);
	        lock = functionnalLockManager.activateLockByName(lockName);
	        Assert.assertEquals(true, lock.getActif());
    	} catch (NoLockFoundException e) {
    		Assert.fail(e.getMessage());
		}
    }
    
    @Test
    public void desactivateLock() throws AlreadyLockedException {
    	try {
	        FunctionnalLock lock = functionnalLockManager.createLock("LOCK5");
	        lock = functionnalLockManager.activateLock(lock);
	        Assert.assertEquals(true, lock.getActif());
	        lock = functionnalLockManager.deactivateLock(lock);
	        Assert.assertEquals(false, lock.getActif());
    	} catch (NoLockFoundException e) {
    		Assert.fail(e.getMessage());
		}
    }
    
    @Test
    public void destroyLock() {
    	FunctionnalLock lock = functionnalLockManager.createLock("LOCK6");
    	Assert.assertNotNull(lock);
    	boolean result = functionnalLockManager.destroyLock(lock);
    	Assert.assertEquals(true, result);
    }
    
    @Test
    public void testFunctionnalLock() {

        functionnalLockManager.createLock("LOCK");

        for (int i = 0; i < 10; i++)
            taskExecutor.execute(new LockTesterThread());

        for (;;) {
            int cout = taskExecutor.getActiveCount();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (cout == 0) {
                taskExecutor.shutdown();
                break;
            }
        }
    }

    class LockTesterThread implements Runnable {
        public void run() {
            try {
                testLockMethod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NoLockFoundException e) {
				System.out.println(e.getMessage());
			}
        }
        
        public void testLockMethod() throws InterruptedException, NoLockFoundException {
        	
        	FunctionnalLock lock;
    		try {
    			
    			lock = functionnalLockManager.activateLockByName("LOCK");
    			compteur++;
            	Assert.assertEquals(1, compteur);
            	compteur--;
                functionnalLockManager.deactivateLock(lock);	
                
    		} catch (AlreadyLockedException e) {
    			System.out.println(e.getMessage());
    		}
        }
    }

}
