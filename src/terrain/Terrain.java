package terrain;

import game.Drawable;
import game.Position;

public class Terrain extends Drawable {
	
	
	public Terrain(TerrainType type, Position p){
		sprite = type.getSprite();
		position = p;
	}

}
