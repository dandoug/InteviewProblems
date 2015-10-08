package org.ken.sorting;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.westminsterkenel.IDog;

import com.rits.cloning.Cloner;

public class DogSortUtility {
	
	private static Cloner cloner=new Cloner();

	public static List<IDog> topologicalSort(List<Dog> inputDogs)
	{
		List<Dog> clonedDogs = cloneDogs(inputDogs);//Clone the dogs to preserve input and output data
		List<IDog> sorted = new ArrayList<>();
		Deque<IDog> dominantDogs = new LinkedList<>();//Dogs with no other dogs that could bite them
		for (Dog dog : clonedDogs) {
			if(dogsThatWillBiteThisDog(clonedDogs,dog).isEmpty())
			{
				dominantDogs.add(dog);
			}	
		}
		
		// dominantDogs are the dogs that have not been lined up yet that do not
		// haved any dogs left that will bite them, they are candidates to put
		// in the line since no dog that comes after them will bite them
		
		while(!dominantDogs.isEmpty())
		{
			IDog dog = dominantDogs.poll();
			// put the dog from the original list that matches the first dominant dog in the output
			sorted.add(findDogByName(inputDogs, dog.getName()));
			for (Iterator<IDog> iterator = dog.dogsBittenByThisDog().iterator(); iterator.hasNext();) {
				IDog weakerDog = iterator.next();
				// dog can't bite weaker dog because it's behind it in line
				iterator.remove();
				// if there's no one left to bite this dog, then it can get in line
				if(dogsThatWillBiteThisDog(clonedDogs,(Dog)weakerDog).isEmpty())
				{
					dominantDogs.offer(weakerDog);
				}	
			}
		}

		// Did all the dogs have their bites/bitenBy relationships removed?  If not, then 
		// there were some does we could not add and that indicates a cycle
		for (Dog dog : clonedDogs) {
			// Any dogs left sould be "neutral" dogs
			if(!dog.dogsBittenByThisDog().isEmpty() || !dogsThatWillBiteThisDog(clonedDogs,dog).isEmpty())
			{
				throw new IllegalStateException("The dog biting graph has cycle dog:" + dog + " bites:"
						+ dog.dogsBittenByThisDog() + " bitten by:" + dog.dogsBittenByThisDog());
			}	
		}
		
		return sorted;
	}

	private static List<Dog> cloneDogs(List<Dog> dogs)
	{
		return cloner.deepClone(dogs);
	}

	private static Dog findDogByName(List<Dog> dogs, String name)
	{
		return dogs.stream().filter(dog -> dog.getName().equals(name)).findFirst().get();
	}	

	private static Set<Dog> dogsThatWillBiteThisDog(List<Dog> dogs, Dog dog)
	{
		return dogs.stream().filter(d -> d.dogsBittenByThisDog().contains(dog)).collect(Collectors.toSet());
	}	
	
}
