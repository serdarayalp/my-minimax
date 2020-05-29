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

        int n = 3;
        int counter = 0;

        int x = 0;
        int y = 0;
        for (; x < n; x++, y++) {
            if (board[x][y] == player) {
                counter++;
            }
        }
        if (counter == n) {
            return true;
        }

        counter = 0;

        x = 0;
        y = n - 1;
        for (; x < 3; x++, y--) {
            if (board[x][y] == player) {
                counter++;
            }
        }

        if (counter == n) {
            return true;
        }

        for (x = 0; x < n; x++) {
            counter = 0;
            for (y = 0; y < n; y++) {
                if (board[x][y] == player) {
                    counter++;
                }
            }
            if (counter == n) {
                return true;
            }
        }

        for (y = 0; y < n; y++) {
            counter = 0;
            for (x = 0; x < n; x++) {
                if (board[x][y] == player) {
                    counter++;
                }
            }
            if (counter == n) {
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

    /*
        Beta ist die minimale Obergrenze der möglichen Lösungen
		Alpha ist die maximale Untergrenze der möglichen Lösungen

		Wenn also ein neuer Knoten als möglicher Weg zur Lösung in Betracht gezogen wird,
		kann er nur funktionieren, wenn er funktioniert:

	        alpha <= N <= beta

        wobei N die aktuelle Schätzung des Wertes des Knotens ist.

        Im weiteren Verlauf des Problems können wir davon ausgehen, dass der Bereich der möglichen Lösungen
        durch Min-Knoten (die eine obere Grenze setzen können) und Max-Knoten (die eine untere Grenze setzen können)
        eingeschränkt wird. Wenn wir uns durch den Suchbaum bewegen, nähern sich diese Schranken normalerweise
        immer mehr an:

        Irgendwann bei der Bewertung eines Knotens können wir feststellen, dass er eine der Grenzen so
        verschoben hat, dass es keine Überschneidung zwischen den Bereichen Alpha und Beta mehr gibt:

        Zum jetzigen Zeitpunkt wissen wir, dass dieser Knoten niemals zu einem Lösungsweg führen könnte,
        den wir in Betracht ziehen werden, so dass wir die Verarbeitung dieses Knotens einstellen könnten.
        Mit anderen Worten, wir hören auf, seine Kinder zu erzeugen und gehen zurück zu seinem Elternknoten.
        Für den Wert dieses Knotens sollten wir den von uns geänderten Wert, der über die andere Grenze hinausging,
        an den übergeordneten Knoten weitergeben.

        Max-Knoten können nur Einschränkungen für die untere Grenze vornehmen. Beachten Sie weiterhin,
        dass Werte, die im Baum nach unten weitergegeben werden, aber nicht auf dem
        Weg nach oben. Stattdessen wird der Endwert von Beta in einem Min-Knoten weitergegeben, um möglicherweise den
        Alpha-Wert seines übergeordneten Knotens zu ändern. Ebenso wird der Endwert von Alpha in einem Max-Knoten
        weitergegeben, um möglicherweise den Beta-Wert seines übergeordneten Knotens zu ändern.
     */
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

                score = alphaBeta(alpha, beta, Board.COMPUTER, depth + 1);

                minValue = Math.min(minValue, score);

                beta = Math.min(score, beta);
            }

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