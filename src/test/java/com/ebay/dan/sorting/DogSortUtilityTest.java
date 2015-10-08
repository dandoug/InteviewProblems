package com.ebay.dan.sorting;

import static com.ebay.dan.sorting.DogSortUtility.topologicalSort;
import static org.junit.Assert.assertArrayEquals;
import static org.westminsterkenel.OutputUtilities.writeDotFile;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.westminsterkenel.IDog;

import com.ebay.interview.questions.util.DogGenerator;

public class DogSortUtilityTest {

	@Test
	public void test() {
		Dog i = new Dog("i");
		Dog j = new Dog("j");
		Dog k = new Dog("k");
		i.bites(j);
		j.bites(k);

		List<Dog> dogs = Arrays.asList(new Dog[] { k, j, i });
		List<IDog> sorted = topologicalSort(dogs);

		System.out.println("Sorted:" + sorted);
		assertArrayEquals(new Dog[] { i, j, k }, sorted.toArray(new Dog[sorted.size()]));
	}
	

	@Test(expected=IllegalStateException.class)
	public void testWithCycle() {
		Dog i = new Dog("i");
		Dog j = new Dog("j");
		Dog k = new Dog("k");
		i.bites(j);
		j.bites(k);
		k.bites(i);
		
		List<Dog> dogs = Arrays.asList(new Dog[]{k,j,i});
		topologicalSort(dogs);
	}	
	
	@Test
	public void testWithRandomDataAndVisualization() {
		List<IDog> dogs = DogGenerator.buildRandomizedInput(Dog.class);
		
		//Topological sort
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<IDog> sortedDogs = topologicalSort((List)dogs);
		System.out.println("testWithRandomDataAndVisualization sorted:"+ dogs);

		//Test to ensure none of the dogs will bite the ones that come before it
		for (int i = 1; i < sortedDogs.size(); i++) {
			IDog dog = sortedDogs.get(i);
			for (int j = 0; j < i; j++) {
				IDog dogBefore = sortedDogs.get(j);
				if(dog.dogsBittenByThisDog().contains(dogBefore))
				{
					Assert.fail(dog+ " can bite dog before it which is "+ dogBefore);
				}
			}
		}
		
		writeDotFile(dogs, sortedDogs, "dan-arrangedDogsGraphComplex.dot");
	}	

	
}
