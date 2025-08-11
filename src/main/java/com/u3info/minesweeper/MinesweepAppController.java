package com.u3info.minesweeper;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

/**
  Main controller for running the Minesweeper game in a CLI environment.
   
     -Prompt the player for board size and mine count  
     -Create Board, place mines. 
     -Accept and parse player input for cell selections. 
     -process moves and determine outcomes. 
     -Detect win or loss conditions and handle replay prompts. 
 */

public final class MinesweepAppController {

    private final MinesweepViewRenderer renderer = new MinesweepViewRenderer();

    public void run(InputStream in, PrintStream out) {
        Scanner sc = new Scanner(in);
        out.println("Welcome to Minesweeper!");
        while (true) {
            Board board = setupBoard(sc, out);
            renderer.printBoard(board, out);
            boolean gameOver = false;
            while (!gameOver) {
                out.print("Select a square to reveal (e.g. A1): ");
                String token = sc.next();
                try{
                    CoordinateMapper coordinateMapper = CoordinateMapper.parse(token, board.getSize());
                    Board.RevealResult res = board.reveal(coordinateMapper.row, coordinateMapper.col);
                    if (res.already) {
                        out.println("This square was already revealed (" + res.adjacent + ").");
                    } else if (res.hitMine) {
                        out.println("Oh no, you detonated a mine! Game over.");
                        revealAllMines(board);
                        renderer.printBoard(board, out);
                        gameOver = true;
                    } else {
                        out.println("This square contains " + res.adjacent + " adjacent mines. ");
                        out.println();
                        out.println("Here is your updated minefield:");
                        renderer.printBoard(board, out);
                        if (board.allNonMinesRevealed()) {
                            out.println("Congratulations, you have won the game!");
                            gameOver = true;
                        }
                    }
                }catch (IllegalArgumentException ex){
                    out.println("Invalid input: " + ex.getMessage());
                }
            }
            out.print("Press any key to play again...");
            sc.nextLine();
            sc.nextLine();
        }
    }

    private Board setupBoard(Scanner sc, PrintStream out) {
        //If we allow more than 26 rows, we run out of single uppercase letters.
        int size = promptInt(sc, out, "Enter the size of the grid (e.g. 4 for a 4x4 grid): ", 2, 26);
        Board board = new Board(size);
        int maxMines = (int) Math.floor(board.getTotalCells() * 0.35);
        int minesCount = promptInt(sc, out, "Enter the number of mines to place on the grid (maximum is 35% of the total squares): ", 1, maxMines);
        board.placeMines(minesCount, new Random());
        out.println();
        out.println("Here is your minefield:");
        return board;
    }

    private int promptInt(Scanner sc, PrintStream out, String prompt, int min, int max) {
        while (true) {
            out.println(prompt);
            String input = sc.next();
            try {
                int val = Integer.parseInt(input);
                if (val < min || val > max) {
                    out.println("Please enter a number between " + min + " and " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException ex) {
                out.println("Please enter a valid integer.");
            }
        }
    }

    private void revealAllMines(Board board) {
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                Cell cell = board.cell(r, c);
                if (cell.isAMine()) {
                    cell.setRevealed(true);
                }
            }
        }
    }
}
