package alg;

import java.util.LinkedList;

public class Signpost {
    final public int Id;
    LinkedList<Road> Roads = new LinkedList<>();

    public Signpost(int Id) {
        this.Id = Id;
    }

    void addRoad(Road road) {
        Roads.add(road);
    }
}
