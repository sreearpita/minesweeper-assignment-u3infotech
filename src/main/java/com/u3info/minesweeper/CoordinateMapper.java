package com.u3info.minesweeper;

/* this class takes a string like "A1" from the user and convert it into row and column
 indexes that the program can work with.
 */

final class CoordinateMapper {
    final int row;
    final int col;

    private CoordinateMapper(int row, int col) {
        this.row = row;
        this.col = col;
    }

    static CoordinateMapper parse(String token, int size) {
        if (token == null) throw new IllegalArgumentException("Null input");
        String t = token.trim();

        if (t.length() < 2) throw new IllegalArgumentException("Invalid coordinate: " + token);
        char first = t.charAt(0);

        if (!Character.isLetter(first)) throw new IllegalArgumentException("Row must be a letter");

        int row = Character.toUpperCase(first) - 'A';

        if (row < 0 || row >= size) throw new IllegalArgumentException("Row out of range");

        String num = t.substring(1);
        int col;
        try {
            col = Integer.parseInt(num) - 1;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Column must be a number");
        }

        if (col < 0 || col >= size) throw new IllegalArgumentException("Column out of range");
        return new CoordinateMapper(row, col);
    }
}
