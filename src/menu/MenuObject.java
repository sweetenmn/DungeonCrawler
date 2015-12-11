package menu;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import game.Drawable;
import game.Position;
import game.graphics.Camera;

public class MenuObject extends Drawable{
	MenuImage type;
	public MenuObject(MenuImage type){
		this.type = type;
		this.position = getPosition(type);
		sprite = new Image(type.image());
	}
	
	private Position getPosition(MenuImage type){
		Position p = null;
		switch(type) {
            case LOAD_GAME:
                p = new Position(0, 5);
                break;
            case NEW_GAME:
                p = new Position(0, 3);
                break;
            case TITLE:
                p = new Position(0, 0);
                break;
            case GAME_OVER:
                p = new Position(250, 150);
                break;
        }
		return p;
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera){
		GraphicsContext gc = canvas.getGraphicsContext2D();
        double x = position.getX() * sprite.getWidth();
        double y = position.getY() * sprite.getHeight();
        gc.drawImage(sprite, x, y, sprite.getWidth(), sprite.getHeight());
	}	
}