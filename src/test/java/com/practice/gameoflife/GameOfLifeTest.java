package com.practice.gameoflife;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.practice.gameoflife.exceptions.SeedOutOfBoundsException;

public class GameOfLifeTest {
	private final static int gridSize = 25;
	// zero-indexed glider
	private final static int[][] glider = new int[][] { { gridSize / 2 - 1, gridSize / 2 },
			{ gridSize / 2, gridSize / 2 + 1 }, { gridSize / 2 + 1, gridSize / 2 + 1 },
			{ gridSize / 2 + 1, gridSize / 2 }, { gridSize / 2 + 1, gridSize / 2 - 1 } };

	@DisplayName("Test Initial Grid creation")
	@MethodSource
	@ParameterizedTest(name = "{0}")
	void initialGridTest(String description, int gridSize, int[][] seed) {
		String hashKeyformat = "%d,%d";
		Set<String> gliderCoordsSet = new HashSet<>();
		for (int[] row : seed) {
			gliderCoordsSet.add(String.format(hashKeyformat, row[0], row[1]));
		}
		GameOfLife game = new GameOfLife();
		int[][] grid = game.generateInitialGrid(gridSize, seed);
		assertNotNull(grid);
		assertEquals(gridSize, grid.length);
		assertEquals(gridSize, grid[0].length);

		int liveCellsCount = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				if (grid[row][col] == GameOfLife.LIVE) {
					assertTrue(gliderCoordsSet.contains(String.format(hashKeyformat, row, col)));
					liveCellsCount++;
				}
			}
		}
		assertEquals(seed.length, liveCellsCount);

	}

	static Stream<Arguments> initialGridTest() {
		// zero-indexed glider
		int gridSize = 25;
		return Stream.of(Arguments.of("Initialisation Test with Glider", gridSize, glider));
	}

	@DisplayName("Test Grid Iteration")
	@MethodSource
	@ParameterizedTest(name = "{0}")
	void iterationTest(String description, int gridSize, int[][] seed, int iterationCount,
			int[][] newGliderPosition) {
		String hashKeyformat = "%d,%d";
		GameOfLife game = new GameOfLife();
		int[][] grid = game.simulate(gridSize, seed, iterationCount, 0);

		assertNotNull(grid);
		assertEquals(gridSize, grid.length);
		assertEquals(gridSize, grid[0].length);
		Set<String> expectedGliderCoordsSet = new HashSet<>();
		for (int[] row : newGliderPosition) {
			expectedGliderCoordsSet.add(String.format(hashKeyformat, row[0], row[1]));
		}
		int liveCellsCount = 0;
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				if (grid[row][col] == GameOfLife.LIVE) {
					assertTrue(expectedGliderCoordsSet.contains(String.format(hashKeyformat, row, col)));
					liveCellsCount++;
				}
			}
		}
		assertEquals(seed.length, liveCellsCount);
	}

	static Stream<Arguments> iterationTest() {
		//zero-th indexed glider position
		int[][] gliderAfterIteration1 = new int[][] { 
			 { gridSize / 2 , gridSize / 2 - 1 },{ gridSize / 2, gridSize / 2 + 1 }, 
			 { gridSize / 2 + 1, gridSize / 2 },{ gridSize / 2 + 1, gridSize / 2 +1 },
			 { gridSize / 2 + 2, gridSize / 2  } };
		int[][] gliderAfterIteration2 = new int[][] {{ gridSize / 2 , gridSize / 2 + 1 },
			 { gridSize / 2 + 1, gridSize / 2 - 1 }, { gridSize / 2 + 1, gridSize / 2 + 1 },
			 { gridSize / 2 + 2, gridSize / 2},{ gridSize / 2 + 2, gridSize / 2 + 1 } };
			 
		int[][] gliderAfterIteration3 = new int[][] {{ gridSize / 2 , gridSize / 2  },
			 { gridSize / 2 + 1, gridSize / 2 }, { gridSize / 2 + 1, gridSize / 2 + 1 },{ gridSize / 2 + 1, gridSize / 2 + 2 },
			 { gridSize / 2 + 2, gridSize / 2},{ gridSize / 2 + 2, gridSize / 2 + 1 } };
		
		//test for wrapping of cells
	    int[][] gliderOnEdgeSeed = new int[][] {{ gridSize -3 , gridSize -2  },
			 { gridSize - 2, gridSize - 1 }, 
			 { gridSize - 1 , gridSize - 3}, { gridSize -1 , gridSize - 2},{ gridSize - 1, gridSize  - 1 } };
		
		int[][] gliderOnEdgeIter1 = new int[][] {{ 0 , gridSize - 2  },
				 { gridSize - 2, gridSize - 3 }, { gridSize - 2, gridSize - 1 },
				 { gridSize - 1, gridSize - 2}, { gridSize -1 , gridSize - 1} };
				 
		int[][] gliderOnEdgeIter3 = new int[][] {{ 0 , gridSize - 2  },{ 0 , gridSize - 1 },
				 { gridSize - 2, gridSize - 2 }, 
				 { gridSize - 1, 0}, { gridSize -1 , gridSize - 1} };
				 
		return Stream
				.of(Arguments.of("Iteration Test with Glider: 1 iteration", gridSize, glider, 1, gliderAfterIteration1),
				    Arguments.of("Iteration Test with Glider: 2 iteration", gridSize, glider, 2, gliderAfterIteration2),
				    Arguments.of("Iteration Test with Glider: 3 iteration", gridSize, glider, 3, gliderAfterIteration3),
				    Arguments.of("Iteration Test with Glider: 2 iteration", gridSize, gliderAfterIteration1, 2, gliderAfterIteration3),
				    Arguments.of("Iteration Test with Glider on edge: 1 iteration", gridSize, gliderOnEdgeSeed, 1, gliderOnEdgeIter1),
				    Arguments.of("Iteration Test with Glider on edge: 3 iteration", gridSize, gliderOnEdgeSeed, 3, gliderOnEdgeIter3)
						);
	}
	
	@DisplayName("Test Seed outside Grid")
	@MethodSource
	@ParameterizedTest(name = "{0}")
	void seedOutsideGridTest(String description, int gridSize, int[][] seed) {
		GameOfLife game = new GameOfLife();
		assertThrows(SeedOutOfBoundsException.class, ()-> game.simulate(gridSize, seed, 1,0));
	}
	
	static Stream<Arguments> seedOutsideGridTest() {
		//zero-th indexed seed position. So gridSize value is outside the grid
		int[][] seedXOutsideGrid1 = new int[][] { { gridSize , gridSize / 2 - 1 }};
	    int[][] seedYOutsideGrid1 = new int[][] { { gridSize/2 , gridSize }};
		int[][] seedXOutsideGrid2 = new int[][] { { gridSize + 5 , gridSize / 2 - 1 }};
	    int[][] seedYOutsideGrid2 = new int[][] { { gridSize/2 , gridSize + 3 }};
		int[][] seedXOutsideGrid3 = new int[][] {  { -5 , gridSize + 3 }};
		int[][] seedYOutsideGrid3 = new int[][] {  { gridSize/2 , -5 }};
		return Stream
				.of(Arguments.of("Iteration Test with seed X value outside grid: x just outside boundary > gridSize ", gridSize, seedXOutsideGrid1),
					Arguments.of("Iteration Test with seed Y value outside grid: y just outside boundary > gridSize ", gridSize, seedYOutsideGrid1),
					Arguments.of("Iteration Test with seed X value outside grid: x > 0", gridSize, seedXOutsideGrid2),
					Arguments.of("Iteration Test with seed Y value outside grid: y > 0", gridSize, seedYOutsideGrid2),
					Arguments.of("Iteration Test with seed X value outside grid: x < 0", gridSize, seedXOutsideGrid3),
					Arguments.of("Iteration Test with seed Y value outside grid: y < 0", gridSize, seedYOutsideGrid3)	
				    );
	}

	

}
