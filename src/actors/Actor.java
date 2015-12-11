package actors;

import util.Dice;
import game.Direction;
import game.Drawable;
import game.Level;
import game.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by josephbenton on 9/13/15.
 */

public abstract class Actor extends Drawable{
    protected boolean alive;
    public Image getCombatSprite() {
        return combatSprite;
    }

    protected Image combatSprite;
	protected int currentHealth, maxHealth, luck, attack;
	protected String name;
	protected Actor attacker;
	
    public void attack(Actor actor){
    	actor.setAttacker(this);
        Dice dice = new Dice(20);
        int roll = dice.roll() + luck;
        if (roll < 5){
            actor.takeDamage(attack / 2);
        } else if (roll < 20){
            actor.takeDamage(attack);
        } else {
            actor.takeDamage(attack * 2);
        }
    }

    protected void takeDamage(int damage){
       // System.out.println(this.name + " took " + damage + " damage!");
        currentHealth -= damage;
        if (currentHealth <= 0) {
            this.die();
        }
    }
    
	public void setAttacker(Actor actor) {
        this.attacker = actor;
    }

    public void move(Direction dir, Level currentLevel){
        Position p = dir.getAdj(this.getPosition());
        if (currentLevel.inBounds(p) && currentLevel.isClear(p)) {
            this.setPosition(p);
        }
    }
    public boolean isAlive(){return alive;}
    public  Image getSprite(){return sprite;}
    public double getHealthPercent(){return (double)currentHealth / (double)maxHealth;}
    
    public void drawForCombat(Canvas canvas, boolean isHero){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (isHero) {
            gc.drawImage(combatSprite, 50, 100);
        } else {
            gc.drawImage(combatSprite, 350, 100);
        }
    }
    public abstract void die();
}