package org.javasel.lock;

public class AlreadyLockedException extends Exception {

	private static final long serialVersionUID = -7716983892580658509L;

	public AlreadyLockedException(String lockName) {
		super("Lock " + lockName + " already in use");
	}
}
