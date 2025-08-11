package com.u3info.minesweeper;

//Model class for cells
final class Cell {
    private boolean mine;
    private boolean revealed;
    private int adjacent;

    boolean isAMine() {
        return mine;
    }

    void setAMine(boolean mine) {
        this.mine = mine;
    }

    boolean isRevealed() {
        return revealed;
    }

    void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    int getAdjacent() {
        return adjacent;
    }

    void setAdjacent(int adjacent) {
        this.adjacent = adjacent;
    }
}
