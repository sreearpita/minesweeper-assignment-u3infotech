package com.u3info.minesweeper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 logic of game board.

    -create the grid of Cell objects with the given size.
    -Randomly placing mines and calculating the number of adjacent mines for each non-mine cell.
    -Handle reveal operations.
    -Track the total number of revealed cells and determining win/loss conditions.
 */

public class Board {

    private final int size;
    private final Cell[][] grid;
    private int totalMines;
    private int revealedCount;

    int getSize() {
        return size;
    }

    int getTotalMines() {
        return totalMines;
    }

    int getRevealedCount() {
        return revealedCount;
    }

    int getTotalCells() {
        return size * size;
    }

    Cell cell(int r, int c) {
        return grid[r][c];
    }

    Board(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = new Cell();
            }
        }
    }

    /* randomly places the specified number of mines onto the game board and then
    computes the values for all other cells.*/
    void placeMines(int minesCount, Random rng) {
        int max = (int)Math.floor(getTotalCells() * 0.35);
        if (minesCount < 1 || minesCount > max) {
            throw new IllegalArgumentException("Mines must be between 1 and " + max);
        }
        this.totalMines = minesCount;
        int placed = 0;
        while (placed < minesCount) {
            int r = rng.nextInt(size);
            int c = rng.nextInt(size);
            if (!grid[r][c].isAMine()) {
                grid[r][c].setAMine(true);
                placed++;
            }
        }

        computeAdjacents();
    }

    //counts the no. of mines in its 8 neighboring squares (up to 8; fewer on edges/corners).
    //no. of mines in the adjacent cells is the value of the cell
    private void computeAdjacents() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c].isAMine()) {
                    grid[r][c].setAdjacent(-1);
                    continue;
                }
                int cnt = 0;
                for (int rr = Math.max(0, r-1); rr <= Math.min(size-1, r+1); rr++) {
                    for (int cc = Math.max(0, c-1); cc <= Math.min(size-1, c+1); cc++) {
                        if (rr == r && cc == c) continue;
                        if (grid[rr][cc].isAMine()) cnt++;
                    }
                }
                grid[r][c].setAdjacent(cnt);
            }
        }
    }

    boolean allNonMinesRevealed() {
        return revealedCount == (getTotalCells() - totalMines);
    }

    static final class RevealResult {
        final boolean hitMine;
        final boolean already;
        final int adjacent;

        private RevealResult(boolean hitMine, boolean already, int adjacent) {
            this.hitMine = hitMine;
            this.already = already;
            this.adjacent = adjacent;
        }

        static RevealResult mine() {
            return new RevealResult(true, false, -1);
        }

        static RevealResult safe(int adj) {
            return new RevealResult(false, false, adj);
        }

        static RevealResult alreadyRevealed(int adj) {
            return new RevealResult(false, true, adj);
        }
    }

    RevealResult reveal(int r, int c) {
        //check if not inside bounds
        if (!(r >= 0 && r < size && c >= 0 && c < size)) throw new IllegalArgumentException("Out of bounds");
        Cell cell = grid[r][c];

        if (cell.isRevealed()) {
            return RevealResult.alreadyRevealed(cell.getAdjacent());
        }

        cell.setRevealed(true);
        revealedCount++;

        if (cell.isAMine()) {
            return RevealResult.mine();
        }

        if (cell.getAdjacent() == 0) {
            floodReveal(r, c);
        }

        return RevealResult.safe(cell.getAdjacent());
    }

    //find neighbor cells
    List<int[]> neighbors(int r, int c) {
        List<int[]> res = new ArrayList<int[]>();
        for (int rr = r-1; rr <= r+1; rr++) {
            for (int cc = c-1; cc <= c+1; cc++) {
                if (rr == r && cc == c) continue;
                if ((rr >= 0 && rr < size && cc >= 0 && cc < size)) res.add(new int[]{rr, cc});
            }
        }
        return res;
    }

    /*Reveals all safe cells using a breadth-first search (BFS).*/
    private void floodReveal(int r, int c) {
        // Track which cells we've already processed in this expansion
        boolean[][] visited = new boolean[size][size];

        // Queue for BFS expansion, starting from the initial zero cell
        ArrayDeque<int[]> dq = new ArrayDeque<int[]>();
        dq.add(new int[]{r, c});
        visited[r][c] = true;

        // Process cells in BFS order
        while (!dq.isEmpty()) {
            int[] cur = dq.removeFirst();
            int cr = cur[0], cc = cur[1];

            // For each neighbor of the current cell
            for (int[] nb : neighbors(cr, cc)) {
                int nr = nb[0], nc = nb[1];
                Cell cel = grid[nr][nc];

                // Reveal neighbor if not already revealed
                if (!cel.isRevealed()) {
                    cel.setRevealed(true);
                    revealedCount++;
                }
                // If this neighbor is also a zero cell and not visited yet, expand it
                if (!visited[nr][nc] && !cel.isAMine() && cel.getAdjacent() == 0) {
                    visited[nr][nc] = true;
                    dq.addLast(new int[]{nr, nc});
                }
            }
        }
    }

}
