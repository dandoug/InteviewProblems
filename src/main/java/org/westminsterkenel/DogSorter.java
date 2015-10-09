package org.westminsterkenel;

import java.util.List;

public interface DogSorter {

	/**
	 * Arrange n dogs facing forward in a straight line.  
	 * If i bites j, then you do not want to put i behind j, since i is capable of biting j.
	 * @param dogs
	 * @return
	 */
	List<IDog> topologicalSort(List<IDog> dogs);
	
	String getAlgorithm();
}
