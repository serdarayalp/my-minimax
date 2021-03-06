package de.game.minimax;

public class AlphaBetaBestDifference {

    private Game game;

    private static int player_a_counter = 0;
    private static int player_b_counter = 0;

    public AlphaBetaBestDifference(int[] values) {
        game = new Game(values);
    }

    public int scorePlayerA(int alpha, int beta) {
        if (game.isFinal()) {
            return game.score();
        }
        for (int move : Game.moves) {
            game.doMove(move, "PLAYER-A");
            int score = scorePlayerB(alpha, beta);
            game.undoMove(move, "PLAYER-A");

            System.out.println("PLAYER-A: AKTUELLE WERTE: SCORE: " + score + ", ALPHA: " + alpha + ", BETA: " + beta);

            if (score > alpha) {
                System.out.println("PLAYER-A: SCORE > ALPHA");
                alpha = score;
                System.out.println("PLAYER-A: ALPHA ERSETZT DURCH SCORE");
                if (alpha >= beta) {
                    System.out.println("PLAYER-A: ALPHA >= BETA >>>>>>>> BREAK");
                    break;
                }
            }
        }
        return alpha;
    }

    public int scorePlayerB(int alpha, int beta) {
        if (game.isFinal()) {
            return game.score();
        }
        for (int move : Game.moves) {
            game.doMove(move, "PLAYER-B");
            int score = scorePlayerA(alpha, beta);
            game.undoMove(move, "PLAYER-B");

            System.out.println("PLAYER-B: AKTUELLE WERTE: SCORE: " + score + ", ALPHA: " + alpha + ", BETA: " + beta);

            if (score < beta) {
                System.out.println("PLAYER-B: SCORE < BETA");
                beta = score;
                System.out.println("PLAYER-B: BETA ERSETZT DURCH SCORE");
                if (beta <= alpha) {
                    System.out.println("PLAYER-B: BETA <= ALPHA >>>>>>>> BREAK");
                    break;
                }
            }
        }
        return beta;
    }

    public int alphaBetaNegamax() {
        return scorePlayer(1, -Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public int scorePlayer(int player, int alpha, int beta) {

        System.out.println("++++++++++++++++++++++++ AKTUELLE WERTE: ALPHA: " + alpha + ", BETA: " + beta);

        if (game.isFinal()) {
            return player * game.score();
        }
        for (int move : Game.moves) {

            game.doMove(move, "PLAYER (" + player + ")");
            int score = -scorePlayer(-player, -beta, -alpha);
            System.out.println("SCORE ist: " + score + " und ein UNDO für Player: " + player);
            game.undoMove(move, "PLAYER (" + player + ")");


            System.out.println("*************** BEGIN ******************");
            System.out.println("PLAYER: "  + player);
            System.out.println("AKTUELLE WERTE: SCORE: " + score + ", ALPHA: " + alpha + ", BETA: " + beta);
            if (score > alpha) {
                System.out.println("SCORE > ALPHA");
                alpha = score;
                System.out.println("ALPHA ERSETZT DURCH SCORE");
                System.out.println("NEUE WERTE: SCORE: " + score + ", ALPHA: " + alpha + ", BETA: " + beta);
                if (alpha >= beta) {
                    System.out.println("ALPHA >= BETA >>>>>>>> BREAK bzw. PRUNING");
                    break;
                }
                System.out.println("*************** ENDE *****************");
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>> ALPHA ZURÜCKGEBEN: " + alpha);

        return alpha;
    }

    public static void main(String[] args) {

        int[] values = {2, 8, 3, 5, 4, 1};

        AlphaBetaBestDifference bd = new AlphaBetaBestDifference(values);

        int bestScore = bd.alphaBetaNegamax();
        System.out.println(bestScore);

        /*
        int bestScoreA = bd.scorePlayerA(Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println(bestScoreA);*/

        /*int bestScoreB = bd.scorePlayerB();
        System.out.println(bestScoreB);*/
    }
}
