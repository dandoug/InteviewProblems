package org.westminsterkenel;

import java.util.Set;

public interface IDog {
	
	/**
	 * Record the fact that this dog bits another one
	 * @param anotherDog
	 */
	void bites(IDog anotherDog);
	
	/**
	 * Return list of dogs that this dog bites
	 */
	Set<IDog> dogsBittenByThisDog();

	/**
	 * String representation for output and use in DOT graphs
	 */
	String getName();
	
}
