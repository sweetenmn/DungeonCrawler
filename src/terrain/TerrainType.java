package terrain;

import javafx.scene.image.Image;

public enum TerrainType {
	EXIT(new Image("assets/dungeon/stairs_up.png")),
	FLOOR(new Image("assets/dungeon/floor_cobble.png")),
	WALL(new Image("assets/dungeon/white_wall.png"));	
	
	private Image sprite;
	
	TerrainType(Image sprite){
		this.sprite = sprite;
	}
	
	public Image getSprite(){
		return sprite;
	}

}
