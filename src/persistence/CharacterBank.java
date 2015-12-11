package persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import terrain.ItemType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import actors.Hero;
import actors.HeroType;

public class CharacterBank {
	private static final String DOCUMENT_NAME = "src/assets/Characters.txt";
	private static final String DELIMITER = "/~RPG~/";
	private ArrayList<Hero> savedHeroes = new ArrayList<Hero>();
	ObservableList<String> heroNames = FXCollections.observableArrayList(); 
	
	public CharacterBank(){
		updateBank();
	}	
	
	public void saveHero(Hero hero){
		try{
			if (getHero(hero.getName()) != null){
					clearDoc();
					overwriteHero(hero);
					for (Hero h: savedHeroes){
						writeHero(h);
					}
			}else{
				writeHero(hero);
				updateBank();
			}
		}catch(IOException e) {
			fail();
		}
	}
	
	private void writeHero(Hero hero){
		FileWriter writer;

		try {
			writer = new FileWriter(DOCUMENT_NAME, true);
			writer.write(DELIMITER + hero.getName());
			writer.write(DELIMITER + hero.getType());
			writer.write(DELIMITER + hero.getLevel());
			writer.write(DELIMITER + hero.getActualHealth());
			writer.write(DELIMITER + hero.getActualExp());
			writer.write(DELIMITER + hero.getNumPotions(ItemType.HEALTH));
			writer.write(DELIMITER + hero.getNumPotions(ItemType.EXPERIENCE));
			writer.write(DELIMITER + hero.getMapLevel());
			writer.write("\n");
			writer.close();
		}catch (IOException e){
			fail();
		}
	}
	
	private void fail(){
		Alert fail = new Alert(AlertType.ERROR);
		fail.setContentText("Internal Error - Unable to save Hero.");
		fail.show();
		
	}
	
	private void overwriteHero(Hero hero){
		savedHeroes.remove(getHero(hero.getName()));
		savedHeroes.add(hero);
	}
	
	private void clearDoc() throws IOException{
		FileWriter writer = new FileWriter(DOCUMENT_NAME);
		writer.close();
	}
	
	
	public Hero getHero(String name){
		for (Hero h: savedHeroes){
			if (h.getName().equals(name)){
				return h;
			}
		}
		return null;
	}
	
	public void updateBank(){
		String[] parts;
		savedHeroes.clear();
		heroNames.clear();
    	FileReader reader;
		try {
			reader = new FileReader(DOCUMENT_NAME);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				parts = line.split(DELIMITER);
				String name = parts[1];
				HeroType prof = stringToProfession(parts[2]);
				int level = Integer.valueOf(parts[3]);
				int health = Integer.valueOf(parts[4]);
				int exp = Integer.valueOf(parts[5]);
				int healthPotions = Integer.valueOf(parts[6]);
				int expPotions = Integer.valueOf(parts[7]);
				int mapLevel = Integer.valueOf(parts[8]);
				Hero hero = new Hero(prof, name, level, mapLevel);
				hero.loadFromSaved(health, exp, healthPotions, expPotions);
				hero.addSprites();
				
				savedHeroes.add(hero);
				heroNames.add(name);
			}
			reader.close();
		}catch(IOException e) {
			Alert internalErr = new Alert(AlertType.ERROR);
			internalErr.setContentText("Internal error - Unable to load existing Heroes");
			internalErr.show();
		}
	}
	
	private HeroType stringToProfession(String prof){
		if (prof.equals("MAGE")){
			return HeroType.MAGE;
		} else if (prof.equals("KNIGHT")){
			return HeroType.KNIGHT;
		} else {
			return HeroType.ROGUE;
		}
	}
	
	public ObservableList<String> getSavedNames(){
		return heroNames;
	}
}