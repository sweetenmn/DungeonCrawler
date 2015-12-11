package gui;

import java.util.ArrayList;

import persistence.CharacterBank;
import terrain.ItemType;
import actors.Hero;
import actors.HeroType;
import actors.MonsterType;
import game.Direction;
import game.Game;
import game.GameState;
import game.Position;
import game.graphics.Camera;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Controller{
    private long FRAMES_PER_SEC = 20L;
    private long NANO_INTERVAL = 100000000L / FRAMES_PER_SEC;
    @FXML
    BorderPane pane;
    @FXML
    Canvas canvas;
    @FXML
    Pane startPane, adventurePane, loadPane, combatPane;
    @FXML
    VBox inspectPane;
    @FXML
    VBox inventory;
    @FXML
    Button enterCombatButton, inspectButton, startButton, saveButton, exitButton, loadButton, clearButton;
    @FXML
    Button runButton;
    @FXML
    TextField nameInput;
    @FXML
    ImageView portrait, loadingView;
    @FXML
    Label healthCount;
    @FXML
    Label expCount;
    @FXML
    Text name, loadingName, savedText, inspectType, inspectAttack, inspectHealth, inventorySpace;
    @FXML
    Text aboutClass;
    @FXML
    Label combatHeroName;
    @FXML
    Label combatMonsterName;
    @FXML
    Rectangle mageSelect, knightSelect, rogueSelect;
    @FXML
    ProgressBar healthBar, expBar, combatHeroHealth, combatMonsterHealth;
    @FXML
    ChoiceBox<String> characterChoice;
    
    HeroType profSelected;

    private Camera camera = new Camera(new Position(0, 0));
    private Position cameraDragStartPos;
    private CharacterBank characters = new CharacterBank();
    double startX, startY;
    Game game;
    private AnimationTimer timer = new AnimationTimer(){
        long last = 0;

        @Override
        public void handle(long now){
            if(now - last > NANO_INTERVAL){
                healthBar.setProgress(game.getHeroHealthPercent());
                expBar.setProgress(game.getHeroExpPercent());
                 name.setText(game.getHeroName() + " | Level " + game.getHeroLevel());
            
                
                checkForGameOver();
                handleCombat();
                
                game.render(canvas, camera);
            }
            last = now;
        }
    };

    @FXML
    public void initialize(){
    	game = new Game();
    	game.setState(GameState.START);
        startHandlingClicks();     
        game.render(canvas, camera);
		startHandlingWalk();
		startHandlingDrag(); 
    }   
    
    
    
    private void startHandlingClicks(){
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                ev -> {
                    handleClickAt(ev.getX(), ev.getY());
                });
    }
    
    

    
    private void handleClickAt(double x, double y){
    	if (game.getState() == GameState.START |
    			game.getState() == GameState.CHARACTER_CREATE |
    			game.getState() == GameState.CHARACTER_LOAD |
                game.getState() == GameState.END){
    		checkNewClicked(x, y);
    		checkLoadClicked(x, y);
    	}
    }
    
    private void checkNewClicked(double x, double y){
    	if (x > 180 && x < 354 && y >175 && y < 200){
    		viewCharacterCreation();
    	}
    }
    private void checkLoadClicked(double x, double y){
    	if (x > 170 && x < 365 && y > 214 && y < 236){
    		viewCharacterLoading();
    	}
    }
    
    private void viewCharacterCreation(){
        combatPane.setVisible(false);
    	startPane.setVisible(true);
    	loadPane.setVisible(false);
    	game.setState(GameState.CHARACTER_CREATE);
    }
    
    private void viewCharacterLoading(){
        combatPane.setVisible(false);
        startPane.setVisible(false);
    	loadPane.setVisible(true);
        setChoice();
        game.setState(GameState.CHARACTER_LOAD);
    }
       
    @FXML
    public void startGame(){
    	switch(game.getState()){
		case CHARACTER_CREATE: newGame();
			break;
		case CHARACTER_LOAD:
			try{
				loadGame();
			} catch (IllegalStateException e) {
				if (confirm(ConfirmType.RESURRECT)){
					getFromSelection().resurrect();
					loadGame();
				}
			}
			break;
		case COMBAT: case END: case START: case WALKING:
			break;   	
    	}
    }
    
    @FXML
    public void loadCharacter(){
    	if (loadButton.getText().equals("VIEW")){
    		if (characterChoice.getSelectionModel().getSelectedItem() != null){
    			clearButton.setVisible(true);
    			loadButton.setText("START");
    			displayChoice();
    		}
    	} else {
    		startGame();
    		clearChoice();
    	}
    }
    
    private void newGame(){
    	if (gameReady()){
    		Hero hero = new Hero(profSelected, nameInput.getText());
    		hero.addSprites();
    		unselectAll();
    		aboutClass.setText("");;
    		nameInput.clear();
    		showLevel(hero);
    		saveHero();
    		setChoice();
    		savedText.setVisible(false);
    	}    	
    }
    
    private void loadGame(){
		Hero hero = getFromSelection();
		if (!hero.isAlive()){
			throw new IllegalStateException();
		} else {
			showLevel(hero);
			updateInventory();
			
		}
    }
    
    private Hero getFromSelection(){
    	String name = characterChoice.getSelectionModel().getSelectedItem();
		return characters.getHero(name);
    }

    
    private void setChoice(){
    	characterChoice.setItems(characters.getSavedNames());
    }
    
    public void displayChoice(){
    	if (characterChoice.getSelectionModel().getSelectedItem() != null){
    		String name = characterChoice.getSelectionModel().getSelectedItem();
    		Hero hero = characters.getHero(name);
    		loadingName.setVisible(true);
    		loadingName.setText(name + " | Level " + hero.getLevel());
    		loadingView.setImage(hero.getType().getPortrait());
    		
    	}
    }
    
    @FXML
    public void clearChoice(){
    	loadingName.setVisible(false);
    	characterChoice.getSelectionModel().clearSelection();
    	loadingView.setImage(null);
    	loadButton.setText("VIEW");
    	clearButton.setVisible(false);
    }
    
    private void showLevel(Hero hero){
		game.newLevel(hero);
		viewWalking();  	
		timer.start();
    }
    
    private boolean gameReady(){
    	boolean ready = profSelected != null;
    	if(!nameInput.getText().matches("^[a-zA-Z]+$")){
    		ready = false;
    		illegalName();
    		
    	}else if(characters.getHero(nameInput.getText()) != null){
    		ready = ready & confirm(ConfirmType.OVERWRITE);
    	}
    	return ready;
    }
    
    private void illegalName(){
    	Alert badName = new Alert(AlertType.INFORMATION);
        badName.setContentText("That is not an "
                + "appropriate name for a hero!");
    	badName.show();
    }
    
    private boolean confirm(ConfirmType type){
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	switch(type){
		case EXIT: createQuitAlert(alert);
			break;
		case OVERWRITE: createOverwriteAlert(alert);
			break;
		case RESURRECT: createResurrectAlert(alert);
			break;
    	}
    	alert.showAndWait();
    	return alert.getResult() == ButtonType.OK;
    }
    
    private void startHandlingWalk() {
        pane.addEventHandler(KeyEvent.KEY_PRESSED,
                ev -> {
                    clearSavedMessage();
                    clearInspect();
                    KeyCode code = ev.getCode();
                    if (code == KeyCode.W || code == KeyCode.UP) {
                        up();
                    } else if (code == KeyCode.A || code == KeyCode.LEFT) {
                        left();
                    } else if (code == KeyCode.S || code == KeyCode.DOWN) {
                        down();
                    } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
                        right();
                    } else if (code == KeyCode.SHIFT) {
                        enterCombat();
                    }
                    checkHeroAtExit();
                    updateInventory();

                });
    }
    
    private void startHandlingDrag(){
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                ev -> {
                    camera.setPosition((int)(cameraDragStartPos.getX() + startX - ev.getX()),
                    		cameraDragStartPos.getY() + (int)(startY - ev.getY()));
                });
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                ev -> {
                    cameraDragStartPos = camera.getPosition();
                    startX = ev.getX();
                    startY = ev.getY();
                });
    }
    
    private void viewWalking(){
    	camera = new Camera(new Position(0, 0));
    	startPane.setVisible(false);
    	loadPane.setVisible(false);
    	combatPane.setVisible(false);
		adventurePane.setVisible(true);
		inventory.setVisible(true);
		portrait.setImage(game.getHero().getType().getPortrait());
    }
    
    @FXML
    public void saveHero(){
    	characters.saveHero(game.getHero());
    	savedText.setVisible(true);
    }

    @FXML
    public void attack() {
        game.attack();
    }

    @FXML
    public void enterCombat() {
        if (game.getState().equals(GameState.WALKING)) {
            if (game.heroAtk()) {
                adventurePane.setVisible(false);
                combatPane.setVisible(true);
                runButton.setVisible(true);                
                savedText.setVisible(false);
                inspectPane.setVisible(false);
            }
        }
    }
    
    @FXML
    public void viewProfile(){
    	Alert profile = new Alert(AlertType.INFORMATION);
    	Hero hero = game.getHero();
    	profile.setTitle("Hero Profile");
    	profile.setHeaderText(hero.getName());
    	profile.setGraphic(null);
    
    	profile.setContentText("Health: " + hero.getActualHealth() + " / " + hero.getMaxHealth()
    			+ "\nAttack: " + hero.getAttack() + "\nExperience: " + hero.getActualExp() + " / "
    			+ hero.getExpToNextLvl() 
    			+"\nLevels Cleared: " + hero.getMapLevel());
    	profile.show();
    	
    }
    
    private void handleCombat(){
    	if (game.getCombat() != null) {
            game.getCombat().setHealthBars(combatHeroHealth, combatMonsterHealth);
            combatHeroName.setText(name.getText());
            combatMonsterName.setText(game.getCombat().getMonster().getType().name());
    	
	    	if (game.combatSuccess()){
	    		combatPane.setVisible(false);
	    		adventurePane.setVisible(true);
	    	} 
    	}
    }
    
    private void checkHeroAtExit(){
    	if (game.checkAtExit()){
    		game.getHero().incMapLevel();
    		showLevel(game.getHero());
    	}
    }

    @FXML
    public void inspect(){
    	if (game.heroInspect()){
    		MonsterType inspected = game.getInspected();
    		String health = Integer.toString(inspected.getMaxHealth(game.mapLevel()));
    		String attack = Integer.toString(inspected.getAttack(game.mapLevel()));
    		String type = inspected.name();
    		inspectType.setText(type);
    		inspectAttack.setText("Attack: " + attack);
    		inspectHealth.setText("Health: " + health);
    		inspectPane.setVisible(true);
    		
    	}

    }

    public void run(){
    	if (game.heroRun()){
    		combatPane.setVisible(false);
    		adventurePane.setVisible(true);
    	} else {
    		runButton.setVisible(false);
    	}
    }

    @FXML
    public void up() { game.moveHero(Direction.UP);}

    @FXML
    public void down() { game.moveHero(Direction.DOWN); }

    @FXML
    public void left() { game.moveHero(Direction.LEFT); }

    @FXML
    public void right() { game.moveHero(Direction.RIGHT); }
    
    public void updateInventory(){
    	if (notAtMenu()){
    		int numHealth = game.getHero().getNumPotions(ItemType.HEALTH);
    		int numExp = game.getHero().getNumPotions(ItemType.EXPERIENCE);
    		healthCount.setText(Integer.toString(numHealth));
    		expCount.setText(Integer.toString(numExp));
    		inventorySpace.setText(Integer.toString(numHealth + numExp) + "/20");
    	}
    }
    @FXML
    public void useExpPotion(){
    	if (!expCount.getText().equals("0")){
    		game.useItem(ItemType.EXPERIENCE);
    		updateInventory();
    	}
    }
    @FXML
    public void useHealthPotion(){
    	if (!healthCount.getText().equals("0")){
    		game.useItem(ItemType.HEALTH);
    		updateInventory();
    	}
    }
     
    @FXML
    public void selectMage(){
    	unselectOthers(mageSelect);
    	profSelected = HeroType.MAGE;
    	aboutClass.setText(profSelected.getAbout());
    }
    
    @FXML
    public void selectKnight(){
    	unselectOthers(knightSelect);
    	profSelected = HeroType.KNIGHT;
    	aboutClass.setText(profSelected.getAbout());
    }
    
    @FXML
    public void selectRogue(){
    	unselectOthers(rogueSelect);
    	profSelected = HeroType.ROGUE;
    	aboutClass.setText(profSelected.getAbout());
    }
    
    private ArrayList<Rectangle> selectBoxes(){
    	ArrayList<Rectangle> boxes = new ArrayList<Rectangle>();
    	boxes.add(mageSelect);
    	boxes.add(knightSelect);
    	boxes.add(rogueSelect);
    	return boxes;
    }
    
    private void unselectOthers(Rectangle selected){
    	selected.setVisible(true);
    	for (Rectangle box: selectBoxes()){
    		if (!box.equals(selected)){
    			box.setVisible(false);
    		}
    	}
    }
    
    private void unselectAll(){
    	for (Rectangle box: selectBoxes()){
    		box.setVisible(false);
    	}
    }
    
    private void clearSavedMessage(){
    	if (savedText.isVisible()){
    		savedText.setVisible(false);
    	}
    }
    
    private void clearInspect(){
    	if (inspectPane.isVisible()){
    		inspectPane.setVisible(false);
    	}
    }
    
    private void createOverwriteAlert(Alert alert){
    	alert.setTitle("Overwrite Hero Confirmation");
    	alert.setHeaderText("Hero already exists!");
    	alert.setContentText("The hero " + nameInput.getText() 
    			+ " already exists.\n"
    			+ "Their progress will be overwritten.\n" 
    			+ "Do you wish to continue?");
    }
    
    private void createQuitAlert(Alert alert) {
        alert.setTitle("Exit Confirmation");
        alert.setContentText("Exit to main screen without saving?\n"
                + "Unsaved progress will be lost.");
    }
    
    private void createResurrectAlert(Alert alert){
    	alert.setTitle("Resurrect Dead Hero");
    	alert.setHeaderText("This hero has died.");
    	alert.setContentText("Do you wish to resurrect this hero?\nThey will "
    			+"lose their items and any "
    			+"progress and experience they made for their level.");
    }
        
    @FXML
    public void exitToStart(){
    	if (confirm(ConfirmType.EXIT)){
    		timer.stop();
    		game.setState(GameState.START);
    		adventurePane.setVisible(false);
    		combatPane.setVisible(false);
    		inventory.setVisible(false);
    		savedText.setVisible(false);
    		inspectPane.setVisible(false);
    		game.render(canvas, camera);
    	}	
    }
    
    private boolean notAtMenu(){
    	switch(game.getState()){
		case CHARACTER_CREATE: case CHARACTER_LOAD: case END: case START:
			return false;
		case COMBAT: case WALKING:
			return true;
			
    	
    	}
    	return false;
    }
    
    private void checkForGameOver(){
    	game.checkForDeath(canvas);
    	if (!game.getHero().isAlive()){
    		combatPane.setVisible(false);
    		inventory.setVisible(false);
    		saveHero();
    	}
    }
}