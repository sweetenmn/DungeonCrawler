package terrain;

import game.Drawable;
import game.Position;
import javafx.scene.image.Image;

/**
 * Created by josephbenton on 9/13/15.
 */
public class Wall extends Drawable {

    public Wall(Position p) {
        this.position = p;
        this.sprite = new Image("assets/dungeon/white_wall.png");
    }
}