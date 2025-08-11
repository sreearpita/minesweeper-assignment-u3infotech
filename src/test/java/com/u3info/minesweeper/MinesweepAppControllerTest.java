package com.u3info.minesweeper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Scanner;

@ExtendWith(MockitoExtension.class)
class MinesweepAppControllerTest {

    @Mock
    private Board mockBoard;
    
    @Mock
    private Cell mockCell;
    
    private MinesweepAppController controller;

    @BeforeEach
    void setUp() {
        controller = new MinesweepAppController();
    }

    @Test
    void revealAllMinesUsesMockBoard() throws Exception {
        when(mockBoard.getSize()).thenReturn(2);
        when(mockBoard.cell(0, 0)).thenReturn(mockCell);
        when(mockBoard.cell(0, 1)).thenReturn(mockCell);
        when(mockBoard.cell(1, 0)).thenReturn(mockCell);
        when(mockBoard.cell(1, 1)).thenReturn(mockCell);
        when(mockCell.isAMine()).thenReturn(true).thenReturn(false).thenReturn(false).thenReturn(true);

        Method revealAllMinesMethod = MinesweepAppController.class.getDeclaredMethod(
            "revealAllMines", Board.class);
        revealAllMinesMethod.setAccessible(true);
        revealAllMinesMethod.invoke(controller, mockBoard);

        verify(mockBoard, times(4)).cell(anyInt(), anyInt());
        verify(mockCell, times(4)).isAMine();
        verify(mockCell, times(2)).setRevealed(true);
    }

    @Test
    void promptIntReturnsValidValue() throws Exception {
        MinesweepAppController controller = new MinesweepAppController();
        String input = "5\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        Method promptIntMethod = MinesweepAppController.class.getDeclaredMethod(
            "promptInt", Scanner.class, PrintStream.class, String.class, int.class, int.class);
        promptIntMethod.setAccessible(true);

        int result = (int) promptIntMethod.invoke(controller, scanner, printStream, "Test prompt:", 1, 10);
        
        assertEquals(5, result);
        String outputStr = output.toString();
        assertTrue(outputStr.contains("Test prompt:"));
    }

    @Test
    void promptIntRejectsInvalidInput() throws Exception {
        MinesweepAppController controller = new MinesweepAppController();
        String input = "abc\n0\n15\n7\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        Method promptIntMethod = MinesweepAppController.class.getDeclaredMethod(
            "promptInt", Scanner.class, PrintStream.class, String.class, int.class, int.class);
        promptIntMethod.setAccessible(true);

        int result = (int) promptIntMethod.invoke(controller, scanner, printStream, "Test:", 1, 10);
        
        assertEquals(7, result);
        String outputStr = output.toString();
        assertTrue(outputStr.contains("valid integer"));
        assertTrue(outputStr.contains("between 1 and 10"));
    }

    @Test
    void setupBoardCreatesValidBoard() throws Exception {
        MinesweepAppController controller = new MinesweepAppController();
        String input = "4\n2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        Method setupBoardMethod = MinesweepAppController.class.getDeclaredMethod(
            "setupBoard", Scanner.class, PrintStream.class);
        setupBoardMethod.setAccessible(true);

        Board board = (Board) setupBoardMethod.invoke(controller, scanner, printStream);
        
        assertNotNull(board);
        assertEquals(4, board.getSize());
        assertEquals(2, board.getTotalMines());
        assertEquals(16, board.getTotalCells());
        
        String outputStr = output.toString();
        assertTrue(outputStr.contains("Enter the size of the grid"));
        assertTrue(outputStr.contains("Enter the number of mines"));
        assertTrue(outputStr.contains("Here is your minefield:"));
    }

    @Test
    void revealAllMinesWorksCorrectly() throws Exception {
        MinesweepAppController controller = new MinesweepAppController();
        Board board = new Board(3);
        board.placeMines(2, new java.util.Random(42));

        int initialMines = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.cell(r, c).isAMine()) {
                    initialMines++;
                    assertFalse(board.cell(r, c).isRevealed());
                }
            }
        }

        Method revealAllMinesMethod = MinesweepAppController.class.getDeclaredMethod(
            "revealAllMines", Board.class);
        revealAllMinesMethod.setAccessible(true);
        revealAllMinesMethod.invoke(controller, board);

        int revealedMines = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.cell(r, c).isAMine()) {
                    assertTrue(board.cell(r, c).isRevealed());
                    revealedMines++;
                }
            }
        }
        
        assertEquals(initialMines, revealedMines);
        assertEquals(2, revealedMines);
    }

}
