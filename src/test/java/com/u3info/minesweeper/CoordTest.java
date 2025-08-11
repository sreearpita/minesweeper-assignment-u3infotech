package com.u3info.minesweeper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordTest {
    @Test
    void parsesValid() {
        CoordinateMapper c = CoordinateMapper.parse("A1", 5);
        assertEquals(0, c.row);
        assertEquals(0, c.col);
        CoordinateMapper d = CoordinateMapper.parse("D4", 5);
        assertEquals(3, d.row);
        assertEquals(3, d.col);
    }

    @Test
    void rejectsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> CoordinateMapper.parse("1A", 5));
        assertThrows(IllegalArgumentException.class, () -> CoordinateMapper.parse("A0", 5));
        assertThrows(IllegalArgumentException.class, () -> CoordinateMapper.parse("Z9", 5));
        assertThrows(IllegalArgumentException.class, () -> CoordinateMapper.parse("A", 5));
    }
}
