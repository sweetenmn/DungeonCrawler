package terrain;

import game.Drawable;
import game.Position;
import javafx.scene.image.Image;

/**
 * Created by josephbenton on 9/20/15.
 */
public class Floor extends Drawable{
    public Floor(Position p){
        this.position = p;
        this.sprite = new Image("assets/dungeon/floor_cobble.png");
    }
}
