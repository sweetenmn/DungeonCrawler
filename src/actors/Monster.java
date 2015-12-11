package actors;

import javafx.scene.image.Image;
import util.Dice;

/**
 * Created by Joseph on 9/13/2015.
 */
public class Monster extends Actor {
    private int expValue;
    private MonsterType type;
   

    public Monster(int level){
    	this.type = randomType();
        this.maxHealth = type.getMaxHealth(level);
        this.currentHealth = maxHealth;
        this.attack = type.getAttack(level);
        this.luck = type.getLuck();
        this.expValue = type.getExpValue(level);
        this.alive = true;
        this.name = type.name();
    }
    
    public void addSprites(){
    	sprite = type.getAvatar();
    	combatSprite = type.getCombatAvatar();
    }
    
    private MonsterType randomType(){
    	Dice dice = new Dice(3);
    	int result = dice.roll();
        return MonsterType.values()[result];
    }
    
    public MonsterType getType(){
    	return type;
    }
    
    public int getHealth(){
    	return currentHealth;
    }


    @Override
    public void die(){
    	
    	((Hero) attacker).addExperience(expValue);
        alive = false;
    }
    
    public void setDeathSprites(){
        this.sprite = new Image("assets/sprites/skull.png");
        this.combatSprite = new Image("assets/sprites/skull.png");
    	
    }

	@Override
	public void setAttacker(Actor actor){attacker = (Hero) actor;}
}