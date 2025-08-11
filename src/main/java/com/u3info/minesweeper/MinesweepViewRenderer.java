package com.u3info.minesweeper;

import java.io.PrintStream;

/**
 It is purely concerned with display.

 -Print column numbers across the top and row labels (A-Z) down the side.
 -Display unrevealed cells as underscores.
 -Display revealed mine cells.
 -Display revealed cells with count (0–8).
 */
final class MinesweepViewRenderer {

    void printBoard(Board board, PrintStream out) {
        int n = board.getSize();
        out.print("  ");
        for (int c = 1; c <= n; c++) {
            out.print(c);
            if (c < n) out.print(" ");
        }
        out.println();
        for (int r = 0; r < n; r++) {
            //print char for row eg: 'A' + 1 → 66 -> cast to char -> 'B'
            out.print((char)('A' + r));
            out.print(" ");
            for (int c = 0; c < n; c++) {
                Cell cell = board.cell(r, c);
                if (!cell.isRevealed()) {
                    out.print("_");
                } else if (cell.isAMine()) {
                    out.print("*");
                } else {
                    out.print(cell.getAdjacent());
                }
                if (c < n - 1) out.print(" ");
            }
            out.println();
        }
        out.println();
    }
}
