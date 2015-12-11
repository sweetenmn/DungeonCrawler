package game;

import actors.Hero;
import actors.Monster;
import actors.MonsterType;
import game.graphics.Camera;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import menu.EndScreen;
import menu.StartScreen;

import java.util.ArrayList;

import terrain.Item;
import terrain.ItemType;
import util.Dice;

/**
 * Created by Joseph on 9/20/2015.
 */
public class Game {
    private Hero hero;
    private ArrayList<Monster> mobs;
    private Level currentLevel;
    private  GameState state;
    private Combat combat;
    private Monster inspected;

    public void moveHero(Direction dir) {
        if (state.equals(GameState.WALKING)&&  hero.isAlive()) {
            hero.moveAnimated(dir, currentLevel);
            currentLevel.checkAddToInventory(hero.getPosition());
        }
    }

    public boolean heroAtk() {
    	boolean combat = false;
        if(hero.isAlive()){
            for (Monster m : mobs){
                if(m.getPosition().getDistanceTo(hero.getPosition()) < 2 && m.isAlive()) {
                    startCombat(m);
                    combat = true;
                }
            }
        }
        return combat;
    }
    
    public ArrayList<Item> getHeroInventory(){
    	return hero.getInventory();
    }
    
    public boolean heroInspect(){
        if (hero.isAlive()) {
            for (Monster m : mobs) {
                if (m.getPosition().getDistanceTo(hero.getPosition()) < 2) {
                	inspected = m;
                    return true;
                }
            }
        }
        return false;	
    }

    public void useItem(ItemType type){
    	hero.usePotion(type);
    }
    
    public MonsterType getInspected(){
    	return inspected.getType();
    	
    }
    public void changeLevel(Level level) {
        this.currentLevel = level;
        this.hero = level.getHero();
        this.mobs = level.getMonsters();
    }

    public void startCombat(Monster monster) {
        combat = new Combat(hero, monster);
        setState(GameState.COMBAT);
    }
    
    public Hero getHero(){
    	return hero;
    }

    public double getHeroHealthPercent() {
    	return hero.getHealthPercent();
    }
    
    public double getHeroExpPercent() {
    	return hero.getExpPercent();
    }
    
    public int getHeroLevel() {
    	return hero.getLevel();
    }
    
    public String getHeroName(){
    	return hero.getName();
    }
    
    public void setHeroName(String name){
    	hero.setName(name);

    }
    
    

    public void checkForDeath(Canvas canvas){
        if (!hero.isAlive()) {
            gameEnd(canvas);
        }
    }
    
    public boolean checkAtExit(){
    	if (state == GameState.WALKING){
    		return currentLevel.atExit(hero.getPosition());
    	}
    	return false;
    }

    private void gameEnd(Canvas canvas){
        if (!state.equals(GameState.CHARACTER_CREATE) && !state.equals(GameState.CHARACTER_LOAD)) {
            state = GameState.END;
        }
    }

    public void render(Canvas canvas, Camera camera){
        if (state.equals(GameState.WALKING)) {
            currentLevel.draw(canvas, camera);
        } else if (state.equals(GameState.COMBAT)){
            combat.draw(canvas, camera);
            if (combatSuccess()){
            	combat.renderMonsterDeath();
            	dropLoot();
            	state = GameState.WALKING;
            }
            if (!combat.isHeroAlive()) state = GameState.END;
        } else if (state.equals(GameState.START)) {
            StartScreen startScreen = new StartScreen(0,0);
            startScreen.draw(canvas, camera);
        } else if (state.equals(GameState.END)) {
            EndScreen endScreen = new EndScreen(0, 0);
            endScreen.draw(canvas, camera);
        }
    }
    
    public boolean heroRun(){
    	boolean ran = false;
    	Dice dice = new Dice(2);
    	int result = dice.roll();
    	switch(result){
    	case 0:
    		currentLevel.removeMonster(combat.getMonster());
    		state = GameState.WALKING;
    		ran = true;
    		break;
    	case 1:
    		Alert noRun = new Alert(AlertType.INFORMATION);
    		noRun.setContentText("Failed to run away!");
    		noRun.show();
    		ran = false;
    		break;
    	}
    	return ran;
    	
    }
    
    public void newLevel(Hero hero){
    	setState(GameState.WALKING);
		Level currentLevel = new Level(hero);
		changeLevel(currentLevel);
    }

    public void setState(GameState state){
        this.state = state;
    }


    public GameState getState() {
        return state;
    }
    
    public int mapLevel(){
    	return hero.getMapLevel();
    }
    
    public void mapLevelUp(){
    	hero.incMapLevel();
    }

    public void attack() {
        combat.heroAttack();
    }
    
    public Combat getCombat(){
    	return combat;
    }
    
    public boolean combatSuccess(){
    	return !combat.isMonsterAlive();
    }
    
    public void dropLoot(){
    	currentLevel.addItem(combat.getMonster());
    	
    }
    
    
    
}
