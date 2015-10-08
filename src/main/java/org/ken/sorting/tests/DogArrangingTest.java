package org.ken.sorting.tests;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class DogArrangingTest {

	
	@Test
	public void test() {
		Dog i = new Dog("i");
		Dog j = new Dog("j");
		Dog k = new Dog("k");
		i.bites(j);
		j.bittenBy(i);
		j.bites(k);
		k.bittenBy(j);
		
		List<Dog> dogs = Arrays.asList(new Dog[]{k,j,i});
		List<Dog> sorted = topologicalSort(dogs);
		
		System.out.println("Sorted:"+ sorted);
		Assert.assertArrayEquals(new Dog[]{i,j,k}, (Dog[]) sorted.toArray(new Dog[sorted.size()])) ;
	}

	@Test(expected=IllegalStateException.class)
	public void testWithCycle() {
		Dog i = new Dog("i");
		Dog j = new Dog("j");
		Dog k = new Dog("k");
		i.bites(j);
		j.bittenBy(i);
		j.bites(k);
		k.bittenBy(j);
		k.bites(i);
		i.bittenBy(k);
		
		List<Dog> dogs = Arrays.asList(new Dog[]{k,j,i});
		topologicalSort(dogs);
	}

	//Test with ramdom data
	//Generate dogs
	//For each dog, randomly find up to half of other dogs to dominate (those that it could bite). 
	//The logic needs to ensure the dominance relationships do not end up in a loop
	//This test method does not 
	@Test
	public void testWithRandomDataAndVisualization() {
		int size = 30;
		int maxDogsToDominatePerDog = Math.min(size/2, 10);//Having more edges will really clutter the graph for inspection
		Random r = new Random();
		List<Dog> dogs = new ArrayList<Dog>();
		for (int i = 0; i < size; i++) {
			dogs.add(new Dog("dog"+i));
		}

		Collections.shuffle(dogs);
		for (Dog dog : dogs) {
			for (int i = 0; i < maxDogsToDominatePerDog; i++) {
				Dog anotherDog = dogs.get(r.nextInt(size));
				if(anotherDog != dog && !dog.canBeBittenByAnotherDogOrItsFollowers(anotherDog))
				{
					//System.out.println(dog + " bites " + anotherDog);
					dog.bites(anotherDog);
					anotherDog.bittenBy(dog);
				}	
			}
		}
		
		//Topological sort
		List<Dog> sortedDogs = topologicalSort(dogs);
		System.out.println("testWithRandomDataAndVisualization sorted:"+ dogs);

		//Test to ensure none of the dogs will bite the ones that come before it
		for (int i = 1; i < sortedDogs.size(); i++) {
			Dog dog = sortedDogs.get(i);
			for (int j = 0; j < i; j++) {
				Dog dogBefore = sortedDogs.get(j);
				if(dog.canBite(dogBefore))
				{
					Assert.fail(dog+ " can bite dog before it which is "+ dogBefore);
				}
			}
		}
		
		writeDotFile(dogs, sortedDogs, "arrangedDogsGraphComplex.dot");
	}

	List<Dog> topologicalSort(List<Dog> inputDogs)
	{
		List<Dog> clonedDogs = cloneDogs(inputDogs);//Clone the dogs to preserve input and output data
		List<Dog> sorted = new ArrayList<Dog>();
		Queue<Dog> dominantDogs = new PriorityQueue<Dog>();//Dogs with no other dogs that could bite them
		for (Dog dog : clonedDogs) {
			if(dog.dogsThatWillBiteThisDog().isEmpty())
			{
				dominantDogs.add(dog);
			}	
		}
		
		while(!dominantDogs.isEmpty())
		{
			Dog dog = dominantDogs.poll();
			sorted.add(findDogByName(inputDogs, dog.getName()));
			for (Iterator<Dog> iterator = dog.dogsBittenByThisDog().iterator(); iterator.hasNext();) {
				Dog weakerDog = iterator.next();
				iterator.remove();
				weakerDog.dogsThatWillBiteThisDog().remove(dog);
				if(weakerDog.dogsThatWillBiteThisDog().isEmpty())
				{
					dominantDogs.offer(weakerDog);
				}	
			}
		}
		
		for (Dog dog : clonedDogs) {
			if(!dog.dogsBittenByThisDog().isEmpty() || !dog.dogsThatWillBiteThisDog().isEmpty())
			{
				throw new IllegalStateException("The dog biting graph has cycle dog:" + dog + " bites:"
						+ dog.dogsBittenByThisDog() + " bitten by:" + dog.dogsBittenByThisDog());
			}	
		}
		
		return sorted;
	}
	
	Dog findDogByName(List<Dog> dogs, String name)
	{
		return dogs.stream().filter(dog -> dog.getName().equals(name)).findFirst().get();
	}
	
	void writeDotFile(List<Dog> dogs, List<Dog> sortedDogs, String fileName) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					fileName)));
			bw.write("digraph {");
			bw.write("edge[weight=1000];");
			
			for (int i = 0; i < sortedDogs.size(); i++) {
				bw.write(""+sortedDogs.get(i));
				if(i<sortedDogs.size()-1)
				{
					bw.write("->");
				}
			}
			bw.write("[color=red];\n");
			bw.write("edge[weight=1];\n");
			
			for (Dog dog : dogs) {
				for (Dog weakerDog : dog.dogsBittenByThisDog()) {
					bw.write(dog.toString()+ " -> " + weakerDog.toString() + "[label=bites];\n");
				}
			}
			
			bw.write("\n");
			bw.write("}");

			bw.flush();
			bw.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	List<Dog> cloneDogs(List<Dog> dogs)
	{
		List<Dog> clonedDogs = new ArrayList<Dog>();
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(dogs);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			clonedDogs = (List<Dog>) new ObjectInputStream(bais).readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return clonedDogs;
	}
	
}

class Dog implements Serializable, Comparable<Dog> {
	private String name;
	private Set<Dog> dogsItWillBite = new HashSet<Dog>();
	private Set<Dog> dogsThatWillBiteIt = new HashSet<Dog>();

	public Dog(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	void bites(Dog anotherDog)
	{
		dogsItWillBite.add(anotherDog);
	}
	
	void bittenBy(Dog anotherDog)
	{
		dogsThatWillBiteIt.add(anotherDog);
	}

    boolean canBite(Dog anotherDog)
    {
	   return dogsItWillBite.contains(anotherDog);
    }
	
	Set<Dog> dogsBittenByThisDog()
	{
		return dogsItWillBite;
	}
	
	Set<Dog> dogsThatWillBiteThisDog()
	{
		return dogsThatWillBiteIt;
	}
	
	boolean canBeBittenByAnotherDogOrItsFollowers(Dog anotherDog)
	{
		if(anotherDog.canBite(this)) 
		{
			return true;
		} 
		else
		{
			for(Dog weakerDog : anotherDog.dogsBittenByThisDog()) {
			  	if(this.canBeBittenByAnotherDogOrItsFollowers(weakerDog))
			  	{
			  		return true;
			  	}	
			}
		}	
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Dog o) {
		return name.compareTo(o.name);
	}

}
