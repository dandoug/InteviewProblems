package org.ken.sorting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.rits.cloning.Cloner;

public class DogSortUtility {
	
	private static Cloner cloner=new Cloner();

	public static List<Dog> cloneDogs(List<Dog> dogs)
	{
		return cloner.deepClone(dogs);
	}

	public static Dog findDogByName(List<Dog> dogs, String name)
	{
		return dogs.stream().filter(dog -> dog.getName().equals(name)).findFirst().get();
	}

	public static List<Dog> topologicalSort(List<Dog> inputDogs)
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

	/**
	 * Generate DOT file.  Use http://www.webgraphviz.com or DOT viewer of choice to view.
	 * @param dogs
	 * @param sortedDogs
	 * @param fileName
	 */
	public static void writeDotFile(List<Dog> dogs, List<Dog> sortedDogs, String fileName) {
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

}
