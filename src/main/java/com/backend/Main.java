package com.backend;

public class Main {
    public static void main(String[] args) {
       solver my = new solver(8, 250, 1);
       System.out.println(my.winScore);
    int board[]=my.getAnAnswer();
     for (int i = 0; i < board.length; i++) {
        System.out.print(board[i] + " ");
    }
    System.out.println();
    }
}