package com.backend;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;
public class solver {
    private int size,numberOfChildren;
    private Set<int[]> children;
    private int winScore;
    public solver(int size,int numberOfChildren){
        this.size=size;
        this.numberOfChildren=numberOfChildren;
        this.winScore=0;
        children=new HashSet<>();
        for(int k=this.size-1;k>0;k--){
            this.winScore+=k;
        }
        System.out.println(this.winScore);

    }
    public void generateChildren(){
        
        Random random = new Random();
        do{
            int[] child = new int[this.size];
        for(int j=0;j<size;j++){
            child[j]=random.nextInt(this.size);
        }this.children.add(child);}while(this.children.size()<this.numberOfChildren);
        System.out.println("Elements of the set:");
        for (int[] arr : this.children) 
        {System.out.println("");
            for (int i = 0; i < arr.length; i++) {
                System.out.print(arr[i]);
                if (i < arr.length - 1) {
                    System.out.print(", ");
                }}
        }
    }
    public void fitnessFunction(){
        
       int updiagonal,downdiagonal,childScore;
       int[] childrenScores=new int[this.numberOfChildren];
       int k=0;
     
        for (int[] child : this.children){
            childScore=this.winScore;
           
            for(int i=0;i<this.size-1;i++){
                downdiagonal=child[i];
                updiagonal=child[i];
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
                    }}
                    if(downdiagonal<this.size){
                    if(downdiagonal==child[j]){
                        childScore--;
                        continue;
                    }}      
            }}childrenScores[k++]=childScore;
            System.out.println();
            System.out.println(childScore);
            
          

        }
    }
}
