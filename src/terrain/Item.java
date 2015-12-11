package terrain;

import javafx.scene.image.Image;
import game.Drawable;

public class Item extends Drawable{
	private ItemType type;
	
	public Item(ItemType type){
		this.type = type;
		this.sprite = type.getAvatar();
	}
	
	
	
	public ItemType getType(){
		return type;
	}
	
	public Image getSprite(){
		return sprite;
	}
	

	
}