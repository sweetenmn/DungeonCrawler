package util;

import java.util.Random;

/**
 * Created by josephbenton on 9/13/15.
 */
public class Dice{
    int sides;
    Random rand;

    public Dice(int sides){
        this.sides = sides;
        rand = new Random();
    }

    public int roll(){
        return (int) (rand.nextDouble() * sides);
    }
}