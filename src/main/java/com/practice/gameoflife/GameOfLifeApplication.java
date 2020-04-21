package com.practice.gameoflife;

import java.util.Scanner;
import java.util.regex.Pattern;

public class GameOfLifeApplication {
	private final static int gridSize=25;
	private final static int[][] gliderPattern = new int[][] { { gridSize / 2 - 1, gridSize / 2 },
		{ gridSize / 2, gridSize / 2 + 1 }, { gridSize / 2 + 1, gridSize / 2 + 1 },
		{ gridSize / 2 + 1, gridSize / 2 }, { gridSize / 2 + 1, gridSize / 2 - 1 } };
		
	public static void main(String[] args) {
		System.out.println("Enter number of iteration required. If indefinite, simply press enter...");
		Scanner scanner=new Scanner(System.in);
		try {
		String line = scanner.nextLine();
		int iteration=-1;
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		if(pattern.matcher(line).matches())
			iteration = Integer.parseInt(line);
		GameOfLife game = new GameOfLife();
		game.simulate(gridSize, gliderPattern, iteration, 500);
		}finally {
			if(scanner != null)
			   scanner.close();
		}
	}

}