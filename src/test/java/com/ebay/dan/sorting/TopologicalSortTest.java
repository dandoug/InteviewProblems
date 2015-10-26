package com.ebay.dan.sorting;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.westminsterkenel.DogSorter;
import org.westminsterkenel.IDog;

public class TopologicalSortTest {

	DogSorter sorter;
	List<IDog> dogs;
	IDog one, two, three, four, five;
	
	@Before
	public void init() {
        sorter = DogSortUtility.getInstance();

        // Create dogs
		one   = new Dog("1");
		two   = new Dog("2");
		three = new Dog("3");
		four  = new Dog("4");
		five  = new Dog("5");
		dogs = Arrays.asList(new IDog[] { one, two, three, four, five });
		
		// setup rules
		one.bites(two);
		one.bites(three);
		three.bites(two);
		//four.bites(one);
		five.bites(four);
		five.bites(three);
		two.bites(four);

	}
	
	@Test
	public void test() {
		List<IDog> sorted = sorter.topologicalSort(dogs);        

        System.out.print(sorted);
	}

	@Test
	public void testCycle() {
		
		// add an edge that creates a cycle
		four.bites(one);
		
        try {
        	sorter.topologicalSort(dogs);
			fail("should have thrown exception");
		} catch (IllegalStateException e) {
			// Expected
		}
	}

}
