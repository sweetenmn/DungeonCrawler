package terrain;

import javafx.scene.image.Image;

public enum ItemType{
	HEALTH(10){
		@Override
		public Image getSprite(){
			return new Image("assets/dungeon/health_potion.png");
		};
	},
	EXPERIENCE(10){
		@Override
		public Image getSprite(){
			return new Image("assets/dungeon/exp_potion.png");
		};
	};
	
		private int value;
		
		public abstract Image getSprite();
		
		public int getValue(int level){return value + ((level - 1) * 3);}
				
		ItemType(int value){
			this.value = value;
		}
}