package menu;

import game.Drawable;
import game.graphics.Camera;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class StartScreen extends Drawable {
	int height, width;
	ArrayList<Image> clickables;
	public StartScreen(int width, int height){
		super();
		this.height = height;
		this.width = width;
		this.clickables = new ArrayList<>();
		addMenu();
	}
	
	private void addMenu(){
		contents.add(new MenuObject(MenuImage.TITLE));
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