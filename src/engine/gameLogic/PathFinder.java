package engine.gameLogic;

import interfaces.Collidable;
import interfaces.Shootable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import engine.Grid;
import engine.Path;
import engine.sprites.Enemy;
import engine.sprites.Projectile;
import engine.sprites.Tile;

public class PathFinder {
	
	private Grid myGrid;
	private HashMap<String, LinkedList<Tile>> myEnemyPaths; 
	// TODO this will be an issue when multiple enemies of the same type require different paths..
	// ie multiple different ports with the same waves-MAKE SURE that the same enemy out of different waves has a different name
	
	public PathFinder(Grid grid){
		myGrid = grid;
	}
	
	//REFACTOR to make this a generic setPath()... for both enemies and projectiles
	public void setEnemyPath(Enemy enemy, Wave w){
		if(!myEnemyPaths.containsKey(enemy.getName()))
			myEnemyPaths.put(enemy.getName(), findEnemyPath(enemy, myGrid.getPortFor(w)));
		
		enemy.setPath(convertToPath(myEnemyPaths.get(enemy), enemy));
	}
	
	public LinkedList<Tile> findEnemyPath(Enemy enemy, Tile port){
		Tile current = port;
		LinkedList<Tile> path = new LinkedList<Tile>();
		boolean pathFound = false;
		while(!pathFound){
			path.add(current);
			current = findNextTile(current, enemy);	
			if (current == null){ // perhaps give Tile a way to check if it has a Tower on it...?
				pathFound = true;
				
			}
		}
		return path;
	}
	

	private Path convertToPath(LinkedList<Tile> tiles, Enemy enemy){
		
		return enemy.getMovement().generatePath(tiles);
		
	}
	
	
	
	

	public Tile findNextTile(Tile current, Enemy enemy){
		for (Tile t: getTileNeighbors(current)){
			if(enemy.getWalkables().contains(t.getName()) && !enemy.getTilePath().contains(t)){
				enemy.getTilePath().add(t);
				return t;
			}
		}
		return null;
		// TODO
		// SHOULD return null if no next tile is found, or if this tile is a base..there is a null check in findPath	
	}
	
	public List<Tile> getTileNeighbors(Tile t){
		int x = t.getGridLocation().x;
		int y = t.getGridLocation().y;
		List<Tile> neighbors = new ArrayList<Tile>();
		int[] dx = {1, -1, 0, 0};
		int[] dy = {0, 0, 1, -1};
		
		for (int i = 0; i < dx.length; i++){
			if(x + dx[i] < myGrid.getTiles().length && 
					x + dx[i] >= 0 &&
					y + dy[i] < myGrid.getTiles()[0].length &&
					y + dy[i] >= 0){
				Tile temp = (myGrid.getTiles()[x + dx[i]][y + dy[i]]);
				neighbors.add(temp);
			}
		}

		return neighbors;
	}

	public Path target(Shootable s, Collidable c) {
		// TODO CREATE PROJECTILE PATH BETWEEN OBJECTS
		return null;
	}

	public void generateProjectile(Projectile projectile, Path path) {
		Projectile newP = new Projectile(projectile);
		newP.setPath(path);
		myGrid.placeSpriteAt(projectile, path.getNextPlacement());
	}

}

