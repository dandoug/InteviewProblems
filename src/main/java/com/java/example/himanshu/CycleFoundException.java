package com.java.example.himanshu;

/**
 * Created by hkulkarni on 9/30/15.
 */
public class CycleFoundException extends Exception {
	private static final long serialVersionUID = -3345823443692263995L;

	public CycleFoundException(String message){
        super(message);
    }
}
