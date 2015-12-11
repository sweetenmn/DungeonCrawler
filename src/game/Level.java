package game;

import actors.Hero;
import actors.Monster;
import game.graphics.Camera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import terrain.Exit;
import terrain.Floor;
import terrain.Item;
import terrain.Wall;
import util.Dice;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by josephbenton on 9/13/15.
 */

public class Level extends Drawable{
    private int height;
    private int width;
    private Hero hero;
    private Position exit;
    private ArrayList<Monster> monsters;
    private ArrayList<Item> items = new ArrayList<Item>();
    private boolean[][] wallMap;
    
    public Level(Hero hero){
    	monsters = new ArrayList<>();
    	this.hero = hero;
    	buildLevel(randomLevel());
    }
    
    private void buildLevel(String map){
    	Scanner scan = null;
        try {
            scan = new Scanner(new File(map));
            width = scan.nextInt();
            height = scan.nextInt();
            wallMap = new boolean[width][height];
            scan.nextLine();
            for(int i = 0; i < height; i++){
            	String line = scan.nextLine();
            	for (int j = 0; j < width; j++){
            		addFloor(j, i);
            		if (line.charAt(j) == 'H'){
            			hero.setPosition(j, i);
            		} else if (line.charAt(j) == 'M'){
            			addMonster(j, i);
            		} else if (line.charAt(j) == 'E'){
            			addExit(j, i);
            		} else if (line.charAt(j) == '#'){
            			wallMap[j][i] = true;
            			addWall(j, i);
            		}
            	}
            }
            	
        } catch (FileNotFoundException e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Internal error: Level map not found");
        }
    }
    
    public String randomLevel(){
    	Dice dice = new Dice(12);
    	int level = dice.roll();
    	return "src/assets/Levels/L" + level + ".txt";
    }

    public void addFloor(int x, int y){
        Position p = new Position(x, y);
        contents.add(new Floor(p));
    }
    public void addMonster(int x, int y){
        Monster monster = new Monster(hero.getMapLevel());
        monster.addSprites();
        monster.setPosition(x, y);
        contents.add(monster);
        monsters.add(monster);
    }
    
    public boolean isOccupied(Position p){
    	for (Monster m: monsters){
    		if (m.getPosition().equals(p)){
    			return m.isAlive();
    		}
    	}
    	return false;
    }
    
    public Item getItemAt(Position p){
    	for (Item item: items){
    		if (item.getPosition().equals(p)){
    			return item;
    		}
    	}
    	return null;
    }

    public ArrayList<Monster> getMonsters(){return monsters;}
    
    public void removeMonster(Monster m){
    	monsters.remove(m);
    	contents.remove(m);
    }
    
    private void removeItem(Item i){
    	items.remove(i);
    	contents.remove(i);
    }

    public void addWall(int x, int y){
        Position p = new Position(x, y);
        contents.add(new Wall(p));
        wallMap[p.getX()][p.getY()] = true;
    }

    public void addExit(int x, int y){
        Position p = new Position(x, y);
        contents.add(new Exit(p));
        this.exit = p;
    }

    public boolean isClear(Position p){
        return !wallMap[p.getX()][p.getY()] && !isOccupied(p);
    }
    
    public void checkAddToInventory(Position p){
    	if (containsItem(p)){
    		Item item = getItemAt(p);
    		if(hero.addIfInventorySpace(item)){
    			removeItem(item);
    		}
    	}
    }
    
    public boolean containsItem(Position p){
    	for (Item item: items){
    		if(item.getPosition().equals(p)){
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean atExit(Position p){
    	return p.equals(exit);
    }

    @Override
    public void draw(Canvas canvas, Camera camera){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, 1000, 1000);
        drawGrid(canvas,camera);
        for(Drawable obj : contents){
            obj.draw(canvas, camera);
        }
        hero.draw(canvas, camera);
    }
    
    public void checkItems(){
    	for (Monster m: monsters){
    		if (!m.isAlive()){
    			for (Item item: m.getType().getLoot()){
    				item.setPosition(getOpen(m.getPosition()));
    				contents.add(item);
    				items.add(item);
    			}
    		}
    	}
    }
    
    public void addItem(Monster m){
    	for (Item item: m.getType().getLoot()){
			item.setPosition(getOpen(m.getPosition()));
			contents.add(item);
			items.add(item);
		}
    }
    
    private Position getOpen(Position p){
    	Position down = Direction.DOWN.getAdj(p);
    	Position left = Direction.LEFT.getAdj(p);
    	Position up = Direction.UP.getAdj(p);
    	Position right = Direction.RIGHT.getAdj(p);
    	if (isOpenForItem(down)){
    		return down;
    	} else if (isOpenForItem(left)){
    		return left;
    	} else if (isOpenForItem(up)){
    		return up;
    	} else if (isOpenForItem(right)){
    		return right;
    	} else {
    		return p;
    	}
    }
    
    private boolean isOpenForItem(Position where){
    	return isClear(where) && !containsItem(where) && !atExit(where);
    }

    private void drawGrid(Canvas canvas, Camera camera){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIGHTGRAY);
        for(int j = 0; j < canvas.getWidth(); j += 32){
            int offsetX = camera.getPosition().getX() % 32;
            int offsetY = camera.getPosition().getY() % 32;
            gc.strokeLine(j - offsetX, 0, j - offsetX, canvas.getHeight());
            gc.strokeLine(0, j - offsetY, canvas.getWidth(), j - offsetY);
        }
    }

    public Hero getHero(){return hero;}

    public boolean inBounds(Position p){
        boolean inBoundsX = (p.getX() < width && p.getX() >= 0);
        boolean inBoundsY = (p.getY() < height && p.getY() >= 0);
        return inBoundsX && inBoundsY;
    }
}