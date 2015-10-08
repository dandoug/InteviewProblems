package com.ebay.dan.sorting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.westminsterkenel.IDog;

public class Dog implements IDog {
	private String name;
	private Set<IDog> dogsItWillBite = new HashSet<>();

	public Dog(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public void bites(IDog anotherDog)
	{
		if (!(anotherDog instanceof Dog)) {
			throw new IllegalArgumentException("only take Dog");
		}
		dogsItWillBite.add((Dog)anotherDog);
	}
	
	@Override
	public Set<IDog> dogsBittenByThisDog()
	{
		return Collections.unmodifiableSet(dogsItWillBite);
	}
		
	@Override
	public String toString() {
		return name;
	}

}