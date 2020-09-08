import edu.princeton.cs.algs4.LinkedBag;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Iterator;
import java.util.PrimitiveIterator;

public class Percolation {

    private boolean[][] grid;

    private int gridSize;

    private int openSites;

    private WeightedQuickUnionUF path;

    private LinkedBag<Integer> topRow, botRow;

    private boolean percolates;

    public Percolation(int n) {
        checkSize(n, n);
        gridSize = n;
        grid = new boolean[n][n];
        openSites = 0;
        path = new WeightedQuickUnionUF(n*n);
        topRow = new LinkedBag<Integer>();
        botRow = new LinkedBag<Integer>();
        percolates = false;
    }

    private void checkSize(int row, int col) {
        if (row < 0 || col < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        else if (gridSize > 0) {
            if (row > gridSize - 1 || col > gridSize - 1) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
    }

    public void open(int row, int col) {
        checkSize(row, col);
        if(grid[col][row] == false) {
            grid[col][row] = true;
            openSites++;
            if(row == 0) {
                topRow.add(col);
                checkPercolation();
            }
            else if(row == gridSize - 1) {
                botRow.add(col + gridSize*(gridSize - 1));
                checkPercolation();
            }
            checkSurroundings(row, col);
        }
    }

    public void checkPercolation() {
        if(topRow.size() > 0 && botRow.size() > 0) {
            Iterator<Integer> top, bot;
            top = topRow.iterator();
            while (top.hasNext()) {
                bot = botRow.iterator();
                while (bot.hasNext()) {
                    if (path.find(top.next()) == path.find(bot.next()))
                        percolates = true;
                }
            }
        }
    }

    public boolean isOpen(int row, int col) {
        return grid[col][row];
    }

    public boolean isFull(int row, int col) {
        return grid[col][row];
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return percolates;
    }

    private void checkSurroundings(int row, int col){
        int site = col + row*gridSize;
        for(int i = -1; i < 2; i += 2) {
            try {
                if(grid[col][row + i] == true) {
                    path.union(col + (row + i)*gridSize, site);
                }
            }
            catch(IndexOutOfBoundsException e) {

            }
        }
        for(int i = -1; i < 2; i += 2) {
            try {
                if(grid[col + i][row] == true) {
                    path.union((col + i) + row*gridSize, site);
                }
            }
            catch(IndexOutOfBoundsException e) {

            }
        }
    }

    public static void main(String[] args) {

    }
}