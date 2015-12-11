package actors;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class HeroTest {
	Monster monster;
	ArrayList<Integer> numKills = new ArrayList<Integer>();
	ArrayList<Integer> numBoostedKills = new ArrayList<Integer>();
	ArrayList<Integer> shotsToKill = new ArrayList<Integer>();
	ArrayList<Integer> deathCount = new ArrayList<Integer>();	
	@Test
	public void basicTest() {
		Hero hero = new Hero(HeroType.ROGUE, "Test");
		assertEquals(1, hero.getLevel());
		hero.addExperience(100);
		assertEquals(2, hero.getLevel());
		assertEquals(0 + hero.getType().getIntel(), hero.getActualExp());
		hero.addExperience(10);
		assertEquals(10 + hero.getType().getIntel() *2, hero.getActualExp());
		assertTrue(hero.getExpPercent() == 0.128);
	}
	
	@Test
	public void killCountTest(){
		numKills.clear();
		System.out.println("Test to show the average number of monsters " 
				+"it takes a hero to level up (without boost)");
		//shows when monsters are scaled up to heroes w/ each of their levels
		Hero rogue = new Hero(HeroType.ROGUE, "Test");
		assertEquals(1, rogue.getLevel());
		while (rogue.getLevel() < 20){
			numKills.add(killToLevel(rogue, rogue.getLevel(), 0));
			rogue.incMapLevel();
		}
		System.out.println("Rogue 1-20: " + numKills);
		numKills.clear();
		Hero mage = new Hero(HeroType.MAGE, "MageTest");
		while(mage.getLevel() < 20){
			numKills.add(killToLevel(mage, mage.getLevel(), 0));
			mage.incMapLevel();
		}
		System.out.println("Mage 1-20: " + numKills);
		numKills.clear();
		Hero knight = new Hero(HeroType.KNIGHT, "KnightTest");
		while (knight.getLevel() < 20){
			numKills.add(killToLevel(knight, knight.getLevel(), 0));
			mage.incMapLevel();
		}
		System.out.println("Knight 1-20: " + numKills);
		
	}
	
	
	private int killToLevel(Hero hero, int level, int boost){
		int count = 0;
		while(hero.getLevel() == level){
			Monster monster = new Monster(0);
			hero.addExperience(boost + monster.getType().getExpValue(hero.getMapLevel()));
			count++;
			
		}
		return count;
	}
	@Test
	public void combatTest(){
		testCombatFor(HeroType.ROGUE);
		testCombatFor(HeroType.MAGE);
		testCombatFor(HeroType.KNIGHT);
		
	}
	
	private void testCombatFor(HeroType type){
		shotsToKill.clear();
		for (int i=0; i<100; i++){
			int count = 0;
			Hero hero = new Hero(type, "Knight");
			Monster monster = new Monster(0);
			while (hero.isAlive() && monster.isAlive()){
				hero.attack(monster);
				if (monster.isAlive()){
					monster.attack(hero);
				}
				count++;
			}
			shotsToKill.add(count);
		}
		//System.out.println(type.name() + ": " + shotsToKill);
		int added = 0;
		for (Integer i: shotsToKill){
			added += i;
		}
		System.out.println("Average shots for " + type.name() +":" + (added/shotsToKill.size()));
		shotsToKill.clear();
		int deathCount = 0;
		for (int i=0; i<100; i++){
			int count1 = 0;
			Hero hero = new Hero(type, "Name", 5, 6);
			hero.loadFromSaved(hero.getMaxHealth(), 0, 0,0);
			Monster monster = new Monster(6);
			while (hero.isAlive() && monster.isAlive()){
				hero.attack(monster);
				if (monster.isAlive()){
					monster.attack(hero);
				}
				if (!hero.isAlive()){
					deathCount++;
				} else {
					
					count1++;
				}
			}
			shotsToKill.add(count1);
		
		}
		int added1 = 0;
		for (Integer i: shotsToKill){
			added1 += i;
		}
		System.out.println("Average shots @ L5 for " + type.name() +":" + (added1/shotsToKill.size()));
		System.out.println("Death count: " + deathCount);
	}
	
	
	
	
	
	

}
