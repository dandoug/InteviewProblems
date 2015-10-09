package com.ebay.interview.questions.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import static org.westminsterkenel.OutputUtilities.writeDotFile;

import java.util.Arrays;
import java.util.List;

import org.westminsterkenel.DogSorter;
import org.westminsterkenel.IDog;

public class GenericTests {

	public static void testPlain(DogFactory df, DogSorter sorter) {
		IDog i = df.create("i");
		IDog j = df.create("j");
		IDog k = df.create("k");
		i.bites(j);
		j.bites(k);

		List<IDog> dogs = Arrays.asList(new IDog[] { k, j, i });
		List<IDog> sorted = sorter.topologicalSort(dogs);

		System.out.println("Sorted:" + sorted);
		assertArrayEquals(new IDog[] { i, j, k }, sorted.toArray(new IDog[sorted.size()]));
	}
	

	public static void testWithCycle(DogFactory df, DogSorter sorter) {
		IDog i = df.create("i");
		IDog j = df.create("j");
		IDog k = df.create("k");
		i.bites(j);
		j.bites(k);
		k.bites(i);
		
		List<IDog> dogs = Arrays.asList(new IDog[]{k,j,i});
		sorter.topologicalSort(dogs);
	}	
	

	public static void testWithRandomDataAndVisualization(DogFactory df, DogSorter sorter) {
		List<IDog> dogs = DogGenerator.buildRandomizedInput(df.getDogClass());
		
		//Topological sort
		List<IDog> sortedDogs = sorter.topologicalSort(dogs);
		System.out.println(sorter.getAlgorithm()+
				"testWithRandomDataAndVisualization sorted:"+ dogs);

		//Test to ensure none of the dogs will bite the ones that come before it
		for (int i = 1; i < sortedDogs.size(); i++) {
			IDog dog = sortedDogs.get(i);
			for (int j = 0; j < i; j++) {
				IDog dogBefore = sortedDogs.get(j);
				if(dog.dogsBittenByThisDog().contains(dogBefore))
				{
					fail(dog+ " can bite dog before it which is "+ dogBefore);
				}
			}
		}
		
		writeDotFile(dogs, sortedDogs, sorter.getAlgorithm()+"-arrangedDogsGraphComplex.dot");
	}	

}
