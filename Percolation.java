import edu.princeton.cs.algs4.LinkedBag;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Iterator;

public class Percolation {

    private int gridSize;

    private boolean[][] grid;

    private int openSites;

    private boolean percolates;

    private WeightedQuickUnionUF path;

    //Used when checking whether percolation is present
    private LinkedBag<Integer> topRow, botRow;

    public Percolation(int n) {
        checkGrid(n);
        gridSize = n;
        grid = new boolean[n][n];
        openSites = 0;
        percolates = false;
        path = new WeightedQuickUnionUF(n*n);
        topRow = new LinkedBag<>();
        botRow = new LinkedBag<>();
    }

    private void checkGrid(int n) {
        if(n < 1)
            throw new java.lang.IllegalArgumentException();
    }

    public void open(int row, int col) {
        checkSite(row, col);

        //Only possible values for row and col are 0,0
        if(gridSize == 1) {
            grid[0][0] = true;
            percolates = true;
            openSites++;
        }

        else if(!isOpen(row, col)) {
            grid[col][row] = true;
            openSites++;

            //Keeps track of open sites in top and bottom row
            if(row == 0)
                topRow.add(col);
            else if(row == gridSize - 1)
                botRow.add(col + gridSize*(gridSize - 1));

            checkSurroundings(row, col);
        }
    }

    private void checkSite(int row, int col) {
        if(!siteExists(row, col))
            throw new java.lang.IndexOutOfBoundsException();
    }

    private boolean siteExists(int row, int col) {
        if(row < 0 || row > gridSize - 1)
            return false;
        else if(col < 0 || col > gridSize - 1)
            return false;
        return true;
    }

    //Creates union for sites adjacent if they exist and are open
    private void checkSurroundings(int row, int col){
        int site = col + row*gridSize;

        int[][] offset = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        int newRow, newCol;

        for(int i = 0; i < 4; i++) {
            newRow = row + offset[i][0];
            newCol = col + offset[i][1];

            if(siteExists(newRow, newCol)) {
                if(isOpen(newRow, newCol))
                    path.union(newCol + newRow*gridSize, site);
            }
        }

        if(!percolates)
            checkPercolation();
    }

    private void checkPercolation() {
        Iterator<Integer> bot = botRow.iterator();
        while(!percolates && bot.hasNext()) {
            int bSite = bot.next();
            int col = bSite % gridSize;
            percolates = isFull(gridSize - 1, col);
        }
    }

    public boolean isOpen(int row, int col) {
        checkSite(row, col);
        return grid[col][row];
    }

    public boolean isFull(int row, int col) {
        if(gridSize == 1)
            return isOpen(row, col);

        checkSite(row, col);

        if(topRow.size() > 0) {
            int siteRoot = path.find(col + row*gridSize);

            Iterator<Integer> top = topRow.iterator();

            while(top.hasNext()) {
                if(siteRoot == path.find(top.next()))
                    return true;
            }
        }
        return false;
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return percolates;
    }

    public static void main(String[] args) {

    }
}
