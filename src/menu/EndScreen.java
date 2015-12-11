package menu;

import game.Drawable;
import game.graphics.Camera;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class EndScreen extends Drawable {
	int height, width;
	ArrayList<Image> clickables;
	public EndScreen(int width, int height){
		super();
		this.height = height;
		this.width = width;
		this.clickables = new ArrayList<>();
		addMenu();
	}
	
	private void addMenu(){
		contents.add(new MenuObject(MenuImage.GAME_OVER));
		contents.add(new MenuObject(MenuImage.NEW_GAME));
		contents.add(new MenuObject(MenuImage.LOAD_GAME));
	}
	
	public void render(Canvas canvas, Camera camera){
		this.draw(canvas, camera);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera){
		for (Drawable obj : contents) {
			obj.draw(canvas, camera);
		}
	}
}