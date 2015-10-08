package org.westminsterkenel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import com.google.common.base.Joiner;

public class OutputUtilities {

	/**
	 * Generate DOT file.  Use http://www.webgraphviz.com or DOT viewer of choice to view.
	 * @param dogs
	 * @param sortedDogs
	 * @param fileName
	 */
	public static void writeDotFile(List<IDog> dogs, List<IDog> sortedDogs, String fileName) {
		try {
			Writer bw = new BufferedWriter(new FileWriter(new File(fileName)));
			bw.write("digraph {");
			bw.write("edge[weight=1000];");
			
			bw.write(Joiner.on("->").join(sortedDogs.stream().map(dog -> dog.getName()).iterator()));
			
			bw.write("[color=red];\n");
			bw.write("edge[weight=1];\n");
			
			for (IDog dog : dogs) {
				for (IDog weakerDog : dog.dogsBittenByThisDog()) {
					bw.append(dog.getName())
					  .append(" -> ")
					  .append(weakerDog.getName())
					  .append("[label=bites];\n");
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
