package solutions.day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Solution {

    private enum Outcome {
        LOSS,
        DRAW,
        WIN
    }


    public int getScore(List<String> lines) {
        int totalScore = 0;

        for (String line : lines) {
            String[] plays = line.split(" ");
            String opponentPlay = plays[0];
            String playerPlay = plays[1];

            totalScore += getScoreOfPlay(opponentPlay, playerPlay);
        }
        return totalScore;
    }

    private int getScoreOfPlay(String opponentPlay, String playerPlay) {
        int score = 0;
        Outcome outcome = Outcome.LOSS;
        if (playerPlay.equals("X")) {
            score++;
            if (opponentPlay.equals("A")) {
                outcome = Outcome.DRAW;
            } else if (opponentPlay.equals("C")) {
                outcome = Outcome.WIN;
            }
        } else if (playerPlay.equals("Y")) {
            score += 2;
            if (opponentPlay.equals("A")) {
                outcome = Outcome.WIN;
            } else if (opponentPlay.equals("B")) {
                outcome = Outcome.DRAW;
            }
        } else {
            score += 3;
            if (opponentPlay.equals("B")) {
                outcome = Outcome.WIN;
            } else if (opponentPlay.equals("C")) {
                outcome = Outcome.DRAW;
            }
        }

        if (outcome.equals(Outcome.DRAW)) {
            score += 3;
        } else if (outcome.equals(Outcome.WIN)) {
            score += 6;
        }

        return score;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day2input.txt"));
        System.out.println(solution.getScore(lines));
    }
}
