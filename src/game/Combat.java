package game;

import actors.Hero;
import actors.Monster;
import game.graphics.Camera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by josephbenton on 11/23/15.
 */
public class Combat extends Drawable{
    Hero hero;
    Monster monster;
    Projectile projectile;
    public Combat(Hero hero, Monster monster) {
        this.hero = hero;
        this.monster = monster;
        hero.setAttacker(monster);
        monster.setAttacker(hero);
    }


    public void heroAttack() {
        if (projectile == null || projectile.isFinished()) {
            hero.attack(monster);
            projectile = new Projectile(true);
        }
    }

    private void monsterAttack() {
    	monster.attack(hero);
        projectile = new Projectile(false);
    }


    @Override
    public void draw(Canvas canvas, Camera camera){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, 1000, 1000);
        gc.drawImage(new Image("assets/combat/combat_background.png"), 0, 0, 600, 300);
        hero.drawForCombat(canvas, true);
        monster.drawForCombat(canvas, false);
        if (projectile != null) {
            projectile.draw(canvas);
        }
    }
    public void setHealthBars(ProgressBar h, ProgressBar m) {
        h.setProgress(hero.getHealthPercent());
        m.setProgress(monster.getHealthPercent());
    }
    public void setText(Label h, Label m) {
        h.setText(hero.getName() + " | Level " + hero.getLevel());
        m.setText(monster.getType().name());
    }

    public boolean isMonsterAlive() {
        return monster.isAlive();
    }
    
    public void renderMonsterDeath(){
    	monster.setDeathSprites();

    }

    public boolean isHeroAlive() {
        return hero.isAlive();
    }

    private class Projectile {
        Image sprite;
        int position, range;
        boolean leftToRight, finished;

        public Projectile(boolean friendly) {
            finished = false;
            position = 50;
            range = 250;
            this.sprite = new Image(friendly ? "assets/combat/fireball_right.png" : "assets/combat/fireball_left.png");
            leftToRight = friendly;
        }
        public void draw (Canvas canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            if (leftToRight && position < range) {
                gc.drawImage(sprite, 100 + position, 150);
            } else if (position < range) {
                gc.drawImage(sprite, 350 - position, 150);
            } else {
                finished = true;
                if (leftToRight) {

                    monsterAttack();
                }
            }
            position += 7;
        }
        public boolean isFinished() {
            return finished;
        }
    }
    
    public Monster getMonster(){
    	return (Monster) monster;
    }
}
