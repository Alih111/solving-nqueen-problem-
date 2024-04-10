package com.backend;

public class Main {
    public static void main(String[] args) {
       solver my = new solver(6, 8, 1);
       my.generatePopulation();
       my.fitnessFunction();

       int bestScore = 1000000000;
       int c = 0;
       while(bestScore > 0){
           bestScore = my.generateNewGeneration();
           c ++;
        }
    }
}