package actors;

import java.util.ArrayList;

import game.Direction;
import game.Level;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import terrain.Item;
import terrain.ItemType;

/**
 * Created by josephbenton on 9/13/15.
 */

public class Hero extends Actor{
    private int level, experience, expToNextLevel;
    private int boostCount = 0;
    private int mapLevel = 0;
    private Actor attacker;
    private HeroType type;
    private ArrayList<Item> inventory = new ArrayList<Item>();
    private static final int EXP_BUFF = 25;
    private static final int HEALTH_BUFF = 6;
    private static final int ATK_BUFF = 2;
    private static final int INIT_EXP_REQUIRED = 100;
    private static final int EXP_BOOST = 4;

    public Hero(HeroType type, String name) {
        create(type, name);
        this.experience = 0;
        this.level = 1;
        adjustStats();
        currentHealth = maxHealth;
    }
    
    public Hero(HeroType type, String name, int level, int mapLevel){
        create(type, name);
        this.level = level;
        this.mapLevel = mapLevel;

        adjustStats();
    }
    
    
    private void create(HeroType type, String name){
        this.type = type;
        this.alive = true;
        this.name = name;
    }
    
    public void addSprites(){
        setSprite(type.getSpriteDirection(Direction.DOWN));
        this.combatSprite = type.getCombatAvatar();
    	
    }
    
    private void adjustStats(){
    	this.expToNextLevel = offsetByLevel(INIT_EXP_REQUIRED, EXP_BUFF);
    	this.maxHealth = offsetByLevel(type.getMaxHealth(), HEALTH_BUFF);
    	this.attack = offsetByLevel(type.getAttack(), ATK_BUFF);
    	this.luck = type.getLuck();
    }
    private int offsetByLevel(int initial, int factorOf){
    	int levelOffset = this.level - 1;
    	return initial + (levelOffset * factorOf);
    }
    
    public void loadFromSaved(int health, int exp, int healthPotions, int expPotions){
    	this.currentHealth = health;
    	this.experience = exp;
    	for (int i = 0; i < healthPotions; i++){
    		inventory.add(new Item(ItemType.HEALTH));
    	}
    	for (int j = 0; j < expPotions; j++){
    		inventory.add(new Item(ItemType.EXPERIENCE));
    	}
        if (currentHealth <= 0){
        	alive = false;
        }
    
    }
    
    public boolean addIfInventorySpace(Item i){
    	if (inventory.size() < 20){
    		inventory.add(i);
    		return true;
    	}
    	return false;
    }
    public ArrayList<Item> getInventory(){ return inventory;}
    
    public void addExperience(int monsterExp){
    	int gained = monsterExp + getBoost() + type.getIntel();
    	experience += gained;
    	this.levelIf();
    }
    public boolean usePotion(ItemType type){
    	boolean used = false;
    	switch(type){
    	case HEALTH:
    		used = heal();
    		break;
    	case EXPERIENCE:
    		used = boost();
    		break;
    	}
    	if (used){
    		for (Item i: inventory){
    			if (i.getType().equals(type)){
    				inventory.remove(i);
    				return used;
    			}
    		}
    	}
    	return used;
    }
    private boolean boostActive(){
    	return boostCount != 0;
    }
    private int getBoost(){
    	if (boostActive()){
    		boostCount--;
    		return (level - 1) + EXP_BOOST;
    	}
    	return 0;
    }
    boolean heal(){
    	if (currentHealth < maxHealth){
    		int value = ItemType.HEALTH.getValue(level);
    		if (maxHealth - currentHealth < value){
    			currentHealth = maxHealth;
    		} else {
    			currentHealth += ItemType.HEALTH.getValue(level);
    		}
    		return true;
    	}
    	return false;
    	
    }
    boolean boost(){
    	boolean boosted = false;
    	if (!boostActive()){
    		boostCount = 4;
			boosted  = true;
		} else {
			Alert boostActive = new Alert(AlertType.INFORMATION);
			boostActive.setContentText("Experience boost still active!\n"
					+ "Please wait " + boostCount + " more battles.");
			boostActive.show();
		}
    	return boosted;
    }
    private void levelIf(){
    	if (expToNextLevel - experience <= 0){
    		level++;
    		experience = Math.abs(expToNextLevel - experience);
    		adjustStats();
    	}
    }
    
    public void resurrect(){
    	currentHealth = maxHealth;
    	experience = 0;
    	inventory.clear();
    	alive = true;
    }
    public void moveAnimated(Direction dir, Level currentLevel){
    	move(dir, currentLevel);
    	setSprite(type.getSpriteDirection(dir));
    }
    @Override
    public void die(){
    	boostCount = 0;
    	currentHealth = 0;
        alive = false;
    }
	@Override
	public void setAttacker(Actor actor) {
        this.attacker = actor;
    }
	public void setName(String input) {
        this.name = input;
    }
	public void setSprite(Image image) {
        sprite = image;
    }
	public int getLevel() {
        return level;
    }
	public String getName() {
        return this.name;
    }
	public int getActualHealth() {
        return currentHealth;
    }
	
	public int getAttack(){
		return attack;
	}
	
	public int getExpToNextLvl(){
		return expToNextLevel;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}
	public int getActualExp() {
        return this.experience;
    }
	public HeroType getType() {
        return this.type;
    }
	public double getExpPercent() {
        return (double)experience / (double)expToNextLevel;
    }
	@Override
    	public Image getSprite() {
        return sprite;
    }
	public Actor getAttacker(){
		return attacker;
	}
	
	public int getMapLevel(){
		return mapLevel;
	}
	
	public void incMapLevel(){
		mapLevel++;
	}
	
	public int getNumPotions(ItemType type){
		int num = 0;
		for (Item i: inventory){
			if (i.getType() == type){
				num++;
			}
		}
		return num;
	}
}

	
	
