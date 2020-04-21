# Project Title
Test project - Game of Life
Implemented as per PDF provided for Game of Life exercise (Game of Life exercise.pdf)

# Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
This is a maven project

# Prerequisites
1. JDK 1.8 
2. Maven
2. You need to set JAVA_HOME variable in your host environment variables where you need to run the program.
3. Also you need to set PATH variable of your host environment variable.

# Assumptions:
1. Only one seed is to be provided
2. The seed co-ords are zero indexed
3. The cells wrap around when the edges are reached
4. Seed co-ords do not wrap around. Applicaiton throws exception is out of bounds
5. The grid is a square
 
Direction to run:
1. Load the source code in an editor, for eg. Eclipse STS or IntelliJ. You can import it as a maven project.
2. To run the test, the tests are available in src/test/java
3. You can run the main method in com.practice.gameoflife.GameOfLifeApplication.java from your Java IDE.
   In the IDE console , please provide an integer or press enter (for indefinite iteration/ticks)
5. In command prompt, when you are in 'gameoflife' directory,
    a. build the project using 'mvn clean install'.
    b. cd target/classes 
	c. java com.practice.gameoflife.GameOfLifeApplication 
	The console will display the grid for each iteration (tick) with the pattern
	    
#Author
Tanveer Rameez Ali