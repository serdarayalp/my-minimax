package de.game.tu;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Stack;

import static java.lang.Math.abs;

/**
 * This class represents a generic TicTacToe game board.
 */
public class Board {

    private int n;
    private int nFreeFields;
    private int nNotFreeFields;

    private int[][] board;
    public static final int[] PLAYER = {-1, 1};

    private final static String PLAYER_O = "O";
    private final static String NO_PLAYER = "0";
    private final static String PLAYER_X = "X";

    /**
     * Creates Board object, am game board of size n * n with 1<=n<=10.
     */
    public Board(int n) {

        // TODO

        if (n < 1 || n > 10) {
            throw new InputMismatchException("Dimension des Spielbretts: 1 <= n <= 10");
        }

        if (n < 3) {
            throw new InputMismatchException("Für diese Dimension kann kein Spielbrett erstellt werden.");
        }

        this.n = n;
        this.nFreeFields = n * n;
        this.nNotFreeFields = 0;

        this.board = new int[n][n];
    }

    /**
     * @return length/width of the Board object
     */
    public int getN() {
        return n;
    }

    /**
     * @return number of currently free fields
     * <p>
     * Aus Effizienzgründen sollte die Anzahl der freien Felder oder die Anzahl der gesetzten
     * Steine in einer Variable gespeichert werden, die in der Methode setField() aktualisiert
     * wird. Dann müssen nicht bei jedem Aufruf von nFreeFields() alle Felder durchsucht
     * werden.
     */
    public int nFreeFields() {
        // TODO
        return nFreeFields;
    }

    /**
     * @return number of fields that are currently not free
     * <p>
     * Aus Effizienzgründen sollte die Anzahl der freien Felder oder die Anzahl der gesetzten
     * Steine in einer Variable gespeichert werden, die in der Methode setField() aktualisiert
     * wird. Dann müssen nicht bei jedem Aufruf von nFreeFields() alle Felder durchsucht
     * werden.
     */
    public int nNotFreeFields() {
        return nNotFreeFields;
    }

    /**
     * @return token at position pos
     * <p>
     * Die Methoden setField() und getField() müssen eine InputMismatchException
     * werfen, wenn die Eingabeargumente keine validen Werte haben.
     */
    public int getField(Position pos) throws InputMismatchException {
        // TODO
        if (pos.x < 0 || pos.x >= n ||
                pos.y < 0 || pos.y >= n) {
            throw new InputMismatchException("Eingabeargument hat keinen validen Wert.");
        }
        return board[pos.x][pos.y];
    }

    /**
     * Sets the specified token at Position pos.
     * <p>
     * Werte für Token
     * -1  -> Spielstein o
     * 0   -> freies Feld
     * 1   -> Spielstein x
     * <p>
     * Aus Effizienzgründen sollte die Anzahl der freien Felder oder die Anzahl der gesetzten
     * Steine in einer Variable gespeichert werden, die in der Methode setField() aktualisiert
     * wird. Dann müssen nicht bei jedem Aufruf von nFreeFields() alle Felder durchsucht
     * <p>
     * Die Methoden setField() und getField() müssen eine InputMismatchException
     * werfen, wenn die Eingabeargumente keine validen Werte haben.
     */
    public void setField(Position pos, int token) throws InputMismatchException {
        // TODO
        if (token < -1 || token > 1) {
            throw new InputMismatchException("Eingabeargument hat keinen validen Wert.");
        }

        board[pos.x][pos.y] = token;

        if (token == 0) {
            nFreeFields = nFreeFields + 1;
            nNotFreeFields = nNotFreeFields - 1;
        } else { // -1 oder 1
            nFreeFields = nFreeFields - 1;
            nNotFreeFields = nNotFreeFields + 1;
        }
    }

    /**
     * Places the token of a player at Position pos.
     * <p>
     * Führt einen Spielzug von Spielerin player (-1 oder 1) auf Feld pos aus.
     */
    public void doMove(Position pos, int player) {
        // TODO
        this.setField(pos, player);
    }

    /**
     * Clears board at Position pos.
     * <p>
     * Macht den Zug auf Feld pos rückgängig, indem sie das Feld pos leert.
     * <p>
     * Die Methode undoMove() braucht kein Gedächtnis zu haben.
     */
    public void undoMove(Position pos) {
        // TODO
        setField(pos, 0);
    }

    /**
     * @return true if game is won, false if not
     * <p>
     * Wurde das Spiel durch den letzten Zug per doMove() gewonnen?
     * <p>
     * Sie sollten auch überlegen, wie Anfragen von isGameWon() effizient beantwortet werden können.
     */
    public boolean isGameWon() {
        // TODO
        return false;
    }

    /**
     * @return set of all free fields as some Iterable object
     * <p>
     * Gibt alle Felder, auf die gezogen werden kann, zurück. Die Methode validMoves() braucht nicht zu prüfen,
     * ob das Spiel bereits beendet ist. Sie kann einfach alle Positionen freier Felder zurückgeben.
     */
    public Iterable<Position> validMoves() {
        // TODO
        List<Position> freeFields = new ArrayList<>();
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (board[x][y] == 0) {
                    freeFields.add(new Position(x, y));
                }
            }
        }

        return freeFields;
    }

    /**
     * Outputs current state representation of the Board object.
     * Practical for debugging.
     */
    public void print() {
        // TODO
        System.out.println();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {

                int field = board[i][j];
                String convertedField;

                if (field == -1) {
                    convertedField = PLAYER_O;
                } else if (field == 1) {
                    convertedField = PLAYER_X;
                } else {
                    convertedField = NO_PLAYER;
                }

                System.out.print(convertedField + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Board b = new Board(3);
        b.print();

        b.setField(new Position(0, 0), -1);
        b.setField(new Position(1, 1), 1);

        b.print();

        Iterable<Position> positions = b.validMoves();
        for (Position position : positions) {
            System.out.print(position + " ");
        }
    }

}

