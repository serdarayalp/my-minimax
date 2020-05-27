package de.game.minimax;

public class MinMaxBestDifference {

    private Game game;

    private static int player_a_counter = 0;
    private static int player_b_counter = 0;

    public MinMaxBestDifference(int[] values) {
        game = new Game(values);
    }

    public int scorePlayerA() {

        if (game.isFinal()) { // Lösung (Ende) erreicht?
            System.out.println("SCORE: " + game.score());
            System.out.println("");
            return game.score();
        }

        int maxScore = Integer.MIN_VALUE;

        for (int move : Game.moves) { // alle Möglichkeiten durchlaufen

            ++player_a_counter;
            System.out.println();
            System.out.println("**************** SCHLEIFE - PLAYER - A: " + player_a_counter);

            game.doMove(move, "PLAYER-A"); // Zug ausführen
            int score = scorePlayerB(); // alternierend rekursiver Aufruf
            game.undoMove(move,"PLAYER-A"); // Zug rückgängig machen
            if (score > maxScore) // Maximum der Bewertungen
                maxScore = score;
        }
        return maxScore;
    }

    public int scorePlayerB() {

        if (game.isFinal()) {
            System.out.println("SCORE: " + game.score());
            System.out.println("");
            return game.score();
        }

        int minScore = Integer.MAX_VALUE;

        for (int move : Game.moves) {

            ++player_b_counter;
            System.out.println();
            System.out.println("**************** SCHLEIFE - PLAYER - B: " + player_b_counter);

            game.doMove(move, "PLAYER-B");
            int score = scorePlayerA();
            game.undoMove(move, "PLAYER-B");
            if (score < minScore)
                minScore = score;
        }
        return minScore;
    }

    public static void main(String[] args) {

        int[] values = {2, 8, 3, 5, 4, 1};

        MinMaxBestDifference bd = new MinMaxBestDifference(values);

        int bestScoreA = bd.scorePlayerA();
        System.out.println(bestScoreA);

        /*int bestScoreB = bd.scorePlayerB();
        System.out.println(bestScoreB);*/
    }
}
