package com.java.example.himanshu;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TopologicalSortTest {

	TopologicalSort sorter;
	ArrayList<String> inputDogs;
	ArrayList<String> ins;
	
	@Before
	public void init() {
        sorter = new TopologicalSort();
        inputDogs = new ArrayList<>();
        inputDogs.add("1");
        inputDogs.add("2");
        inputDogs.add("3");
        inputDogs.add("4");
        inputDogs.add("5");

        ins= new ArrayList<>();
        ins.add("1 bites 2");
        ins.add("1 bites 3");
        ins.add("3 bites 2");
        //ins.add("4 bites 1");
        ins.add("5 bites 4");
        ins.add("5 bites 3");
        ins.add("2 bites 4");
		
	}
	
	@Test
	public void test() throws CycleFoundException {
        sorter.initGraph(inputDogs, ins);

        System.out.print(sorter.sortDogsInLine());
	}

	@Test
	public void testCycle() {
		
		// add an edge that creates a cycle
		ins.add("4 bites 1");
		
        sorter.initGraph(inputDogs, ins);

        try {
			System.out.print(sorter.sortDogsInLine());
			fail("should have thrown exception");
		} catch (CycleFoundException e) {
			// Expected
		}
	}

}
