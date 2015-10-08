package org.ken.sorting.tests;

import java.util.HashSet;
import java.util.Set;

class Dog implements Comparable<Dog> {
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