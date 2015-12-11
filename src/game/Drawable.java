package game;

import game.graphics.Camera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;

/**
 * Created by josephbenton on 9/13/15.
 */

public abstract class Drawable{
    protected Image sprite;
    protected Position position;
    protected ArrayList<Drawable> contents;

    public Drawable(){
        this.contents = new ArrayList<Drawable>();
    }

    public void draw(Canvas canvas, Camera camera){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double x = position.getX() * 32 - camera.getPosition().getX();
        double y = position.getY() * 32 - camera.getPosition().getY();
        gc.drawImage(sprite, x, y, 32, 32);       
    }

    public Position getPosition(){return position;}
    public void setPosition(int x, int y){position = new Position(x, y);}
    public void setPosition(Position p){position = p;}
}
