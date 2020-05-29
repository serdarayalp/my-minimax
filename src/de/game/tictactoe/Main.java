package de.game.tictactoe;

import java.util.Random;

// http://www.codebytes.in/2014/11/alpha-beta-pruning-minimax-algorithm.html
// http://web.cs.ucla.edu/~rosen/161/notes/alphabeta.html
public class Main {

    public static void main(String[] args) {

        Board b = new Board();

        Random rand = new Random();

        b.displayBoard();

        /*
        System.out.println("Wer soll zuerst starten: 1 - Computer oder 2 - User: ");
        int choice = b.scan.nextInt();

        if (choice == 1) {
            Point p = new Point(rand.nextInt(3), rand.nextInt(3));
            b.placeAMove(p, 1);
            b.displayBoard();
        }
         */

        Point p = new Point(rand.nextInt(3), rand.nextInt(3));
        b.doMove(p, Board.COMPUTER);
        b.displayBoard();

        while (!b.isGameOver()) {

            System.out.println("[Ihr Zug]: X Y");

            Point userMove = new Point(b.scan.nextInt(), b.scan.nextInt());

            b.doMove(userMove, Board.USER);
            b.displayBoard();

            if (b.isGameOver()) break;

            b.alphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE, Board.COMPUTER, 0);

            for (PositionScore pas : b.childrenScoreList) {
                System.out.println("Point: " + pas.point + " Score: " + pas.score);
            }

            b.doMove(b.getBestMove(), Board.COMPUTER);

            b.displayBoard();
        }

        if (b.isWon(1)) {
            System.out.println("Sie haben verloren.");
        } else if (b.isWon(2)) {
            System.out.println("Sie haben gewonnen.");
        } else {
            System.out.println("Unentschieden!");
        }
    }
}
