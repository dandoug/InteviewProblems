package org.ken.sorting;

import static org.ken.sorting.DogSortUtility.topologicalSort;
import static org.westminsterkenel.OutputUtilities.writeDotFile;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.westminsterkenel.IDog;

import com.ebay.interview.questions.util.DogGenerator;

public class DogArrangingTest {

	
	@Test
	public void test() {
		Dog i = new Dog("i");
		Dog j = new Dog("j");
		Dog k = new Dog("k");
		i.bites(j);
		j.bites(k);
		
		List<Dog> dogs = Arrays.asList(new Dog[]{k,j,i});
		List<IDog> sorted = topologicalSort(dogs);
		
		System.out.println("Sorted:"+ sorted);
		Assert.assertArrayEquals(new Dog[]{i,j,k}, sorted.toArray(new Dog[sorted.size()])) ;
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

	//Test with ramdom data
	//Generate dogs
	//For each dog, randomly find up to half of other dogs to dominate (those that it could bite). 
	//The logic needs to ensure the dominance relationships do not end up in a loop
	//This test method does not 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testWithRandomDataAndVisualization() {
		List<IDog> dogs = DogGenerator.buildRandomizedInput(Dog.class);
		
		//Topological sort
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
		
		writeDotFile(dogs, sortedDogs, "ken-arrangedDogsGraphComplex.dot");
	}

	
}
