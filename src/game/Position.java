package game;

/**
 * Created by josephbenton on 9/13/15.
 */
public class Position{
    int x;
    int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getDistanceTo(Position p){
        return Math.abs(this.x - p.x) + Math.abs(this.y - p.y);
    }

    public boolean equals(Position other) {
        return this.getDistanceTo(other) == 0;
    }
}