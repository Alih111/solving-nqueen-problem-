package com.backend;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
       solver my=new solver(3,1);
       my.generateChildren();
      
       my.fitnessFunction();
      
    }
}