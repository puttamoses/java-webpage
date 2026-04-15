package com.game;

import java.util.Random;
import java.util.Scanner;

public class GuessGame {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int numberToGuess = random.nextInt(100) + 1;
        int guess = 0;
        int attempts = 0;

        System.out.println("🎮 Welcome to Guess the Number Game!");
        System.out.println("Guess a number between 1 and 100");

        while (guess != numberToGuess) {
            System.out.print("Enter your guess: ");
            guess = scanner.nextInt();
            attempts++;

            if (guess < numberToGuess) {
                System.out.println("📉 Too low! Try again.");
            } else if (guess > numberToGuess) {
                System.out.println("📈 Too high! Try again.");
            } else {
                System.out.println("🎉 Correct! You guessed in " + attempts + " attempts.");
            }
        }

        scanner.close();
    }
}
