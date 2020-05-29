package de.game.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Board {

    public static final int EMPTY = 0;
    public static final int COMPUTER = 1;
    public static final int USER = 2;

    List<Point> availablePoints;
    Scanner scan = new Scanner(System.in);

    int[][] board = new int[3][3];

    List<PositionScore> childrenScoreList = new ArrayList<>();

    /*
    Setzen Sie diesen Wert auf irgendeinen Wert, wenn Sie eine bestimmte
    Tiefenbegrenzung für die Suche wünschen. */
    int uptoDepth = -1;

    public void displayBoard() {

        System.out.println();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void resetBoard() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                board[i][j] = 0;
            }
        }
    }

    public void doMove(Point point, int player) {
        board[point.x][point.y] = player;
    }

    public boolean isGameOver() {
        // Das Spiel ist zu Ende, wenn jemand gewonnen hat,
        // oder das Brett voll ist (Unentschieden)
        return isWon(Board.COMPUTER) ||
                isWon(Board.USER) ||
                getAvailableStates().isEmpty();
    }

    public boolean isWon(int player) {
        int b00 = board[0][0];
        int b11 = board[1][1];
        int b22 = board[2][2];

        int b02 = board[0][2];
        int b20 = board[2][0];

        if ((b00 == b11 && b00 == b22 && b00 == player) ||
                (b02 == b11 && b02 == b20 && b02 == player)) {
            return true;
        }

        for (int i = 0; i < 3; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == player) ||
                    (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == player)) {
                return true;
            }
        }

        return false;
    }

    public List<Point> getAvailableStates() {

        availablePoints = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }

        return availablePoints;
    }

    public Point getBestMove() {

        int MAX = Integer.MIN_VALUE;
        int bestPosition = -1;

        for (int i = 0; i < childrenScoreList.size(); i++) {
            int score = childrenScoreList.get(i).score;
            if (MAX < score) {
                MAX = score;
                bestPosition = i;
            }
        }

        return childrenScoreList.get(bestPosition).point;
    }

    public int alphaBeta(int alpha, int beta, int player, int depth) {

        if (beta <= alpha) {
            /*
            Wenn Integer.MIN_VALUE oder Integer.MAX_VALUE für einen Knoten zurückgegeben wird,
            wissen wir, dass eine Alpha-Beta-Pruning vorgenommen wurde und wir möchten
            Geschwister nicht mehr auswerten.
            */
            if (player == Board.COMPUTER) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }

        if (depth == uptoDepth || isGameOver()) {
            return evaluateBoard();
        }

        List<Point> pointsAvailable = getAvailableStates();

        if (pointsAvailable.isEmpty()) return 0;

        if (depth == 0) childrenScoreList.clear();

        int maxValue = Integer.MIN_VALUE;
        int minValue = Integer.MAX_VALUE;

        for (Point point : pointsAvailable) {

            int score = 0;

            if (player == Board.COMPUTER) {

                doMove(point, Board.COMPUTER);

                score = alphaBeta(alpha, beta, Board.USER, depth + 1);

                maxValue = Math.max(maxValue, score);

                alpha = Math.max(score, alpha);

                if (depth == 0) {
                    childrenScoreList.add(new PositionScore(score, point));
                }

            } else if (player == Board.USER) {

                doMove(point, Board.USER);

                score = alphaBeta(alpha, beta, 1, depth + 1);

                minValue = Math.min(minValue, score);

                beta = Math.min(score, beta);
            }
            //reset board
            board[point.x][point.y] = 0;

            //If a pruning has been done, don't evaluate the rest of the sibling states
            if (score == Integer.MAX_VALUE || score == Integer.MIN_VALUE) break;
        }

        return player == Board.COMPUTER
                ? maxValue
                : minValue;
    }

    public int evaluateBoard() {

        int score = 0;

        int empty = 0;
        int computer = 0;
        int user = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Board.USER) {
                    user++;
                } else if (board[i][j] == Board.COMPUTER) {
                    computer++;
                } else {
                    empty++;
                }
            }
            score = score + changeScore(computer, user);
        }

        for (int j = 0; j < 3; ++j) {

            empty = 0;
            computer = 0;
            user = 0;

            for (int i = 0; i < 3; ++i) {
                if (board[i][j] == Board.COMPUTER) {
                    computer++;
                } else if (board[i][j] == Board.USER) {
                    user++;
                } else {
                    empty++;
                }
            }
            score = score + changeScore(computer, user);
        }

        empty = 0;
        computer = 0;
        user = 0;

        for (int i = 0, j = 0; i < 3; ++i, ++j) {
            if (board[i][j] == Board.COMPUTER) {
                computer++;
            } else if (board[i][j] == Board.USER) {
                user++;
            } else {
                empty++;
            }
        }

        score = score + changeScore(computer, user);

        empty = 0;
        computer = 0;
        user = 0;

        for (int i = 2, j = 0; i >= 0; --i, ++j) {
            if (board[i][j] == Board.COMPUTER) {
                computer++;
            } else if (board[i][j] == Board.USER) {
                user++;
            } else {
                empty++;
            }
        }

        score = score + changeScore(computer, user);

        return score;
    }

    private int changeScore(int X, int O) {
        int change;
        if (X == 3) {
            change = 100;
        } else if (X == 2 && O == 0) {
            change = 10;
        } else if (X == 1 && O == 0) {
            change = 1;
        } else if (O == 3) {
            change = -100;
        } else if (O == 2 && X == 0) {
            change = -10;
        } else if (O == 1 && X == 0) {
            change = -1;
        } else {
            change = 0;
        }
        return change;
    }

}