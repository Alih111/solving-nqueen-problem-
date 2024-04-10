package com.backend;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Random;
public class solver {
    private int size, populationSize;
    private double mutationChance;
    private Set<int[]> children;
    private int winScore;

    public solver(int size, int populationSize, double mutationChance){
        this.size = size;
        this.populationSize = populationSize;
        this.mutationChance = mutationChance;
        this.winScore=0;
        children = new HashSet<>();

        for(int k=this.size-1;k>0;k--){
            this.winScore += k;
        }
        //System.out.println(this.winScore);
    }

    public void generatePopulation(){
        
        Random random = new Random();
        do{
            int[] child = new int[this.size];

            for(int j=0;j<size;j++){
                child[j]=random.nextInt(this.size);
            }
            this.children.add(child);
        }while(this.children.size() < this.populationSize);

        System.out.println("Elements of the set:");

        for (int[] arr : this.children){
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i]);

                if (i < arr.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("");
        }

        /* 
        System.out.println("Fitness scores per individual");
        for(int[] arr : this.children)
        System.out.print(this.evaluateFitness(arr));
        System.out.println("");
        */
    }

    public int[] fitnessFunction(){
        
       int updiagonal, downdiagonal, childScore;
       int[] childrenScores = new int[this.populationSize];
       int k = 0;
     
        for (int[] child : this.children){
            childScore = this.winScore;
           
            for(int i=0;i<this.size-1;i++){
                downdiagonal = child[i];
                updiagonal = child[i];

                for(int j=i+1;j<this.size;j++){
                    updiagonal--;
                    downdiagonal++;
                     
                    if(child[i]==child[j]){
                        childScore--;
                        continue;
                    }

                    if(updiagonal>=0){
                        if(updiagonal==child[j]){
                            childScore--;
                            continue;
                        }
                    }

                    if(downdiagonal<this.size){
                        if(downdiagonal==child[j]){
                            childScore--;
                            continue;
                        }
                    }      
                }
            }

            childrenScores[k++] = childScore;
        }
        return childrenScores;
    }

    public int[] mutate(int[] individual1){
        Random random = new Random();
        
        int numberOfMutations = random.nextInt(this.size);

        for(int i=0; i<numberOfMutations; i++){
            int mutatedIndex = random.nextInt(this.size);
            int generatedElement = random.nextInt(this.size);
            
            individual1[mutatedIndex] = generatedElement;
        }

        return individual1;
    }

    public ArrayList<int[]> crossover(int[] individual1, int[] individual2){
        int[] offspring1 = new int[this.size];
        int[] offspring2 = new int[this.size];
        ArrayList<int[]> output = new ArrayList<int[]>();
        int crossoverPoint = this.size / 2;
        
        for(int i=0; i<crossoverPoint; i++){
            offspring1[i] = individual1[i];
            offspring2[i] = individual2[i];
        }

        for(int i=crossoverPoint; i<this.size; i++){
            offspring1[i] = individual2[i];
            offspring2[i] = individual1[i];
        }

        Random random = new Random();
        if(random.nextDouble() >= 1 - this.mutationChance)
            offspring1 = this.mutate(offspring1);
        if(random.nextDouble() >= 1 - this.mutationChance)
            offspring2 = this.mutate(offspring2);

        output.add(offspring1);
        output.add(offspring2);

        return output;
    }

    public int evaluateFitness(int[] solution) {
        int conflicts = 0;
        int N = solution.length;

        // Check each queen
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Check if queens i and j are in the same row or diagonal
                if (solution[i] == solution[j] || Math.abs(solution[i] - solution[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }

        // The fitness is the number of conflicts
        return conflicts;
    }

    public int generateNewGeneration(){
        Set<int[]> newGeneration = new HashSet<>();
        int [][] childrenArray = this.children.toArray(new int[this.children.size()][]);
        
        for(int i=0; i<this.populationSize; i+=2){
            if(i+1 < this.populationSize){
                ArrayList<int[]> newOffspring = this.crossover(childrenArray[i], childrenArray[i+1]);
                newGeneration.add(newOffspring.get(0));
                newGeneration.add(newOffspring.get(1));
            }
            else{
                newGeneration.add(childrenArray[i]);
            }
        }

        int [][] newGenerationArray = newGeneration.toArray(new int[newGeneration.size()][]);
        System.out.println("New generation:");
        for (int[] arr : newGenerationArray){
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i]);

                if (i < arr.length - 1) {
                    System.out.print(", ");
                }
            }

            System.out.print("  Fitness score = ");
            System.out.print(this.evaluateFitness(arr));
            System.out.println("");
        }
        System.out.println("");

        int[][] oldAndNewGeneration = new int[this.children.size() * 2][];
        int j = 0;
        for(int i=0; i<childrenArray.length; i++){
            oldAndNewGeneration[j] = childrenArray[i];
            j ++;
        }
        for(int i=0; i<newGenerationArray.length; i++){
            oldAndNewGeneration[j] = newGenerationArray[i];
            j ++;
        }

        TreeMap<Integer, Integer> map = new TreeMap<>(); // map with new and old generation individuals' index as key and fitness score as value
        for(int i=0; i<oldAndNewGeneration.length; i++){
            map.put(i, this.evaluateFitness(oldAndNewGeneration[i]));
        }

        this.children.clear();
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(map.entrySet());

        while(this.children.size() < this.populationSize){
            int bestScore = 1000000000;
            for (Map.Entry<Integer, Integer> entry : entryList) {
                if(entry.getValue() < bestScore)
                    bestScore = entry.getValue();
            }
            Iterator<Map.Entry<Integer, Integer>> iterator = entryList.iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Integer> entry = iterator.next();
                if (entry.getValue() == bestScore && !this.children.contains(oldAndNewGeneration[entry.getKey()])) {
                    this.children.add(oldAndNewGeneration[entry.getKey()]);
                    iterator.remove();
                    break;
                }
            }
        }
        
        System.out.println("New population: ");
        
        int bestFitnessScore = 100000000;
        for (int[] arr : this.children){
            int fitnessScore = this.evaluateFitness(arr);
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i]);

                if (i < arr.length - 1) {
                    System.out.print(", ");
                }
            }

            System.out.print("  Fitness score = ");
            System.out.print(fitnessScore);
            System.out.println("");

            if(fitnessScore < bestFitnessScore)
                bestFitnessScore = fitnessScore;
        }

        return bestFitnessScore;
    }
}
