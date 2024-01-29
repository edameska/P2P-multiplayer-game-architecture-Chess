package main;

import java.awt.Point;

public class Move {
    private Point source;
    private Point destination;

    public Move(Point source, Point destination) {
        this.source = source;
        this.destination = destination;
    }

    public Point getSource() {
        return source;
    }

    public Point getDestination() {
        return destination;
    }
}