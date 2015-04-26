package engine.gameScreens;

import java.util.Map;

import engine.gameLogic.GameObject;
import engine.sprites.Tower;

/**
 * The Class Store.
 * 
 * @author Brooks, Patrick, Robert, and Sid.
 */
public class Store extends GameObject {

	
	private Map<Tower, Integer> myTowersOnSale;
	private Integer myMoney;
	private String myBackgroundImagePath;
	private Map<String, Tower> myTowerNames;
	
	/**
	 * Instantiates a new store.
	 */
	public Store(){
		
	}
	
	/**
	 * Instantiates a new store.
	 *
	 * @param towersOnSale the towers on sale
	 * @param backgroundImagePath the background image path
	 */
	public Store(Map<Tower, Integer> towersOnSale, String backgroundImagePath){
		myTowersOnSale = towersOnSale;
		myBackgroundImagePath = backgroundImagePath;
	}

	
	/**
	 * Gets the tower cost.
	 *
	 * @param tower the tower
	 * @return the tower cost
	 */
	public Integer getTowerCost(Tower tower){
		return myTowersOnSale.get(tower);
	}
	
	/**
	 * Gets the image path.
	 *
	 * @return the image path
	 */
	public String getImagePath(){
		return myBackgroundImagePath;
	}
	
	public Tower getFromID(String TowerID){
		for (String myTower: myTowerNames.keySet()){
			if (myTower == TowerID){
				return myTowerNames.get(myTower);
			}
		}
		return null;
	}
}