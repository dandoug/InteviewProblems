package com.java.example.himanshu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by hkulkarni on 9/30/15.
 */
public class TopologicalSort {
    HashMap<String, ArrayList<String>> instructionMap=new HashMap<>();
    HashMap<String, Integer> incomingEdges=new HashMap<>();
    int numberOfDogs;
    public void initGraph(ArrayList<String> allDogs, ArrayList<String> instructions){
        numberOfDogs=allDogs.size();
        for(String dog:allDogs){
            incomingEdges.put(dog,0);
        }
        for(String instruction: instructions){
            String[] dogs=instruction.split(" ");
            String firstDog=dogs[0];
            String secondDog=dogs[2];
            ArrayList<String> value;
            //int incomingEdgeValue;
            if(instructionMap.containsKey(secondDog)){
                value=instructionMap.get(secondDog);
            }
            else{
                value=new ArrayList<>();
            }
            value.add(firstDog);
            instructionMap.put(secondDog, value);
            incomingEdges.put(firstDog, incomingEdges.get(firstDog)+1);
        }
    }

    public ArrayList<String> sortDogsInLine() throws CycleFoundException {
        ArrayList<String> sortedDogsInLine=new ArrayList<>();
        LinkedList<String> queue=getAllNodesWithZeroIncomingEdgeNodes();
        while(!queue.isEmpty()) {
            String nextDog = queue.removeFirst();
            sortedDogsInLine.add(nextDog);
            removeIncomingEdgesForDog(nextDog, queue);
        }
        //Topological sort would return lesser number of dogs to be arranged in a sorted line if there is a cycle detected in instructions.
        if(sortedDogsInLine.size()<numberOfDogs){
            throw new CycleFoundException("Detected cycle for the input instructions.Dogs can not be arranged in line!!");
        }
        return sortedDogsInLine;
    }

    private void removeIncomingEdgesForDog(String nextDog, LinkedList<String> queue) {
        ArrayList<String> dogs;
        if((dogs=instructionMap.get(nextDog))!=null){
            for(String dog:dogs){
                if(incomingEdges.containsKey(dog)){
                    int numberOfincomingEdges=incomingEdges.get(dog)-1;
                    if(numberOfincomingEdges==0) {
                        queue.add(dog);
                        incomingEdges.remove(dog);
                    }
                    incomingEdges.put(dog, numberOfincomingEdges);
                }
                else{
                    System.out.println("ERROR : This condition should not arise.");
                }
            }
        }
    }

    public LinkedList<String> getAllNodesWithZeroIncomingEdgeNodes() {
        LinkedList<String> queue=new LinkedList<>();
        for(String dog:incomingEdges.keySet()){
            if(incomingEdges.get(dog)==0){
                queue.add(dog);
            }
        }
        return queue;
    }


}
