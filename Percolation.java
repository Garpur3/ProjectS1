import edu.princeton.cs.algs4.LinkedBag;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Iterator;

public class Percolation {

    private boolean[][] grid;

    private int gridSize;

    private int openSites;

    private WeightedQuickUnionUF path;

    private LinkedBag<Integer> topRow, botRow;

    private boolean percolates;

    public Percolation(int n) {
        checkGrid(n);
        gridSize = n;
        grid = new boolean[n][n];
        openSites = 0;
        path = new WeightedQuickUnionUF(n*n);
        topRow = new LinkedBag<Integer>();
        botRow = new LinkedBag<Integer>();
        percolates = false;
    }

    private void checkGrid(int n) {
        if(n < 1)
            throw new java.lang.IndexOutOfBoundsException();
    }

    private void checkSize(int row, int col) {
        if (row < 0 || col < 0 || row > gridSize - 1 || col > gridSize - 1)
            throw new java.lang.IndexOutOfBoundsException();
    }

    public void open(int row, int col) {
        checkSize(row, col);
        if(grid[col][row] == false) {
            grid[col][row] = true;
            openSites++;
            if(row == 0)
                topRow.add(col);

            else if(row == gridSize - 1)
                botRow.add(col + gridSize*(gridSize - 1));

            checkSurroundings(row, col);
        }
    }

    private void checkPercolation() {
        if(topRow.size() > 0 && botRow.size() > 0) {
            Iterator<Integer> top, bot;
            top = topRow.iterator();
            while (top.hasNext()) {
                bot = botRow.iterator();
                int tSite = top.next();
                while (bot.hasNext()) {
                    int bSite = bot.next();
                    if (path.find(tSite) == path.find(bSite))
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
        checkPercolation();
    }
}
