package com.ebay.interview.questions.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.westminsterkenel.IDog;

public class DogGenerator {
	

	public  static <T extends IDog> List<IDog> buildRandomizedInput(Class<T> claz) {
		int size = 30;
		int maxDogsToDominatePerDog = Math.min(size/2, 10);//Having more edges will really clutter the graph for inspection
		Random r = new Random();
		
		List<IDog> dogs;
		try {
			// Get constructor
			Constructor<T> cons = claz.getConstructor(String.class);
			
			dogs = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				dogs.add(cons.newInstance("dog"+i));
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Bad class "+claz.getName(),e);
		}

		Collections.shuffle(dogs);
		for (IDog dog : dogs) {
			for (int i = 0; i < maxDogsToDominatePerDog; i++) {
				IDog anotherDog = dogs.get(r.nextInt(size));
				if(anotherDog != dog && !canBeBittenByAnotherDogOrItsFollowers(dog,anotherDog))
				{
					//System.out.println(dog + " bites " + anotherDog);
					dog.bites(anotherDog);
				}	
			}
		}
		return dogs;
	}

	private static boolean canBeBittenByAnotherDogOrItsFollowers(IDog thisDog, IDog anotherDog)
	{
		if(anotherDog.dogsBittenByThisDog().contains(thisDog)) 
		{
			return true;
		} 
		else
		{
			for(IDog weakerDog : anotherDog.dogsBittenByThisDog()) {
			  	if(canBeBittenByAnotherDogOrItsFollowers(thisDog, weakerDog))
			  	{
			  		return true;
			  	}	
			}
		}	
		return false;
	}		

}
