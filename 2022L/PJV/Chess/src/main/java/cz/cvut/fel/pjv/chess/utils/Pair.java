package cz.cvut.fel.pjv.chess.utils;

public class Pair {
    //class to represent a pair of numbers
    final int x;
    final int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair pair)) return false;
        return x == pair.x && y == pair.y;
    }
}
