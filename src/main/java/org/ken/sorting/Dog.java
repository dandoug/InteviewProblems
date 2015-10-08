package org.ken.sorting;

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
		dogsItWillBite.add(anotherDog);
	}
	
	@Override
	public Set<IDog> dogsBittenByThisDog()
	{
		return dogsItWillBite;
	}
		
	@Override
	public String toString() {
		return name;
	}

}