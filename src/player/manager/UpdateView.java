// This entire file is part of my masterpiece.
// Brooks Sime

package player.manager;

import engine.Grid;
import engine.HeadsUpDisplay;
import engine.gameLogic.LevelStats;
import engine.gameScreens.DialogueBox;
import engine.gameScreens.Store;


/**
 * This interface is for controller to use to update level and dialogue scene
 * @author Fangyi Chen
 *
 */

public interface UpdateView {
	public void displayError(String errormessage);
	public void updateLevel(Grid grid, Store store, LevelStats stats);
	public void updateDialogue(DialogueBox dialog);
		
	
}
