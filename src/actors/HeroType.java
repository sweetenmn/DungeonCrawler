package actors;

import game.Direction;
import javafx.scene.image.Image;


public enum HeroType {
    ROGUE(30, 10, 7, 3) {



		@Override
		public Image getSpriteDirection(Direction dir){
			String img = "assets/sprites/rogue_" + getEnd(dir);
			return new Image(img);
		}

		@Override
		public Image getCombatAvatar(){
			return new Image("assets/combat/rogue_combat.png");
		}

		@Override
		public Image getPortrait(){
			return new Image("assets/sprites/rogue_portrait.png");
		}

		@Override
		public String getAbout() {
			return "Rogues are balanced heroes\nand earn a little\nbonus XP.";
		}
    },
    MAGE(25, 7, 10, 5){
		@Override
		public Image getSpriteDirection(Direction dir){
			String img = "assets/sprites/mage_" + getEnd(dir);
			return new Image(img);
		}

		@Override
		public Image getCombatAvatar(){
			return new Image("assets/combat/mage_combat.png");
		}

		@Override
		public Image getPortrait(){
			return new Image("assets/sprites/mage_portrait.png");
		}

		@Override
		public String getAbout() {
			return "Mages are glass cannons:\nThey hit with a high\ncritical strike chance,"
					+" but\n can't take much damage.\nThey earn bonus XP.";
		}
    },
    KNIGHT(40, 11, 1, 0){
		@Override
		public Image getSpriteDirection(Direction dir){
			String img = "assets/sprites/knight_" + getEnd(dir);
			return new Image(img);
		}

		@Override
		public Image getCombatAvatar(){
			return new Image("assets/combat/knight_combat.png");
		}

		@Override
		public Image getPortrait(){
			return new Image("assets/sprites/knight_portrait.png");
		}
		
		@Override
		public String getAbout(){
			return "Knights have more health\nand a stronger base attack\nthan the other classes.";
		}
    };
    private int maxHealth, attack, luck, intellect;
    
   

    public abstract Image getSpriteDirection(Direction dir);
    public abstract Image getCombatAvatar();
    public abstract Image getPortrait();
    public abstract String getAbout();

    public int getMaxHealth(){return maxHealth;}
    public int getAttack(){return attack;}
    public int getLuck(){return luck;}
    public int getIntel(){return intellect;}
    
    private static String getEnd(Direction dir){
    	String end = "";
		switch(dir){
		case DOWN:
			end = "down.png";
			break;
		case LEFT:
			end = "left.png";
			break;
		case RIGHT:
			end = "right.png";
			break;
		case UP:
			end = "up.png";
			break;
		}
		return end;    	
    }

    HeroType(int health, int attack,  int luck, int intellect) {
        this.maxHealth = health;
        this.attack = attack;
        this.luck = luck;
        this.intellect = intellect;
    }
}