package com.practice.gameoflife;

import com.practice.gameoflife.exceptions.ApplicationFailureException;
import com.practice.gameoflife.exceptions.SeedOutOfBoundsException;

/**
 * Basic implementation of Game of Life
 * @author Tanveer Rameez Ali
 *
 */
public class GameOfLife {

	public static final int LIVE = 1;
	public static final int DEAD = 0;
	public static int defaultWaitTimeMillis=500;
	
	public GameOfLife() {

	}

	/**
	 * Simulate Game of Life
	 * 
	 * @param gridSize size of each side of the grid
	 * @param pattern  co-ords of the initial seed (w.r.t. zero-indexed grid)
	 */
	public void simulate(int gridSize, int[][] pattern) {

		// simulate with infinite iterations (-1) and 500 milliseconds wait time per tick
		simulate(gridSize, pattern, -1, defaultWaitTimeMillis);
	}

	/**
	 * Simulate Game of Life
	 * 
	 * @param gridSize       size of each side of the grid
	 * @param pattern        co-ords of the initial seed (w.r.t. zero-indexed grid)
	 * @param iterationCount number of times of iteration. negative value indicates indefinite 
	 * @waitTime time to wait after each tick (for rendering purpose)
	 * @return the final grid
	 */
	public int[][] simulate(int gridSize, int[][] pattern, int iterationCount, int waitTime) {

		try {
			int[][] grid = generateInitialGrid(gridSize, pattern);
			return iterate(grid, iterationCount, waitTime);
			
		} catch (InterruptedException ie) {
			ie.printStackTrace();
			throw new ApplicationFailureException("Application failed due to thread interruption");
		}
	}

	/**
	 * Generate initial grid with the seed pattern
	 * 
	 * @param gridSize size of each side of the grid
	 * @param pattern  co-ords of the initial seed (w.r.t. zero-indexed grid)
	 * @return the grid containing the seed
	 */
	public int[][] generateInitialGrid(int gridSize, int[][] pattern) {
		int[][] grid = new int[gridSize][gridSize];
		for (int i = 0; i < pattern.length; i++) {
			if (pattern[i][0] <= 0 || pattern[i][0] >= gridSize || pattern[i][1] <= 0 || pattern[i][1] >= gridSize)
				throw new SeedOutOfBoundsException("Seed value " + pattern[i][0] + " , " + pattern[i][1]
						+ " out of bounds for matrix with size " + gridSize);
			grid[pattern[i][0]][pattern[i][1]] = LIVE;
		}
		print(grid);
		return grid;
	}

	/**
	 * Iterate
	 * 
	 * @param grid the grid containing the pattern
	 * @param n    number of times of iteration (tick)
	 * @waitTime time to wait after each tick (for rendering purpose);
	 * @return the grid containing the pattern after n iteration
	 */
	public int[][] iterate(int[][] grid, int n, int waitTime) throws InterruptedException {
		int count = 1;
		int gridSize=grid.length;
		int[][] futureGrid = new int[gridSize][gridSize];
		int[][] currentGrid = grid;
		do {// for each tick, generate
			generate(currentGrid, futureGrid);
			//swap arrays, rather than create a new array each time for futureGrid
			int[][] temp=currentGrid;
			currentGrid = futureGrid;
			futureGrid = temp;
			
			Thread.sleep(waitTime);
			count++;
			// continue n times or indefinitely if n < 0
		} while (count <= n || n < 0);
		return currentGrid;
	}


	/**
	 * Generate next generation pattern for a tick based on : 
	 * 1. Any live cell with fewer than two live neighbours dies , as if caused by underpopulation. 
	 * 2. Any live cell with two or three live neighbours lives on to the next generation.
	 * 3. Any live cell with more than three live neighbours dies , as if by overcrowding. 
	 * 4. Any dead cell with exactly three live neighbours becomes a live cell , as if by reproduction.
	 * 
	 * @param currentGrid grid on which the game of life logic applies
	 * @param futureGrid  generated grid for the next cycle (tick)
	 * @return
	 */
	private void generate(int[][] currentGrid, int[][] futureGrid) {
		int gridSize = currentGrid.length;
		for (int row = 0; row < currentGrid.length; row++) {
			for (int col = 0; col < currentGrid[row].length; col++) {
				int cell = currentGrid[row][col];
				int liveNeighbourCount = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {

						// wrap around at ends
						int x = (row + i + gridSize) % gridSize;
						int y = (col + j + gridSize) % gridSize;
						if (x == row && y == col) {
							continue;
						}
						if (currentGrid[x][y] == LIVE) {
							liveNeighbourCount++;
						}
					}
				}
				if (cell == LIVE) {
					futureGrid[row][col] = (liveNeighbourCount == 2 || liveNeighbourCount == 3) ? LIVE : DEAD;
				} else { // if cell == DEAD
					if (liveNeighbourCount == 3)
						futureGrid[row][col] = LIVE;
					else
						futureGrid[row][col] = DEAD;
				}
			}
		}
		print(futureGrid);
	}

	private char border = ' ';
	private String live = "X ";
	private String dead = ". ";

	/**
	 * Prints the grid on the console, for rendering purpose
	 * @param grid
	 */
	private void print(int[][] grid) {
		System.out.println(border);
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				System.out.print(grid[row][col] == DEAD ? dead : live);
			}
			System.out.println(border);
		}
	}

}
