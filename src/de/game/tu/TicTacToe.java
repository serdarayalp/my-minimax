package de.game.tu;

import de.game.minimax.Game;
import de.game.tictactoe.Point;
import de.game.tictactoe.PointsAndScores;

import java.util.List;

/**
 * This class implements and evaluates game situations of a TicTacToe game.
 */
public class TicTacToe {

    /**
     * Returns an evaluation for player at the current board state.
     * Arbeitet nach dem Prinzip der Alphabeta-Suche. Works with the principle of Alpha-Beta-Pruning.
     *
     * @param board  current Board object for game situation
     * @param player player who has a turn
     * @return rating of game situation from player's point of view
     **/
    public static int alphaBeta(Board board, int player) {
        // TODO
        return TicTacToe.alphaBetaMinimax(board, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
    }

    public static int alphaBetaMinimax(Board board, int alpha, int beta, int player) {

        if (beta <= alpha) {
            if (player == 1) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }

        /*
        if (depth == uptoDepth || isGameOver()) {
            return evaluateBoard();
        }
        */


        Iterable<Position> positions = board.validMoves();

        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;

        for (Position position : positions) {

             int currentScore = 0;

            if (player == -1) {
                board.doMove(position, -1);
                currentScore = alphaBetaMinimax(board, alpha, beta, 1);
                maxValue = Math.max(maxValue, currentScore);

                alpha = Math.max(currentScore, alpha);

            } else if (player == 1) {
                board.doMove(position, 1);
                currentScore = alphaBetaMinimax(board, alpha, beta , -1);
                minValue = Math.min(minValue, currentScore);

                beta = Math.min(currentScore, beta);
            }

            //If a pruning has been done, don't evaluate the rest of the sibling states
            if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
        }

        return player == 1 ? maxValue : minValue;
    }



    /*public static int scorePlayer(Board board, int player, int alpha, int beta) {

        *//*if (board.isGameWon()) {
            return player * board.score();
        }*//*

        for (int boardPlayer : Board.PLAYER) {
            board.doMove(pos, player);
            int score = -scorePlayer(board, -boardPlayer, -beta, -alpha);
            board.undoMove(pos);
            if (score > alpha) {
                alpha = score;
                if (alpha >= beta) break;
            }
        }
        return alpha;
    }*/


    /**
     * Vividly prints a rating for each currently possible move out at System.out.
     * (from player's point of view)
     * Uses Alpha-Beta-Pruning to rate the possible moves.
     * formatting: See "Beispiel 1: Bewertung aller Zugmöglichkeiten" (Aufgabenblatt 4).
     *
     * @param board  current Board object for game situation
     * @param player player who has a turn
     **/
    public static void evaluatePossibleMoves(Board board, int player) {
        // TODO
    }

    public static void main(String[] args) {
        Board b = new Board(3);
        b.print();

        b.setField(new Position(0, 0), -1);
        b.setField(new Position(1, 1), 1);

        System.out.println(TicTacToe.alphaBeta(b, -1));
    }
}

