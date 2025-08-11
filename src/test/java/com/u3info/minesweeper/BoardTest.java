package com.u3info.minesweeper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

class BoardTest {

    @Test
    void placesMinesDeterministically() {
        Board b = new Board(4);
        b.placeMines(3, new Random(42));
        int mines = 0;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (b.cell(r,c).isAMine()) mines++;
            }
        }
        assertEquals(3, mines);
    }

    @Test
    void coordinateMapperParsesValidInput() {
        CoordinateMapper coord = CoordinateMapper.parse("A1", 4);
        assertEquals(0, coord.row);
        assertEquals(0, coord.col);
        
        coord = CoordinateMapper.parse("D4", 4);
        assertEquals(3, coord.row);
        assertEquals(3, coord.col);
    }

    @Test
    void coordinateMapperThrowsOnInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> 
            CoordinateMapper.parse("Z1", 4));
        assertThrows(IllegalArgumentException.class, () -> 
            CoordinateMapper.parse("A9", 4));
        assertThrows(IllegalArgumentException.class, () -> 
            CoordinateMapper.parse("1A", 4));
    }

    @Test
    void revealingMineReturnsCorrectResult() {
        Board board = new Board(3);
        board.cell(1, 1).setAMine(true);
        board.cell(1, 1).setAdjacent(-1);
        
        Board.RevealResult result = board.reveal(1, 1);
        assertTrue(result.hitMine);
        assertFalse(result.already);
        assertEquals(-1, result.adjacent);
    }

    @Test
    void floodRevealWorksForZeroAdjacentCells() {
        Board board = new Board(4);
        board.placeMines(1, new Random(123));
        
        int zeroRow = -1, zeroCol = -1;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (!board.cell(r, c).isAMine() && board.cell(r, c).getAdjacent() == 0) {
                    zeroRow = r;
                    zeroCol = c;
                    break;
                }
            }
            if (zeroRow != -1) break;
        }
        
        if (zeroRow != -1) {
            int initialRevealed = board.getRevealedCount();
            board.reveal(zeroRow, zeroCol);
            assertTrue(board.getRevealedCount() > initialRevealed + 1);
        }
    }

    @Test
    void allNonMinesRevealedDetectsWinCondition() {
        Board board = new Board(3);
        board.placeMines(1, new Random(456));
        
        assertFalse(board.allNonMinesRevealed());
        
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (!board.cell(r, c).isAMine() && !board.cell(r, c).isRevealed()) {
                    board.reveal(r, c);
                }
            }
        }
        
        assertTrue(board.allNonMinesRevealed());
    }

}
