package org.javasel.lock;

public class NoLockFoundException extends Exception {

	private static final long serialVersionUID = 5744245133349835596L;

	public NoLockFoundException(String name) {
		super("No lock with name " + name + " exist");
	}
}
