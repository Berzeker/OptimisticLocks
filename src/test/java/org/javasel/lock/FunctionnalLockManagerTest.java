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
    public void createLock() {
        FunctionnalLock functionnalLock = functionnalLockManager.createLock("TEST22");
        Assert.assertNotNull(functionnalLock);
    }

    @Test
    public void activateLock() throws AlreadyLockedException, NoLockFoundException {
        FunctionnalLock lockTest = functionnalLockManager.createLock("TEST");
        functionnalLockManager.activateLock(lockTest);
        Assert.assertEquals(true, lockTest.getActif());
    }

    @Test
    public void desactivateLock() throws AlreadyLockedException, NoLockFoundException {
        FunctionnalLock lockTest = functionnalLockManager.createLock("TEST");
        functionnalLockManager.activateLock(lockTest);
        Assert.assertEquals(true, lockTest.getActif());
        functionnalLockManager.deactivateLock(lockTest);
        Assert.assertEquals(false, lockTest.getActif());
    }


    @Test
    public void testFunctionnalLock() {

        functionnalLockManager.createLock("TEST10");

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
    			
    			lock = functionnalLockManager.activateLockByName("TEST10");
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
