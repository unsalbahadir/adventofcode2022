package solutions.day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Solution {

    private enum Outcome {
        LOSS,
        DRAW,
        WIN
    }

    private enum RockPaperScissors {
        ROCK,
        PAPER,
        SCISSORS
    }

    private final Map<RockPaperScissors, Integer> scoresOfPlays = Map.of(
            RockPaperScissors.ROCK, 1,
            RockPaperScissors.PAPER, 2,
            RockPaperScissors.SCISSORS, 3
    );


    private final Map<Outcome, Integer> scoresOfOutcomes = Map.of(
            Outcome.LOSS, 0,
            Outcome.DRAW, 3,
            Outcome.WIN, 6
    );

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

    public int getScore2(List<String> lines) {
        int totalScore = 0;

        for (String line : lines) {
            String[] plays = line.split(" ");
            String opponentPlay = plays[0];
            String playerPlay = plays[1];

            totalScore += getScoreOfPlay2(opponentPlay, playerPlay);
        }
        return totalScore;
    }

    private int getScoreOfPlay2(String opponentPlay, String playerPlay) {
        int score = 0;
        Outcome outcome;
        if (playerPlay.equals("X")) {
            outcome = Outcome.LOSS;
        } else if (playerPlay.equals("Y")) {
            outcome = Outcome.DRAW;
        } else {
            outcome = Outcome.WIN;
        }
        score += scoresOfOutcomes.get(outcome);

        RockPaperScissors playerShouldPlay;
        if (opponentPlay.equals("A")) {
            if (outcome.equals(Outcome.LOSS)) {
                playerShouldPlay = RockPaperScissors.SCISSORS;
            } else if (outcome.equals(Outcome.DRAW)) {
                playerShouldPlay = RockPaperScissors.ROCK;
            } else {
                playerShouldPlay = RockPaperScissors.PAPER;
            }
        } else if (opponentPlay.equals("B")) {
            if (outcome.equals(Outcome.LOSS)) {
                playerShouldPlay = RockPaperScissors.ROCK;
            } else if (outcome.equals(Outcome.DRAW)) {
                playerShouldPlay = RockPaperScissors.PAPER;
            } else {
                playerShouldPlay = RockPaperScissors.SCISSORS;
            }
        } else {
            if (outcome.equals(Outcome.LOSS)) {
                playerShouldPlay = RockPaperScissors.PAPER;
            } else if (outcome.equals(Outcome.DRAW)) {
                playerShouldPlay = RockPaperScissors.SCISSORS;
            } else {
                playerShouldPlay = RockPaperScissors.ROCK;
            }
        }

        score += scoresOfPlays.get(playerShouldPlay);

        return score;
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day2input.txt"));
        System.out.println(solution.getScore(lines));
        System.out.println(solution.getScore2(lines));
    }
}
