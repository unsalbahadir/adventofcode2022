package solutions.day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Solution {

    public int getCountOfVisibleTrees(List<String> lines) {
        int count = 0;

        int[][] grid = buildGrid(lines);
        int[][] grid2 = new int[lines.size()][lines.get(0).length()];


        for (int i = 0; i < grid.length; i++) {
            if (i == 0 || i == grid.length - 1) { // edge rows
                count += grid.length;
                for (int j = 0; j < grid[i].length; j++) {
                    grid2[i][j] = 1;
                }
                continue;
            }
            for (int j = 0; j < grid[i].length; j++) {
                if (j == 0 || j == grid[i].length - 1) { // edge columns
                    count++;
                    grid2[i][j] = 1;
                    continue;
                }
                if (isVisible(grid, i, j)) {
                    grid2[i][j] = 1;
                    count++;
                }
            }
        }

//        for (int[] ints : grid2) {
//            System.out.println(Arrays.toString(ints));
//        }
        return count;
    }

    private int[][] buildGrid(List<String> lines) {
        int[][] grid = new int[lines.size()][lines.get(0).length()];

        int row = 0;
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                grid[row][i] = Character.getNumericValue(c);
            }
            row++;
        }

        return grid;
    }

    private boolean isVisible(int[][] grid, int i, int j) {
        return isVisibleLeft(grid, i, j) || isVisibleRight(grid, i, j) || isVisibleTop(grid, i, j) || isVisibleBottom(grid, i, j);
    }

    private boolean isVisibleLeft(int[][] grid, int i, int j) {
        int height = grid[i][j];

        for (int column = j - 1; column >= 0; column--) {
            if (grid[i][column] >= height) {
                return false;
            }
        }
        return true;
    }

    private boolean isVisibleRight(int[][] grid, int i, int j) {
        int height = grid[i][j];

        for (int column = j + 1; column < grid[0].length; column++) {
            if (grid[i][column] >= height) {
                return false;
            }
        }
        return true;
    }

    private boolean isVisibleTop(int[][] grid, int i, int j) {
        int height = grid[i][j];

        for (int row = i - 1; row >= 0; row--) {
            if (grid[row][j] >= height) {
                return false;
            }
        }
        return true;
    }

    private boolean isVisibleBottom(int[][] grid, int i, int j) {
        int height = grid[i][j];

        for (int row = i + 1; row < grid.length; row++) {
            if (grid[row][j] >= height) {
                return false;
            }
        }
        return true;
    }

    public int calculateScenicScore(List<String> lines) {
        int maxScenicScore = 1;

        int[][] grid = buildGrid(lines);
        int[][] grid2 = new int[lines.size()][lines.get(0).length()];


        for (int i = 0; i < grid.length; i++) {
            if (i == 0 || i == grid.length - 1) { // edge rows
                for (int j = 0; j < grid[i].length; j++) {
                    grid2[i][j] = 0;
                }
                continue;
            }
            for (int j = 0; j < grid[i].length; j++) {
                if (j == 0 || j == grid[i].length - 1) { // edge columns
                    grid2[i][j] = 0;
                    continue;
                }
                int scenicScore = getScenicScore(grid, i, j);
                grid2[i][j] = scenicScore;
                maxScenicScore = Math.max(maxScenicScore, scenicScore);
            }
        }

//        for (int[] ints : grid2) {
//            System.out.println(Arrays.toString(ints));
//        }
        return maxScenicScore;
    }

    private int getScenicScore(int[][] grid, int i, int j) {
        return getScenicScoreLeft(grid, i, j) * getScenicScoreRight(grid, i, j) * getScenicScoreTop(grid, i, j) * getScenicScoreBottom(grid, i, j);
    }

    private int getScenicScoreLeft(int[][] grid, int i, int j) {
        int height = grid[i][j];

        int count = 0;
        for (int column = j - 1; column >= 0; column--) {
            count++;
            if (grid[i][column] >= height) {
                break;
            }
        }
        return count;
    }

    private int getScenicScoreRight(int[][] grid, int i, int j) {
        int height = grid[i][j];

        int count = 0;
        for (int column = j + 1; column < grid[0].length; column++) {
            count++;
            if (grid[i][column] >= height) {
                break;
            }
        }
        return count;
    }

    private int getScenicScoreTop(int[][] grid, int i, int j) {
        int height = grid[i][j];

        int count = 0;
        for (int row = i - 1; row >= 0; row--) {
            count++;
            if (grid[row][j] >= height) {
                break;
            }
        }
        return count;
    }

    private int getScenicScoreBottom(int[][] grid, int i, int j) {
        int height = grid[i][j];

        int count = 0;
        for (int row = i + 1; row < grid.length; row++) {
            count++;
            if (grid[row][j] >= height) {
                break;
            }
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day8input.txt"));
        System.out.println(solution.getCountOfVisibleTrees(lines));
        System.out.println(solution.calculateScenicScore(lines));
    }
}
