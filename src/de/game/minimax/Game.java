package de.game.minimax;

public class Game {

    private int[] values;
    public static final int[] moves = {-1, 1};
    private int first, last;

    public Game(int[] values) {
        this.values = values;
        first = 0;
        last = values.length - 1;
    }

    public boolean isFinal() {
        return last - first == 1;
    }

    public int score() {
        return values[first] - values[last];
    }

    public void doMove(int move, String player) {
        if (move < 0) {
            System.out.println(player + " - MOVE: FIRST: " + values[first]);
            first++;
        } else {
            System.out.println(player + " - MOVE: LAST: " + values[last]);
            last--;
        }
    }

    public void undoMove(int move, String player) {
        if (move < 0) {
            //System.out.println(player + " - UNDO-MOVE: FIRST: " + values[first]);
            first--;
        } else {
            //System.out.println(player + " - UNDO-MOVE: LAST: " + values[last]);
            last++;
        }
    }
}
