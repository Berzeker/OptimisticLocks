package org.javasel.lock;

import org.javasel.models.FunctionnalLock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void testFunctionnalLock() throws InterruptedException {
        System.out.println("DEBUT TEST");
        
        
        functionnalLockManager.createLock("TEST10");

        LockTesterThread lockThread = new LockTesterThread();
        Thread[] threads = new Thread[10];

        for (int i=0; i<10; i++) {
            threads[i] = new Thread(lockThread);
        }

        for (int i=0; i<10; i++) {
            threads[i].start();
        }
        
     // on attend que chaque thread ait fini son exécution
        for (int i =  0 ; i < threads.length ; i++) {
            // jette InterruptedException
            threads[i].join();
        }
        
        for (int i=0; i<10; i++) {
            threads[i] = new Thread(lockThread);
        }
        
        for (int i=0; i<10; i++) {
            threads[i].start();
        }

        // on attend que chaque thread ait fini son exécution
        for (int i =  0 ; i < threads.length ; i++) {
            // jette InterruptedException
            threads[i].join();
        }
        
        

        System.out.println("FIN TEST");
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
