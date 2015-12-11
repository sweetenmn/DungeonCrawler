package terrain;

import game.Drawable;
import game.Position;
import javafx.scene.image.Image;

/**
 * Created by josephbenton on 9/13/15.
 */
public class Exit extends Drawable{
    public Exit(Position p){
        this.position = p;
        sprite = new Image("assets/dungeon/stairs_up.png");
    }
}
