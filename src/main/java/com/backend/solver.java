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
    private List<int []> childrenArray ;
    public int winScore;
    private List<Integer> scores = new ArrayList<>();;
    public solver(int size, int populationSize, double mutationChance){
        this.size = size;
        this.populationSize = populationSize;
        this.mutationChance = mutationChance;
        this.winScore=0;
      

        for(int k=this.size-1;k>0;k--){
            this.winScore += k;
        }
        //System.out.println(this.winScore);
    }

    private void generatePopulation(){
        Set<int[]> children=new HashSet<>();
        Random random = new Random();
        do{
            int[] child = new int[this.size];

            for(int j=0;j<size;j++){
                child[j]=random.nextInt(this.size);
            }
            children.add(child);
        }while(children.size() < this.populationSize);

        System.out.println("Elements of the set:");

        for (int[] arr : children){
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i]);

                if (i < arr.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("");
        }
        childrenArray =  new ArrayList<>(children);;
        /* 
        System.out.println("Fitness scores per individual");
        for(int[] arr : this.children)
        System.out.print(this.evaluateFitness(arr));
        System.out.println("");
        */
    }
    private int fitnessChild(int[] child){
        int updiagonal, downdiagonal;
        int childScore1 = this.winScore;
           
            for(int i=0;i<this.size-1;i++){
                downdiagonal = child[i];
                updiagonal = child[i];

                for(int j=i+1;j<this.size;j++){
                    updiagonal--;
                    downdiagonal++;
                     
                    if(child[i]==child[j]){
                        childScore1--;
                        continue;
                    }

                    if(updiagonal>=0){
                        if(updiagonal==child[j]){
                            childScore1--;
                            continue;
                        }
                    }

                    if(downdiagonal<this.size){
                        if(downdiagonal==child[j]){
                            childScore1--;
                            continue;
                        }
                    }      
                }
            }
            return childScore1;
    }
    private void fitnessFunction(){
        
       int updiagonal, downdiagonal, childScore;
   
     
        for (int[] child : this.childrenArray){
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

            this.scores.add(childScore);
        }
       
    }

    private int[] mutate(int[] individual1){
        Random random = new Random();
        
        int numberOfMutations = random.nextInt(this.size);

        for(int i=0; i<numberOfMutations; i++){
            int mutatedIndex = random.nextInt(this.size);
            int generatedElement = random.nextInt(this.size);
            
            individual1[mutatedIndex] = generatedElement;
        }

        return individual1;
    }

    private ArrayList<int[]> crossover(int[] individual1, int[] individual2){
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
    private  int getRandomIndex(int length) {
        Random random = new Random();
        int randomIndex;
        if (random.nextDouble() < 0.5) {
            // Choose from the first 10% of the array with 50% probability
            randomIndex = random.nextInt(length / 10);
        } else {
            // Choose from the rest of the array with 50% probability
            randomIndex = random.nextInt(length - (length / 10)) + (length / 10);
        }
        return randomIndex;
    }
   private int[] solve(){
        
        
        int n =scores.size();
        for (int i = 0; i < n - 1; i++) {
            int temp=-1;
            for (int j = 0; j < n - i - 1; j++) {
                if (scores.get(j) < scores.get(j+1)) {
                    // Swap array[j] and array[j+1]
                     temp = scores.get(j);
                     scores.set(j, scores.get(j + 1));
                     scores.set(j + 1, temp);
                    int []temp1=childrenArray.get(j);
                    childrenArray.set(j, childrenArray.get(j + 1));
                    childrenArray.set(j + 1, temp1);
                }
            }
            if(temp==-1){
                break;
            }
        } 
        int [] baba=childrenArray.get(getRandomIndex(n));
        int [] mama=childrenArray.get(getRandomIndex(n));
        ArrayList<int[]> baby=crossover(baba,mama);
        childrenArray.add(0,baby.get(0));
        scores.add(0,fitnessChild(baby.get(0)));
        childrenArray.add(0,baby.get(1));
        scores.add(0,fitnessChild(baby.get(1)));
        System.out.println(scores.get(0));
        if(scores.get(0)==this.winScore){
            return childrenArray.get(0);
        }
        else{
            int[] array = new int[this.size];
            for (int i = 0; i < array.length; i++) {
                array[i] = -1;
            }
    
            return array;
        }

    }
    public int[] getAnAnswer(){
        generatePopulation();
        fitnessFunction();
        int[] arr=new int[this.size];
        for (int i=0;i<100000;i++){
        arr=solve();
        if(arr[0]!=-1)
        break;
    }
    return arr;
    }
}
