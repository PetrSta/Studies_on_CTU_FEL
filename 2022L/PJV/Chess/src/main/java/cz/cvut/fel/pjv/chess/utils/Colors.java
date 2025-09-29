package cz.cvut.fel.pjv.chess.utils;

public enum Colors {
    //enum to represent piece color
    blackColor("black"),
    whiteColor("white");

    private final String color;

    Colors(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
