# Minesweeper Game - Design Analysis

## Overview
Console-based Minesweeper game in Java following classic rules with clean architecture.

## Assumptions 
- Only grid size of 2-26 considered 
- Only 8 neighbours need to be considered
- If neighbours do not have any mines, assign 0 to the square
- Value of each cell is number of mines in its neighbourhood

## Core Design Decisions

### MVC Architecture
Separated Board/Cell (model), MinesweepViewRenderer (view), MinesweepAppController (controller)

I chose this architecture for the code organisation because using it 
enables independent testing and maintainability. It is readable and a pattern followed for production grade applications.

### Computing Adjacents
I chose to compute all adjacent mine counts in a single pass after placing all mines because it's more efficient than calculating them on-demand. This approach has O(n²) time complexity where n is the grid size, which is acceptable for typical Minesweeper grid sizes.

I used Math.max(0, r-1) and Math.min(size-1, r+1) for boundary checking because it's cleaner and more readable than multiple if-statements. This approach automatically handles edge and corner cases without special logic - the loops simply iterate over the valid range of neighboring cells.

### BFS Flood Fill
I chose iterative breadth-first search

I chose this because we need to only recursively check adjacent elements and be able to find empty squares in standard minesweeper pattern. 

## Environment Requirements

### Cross-Platform
- Pure Java 8, no platform dependencies
- Have tested on MacOS. Will be able to run on Linux and Windows environments directly.

## Running Instructions using Maven
- **Build** -> `mvn compile`
- **Test** -> `mvn test` 
- **Run** -> `mvn exec:java`

