package gae;

import gae.inventorypane.InventoryPane;

import gae.editorpane.WaveEditor;

import gae.editorpane.GameEditor;
import gae.editorpane.TitleScreenEditor;

import gae.menupane.MenuAdder;
import gae.menupane.MenuManager;
import gae.menupane.MenuPane;
import gae.model.Receiver;

import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Display {

	private BorderPane myRoot;
	private Receiver myReceiver;
	private Scene myScene;
	private MenuAdder myMenuAdder;
	private static ResourceBundle myConfigs = BundleGrabber.grabBundle(
			"configs", "display"); // find way to add this :
									// this.getClass().getName());
	private static final int myWidth = Integer.parseInt(myConfigs
			.getString("Width"));
	private static final int myHeight = Integer.parseInt(myConfigs
			.getString("Height"));

	public Display(Receiver rec) {
		myRoot = new BorderPane();
		myReceiver = rec;
	}

	public Scene init() {
		myRoot.setTop(setupMenuPane());
		myRoot.setCenter(setupEditorPane());
		myRoot.setLeft(setupInventoryPane());
		myRoot.setBottom(setupTimelinePane());
		myScene = new Scene(myRoot, myWidth, myHeight);
		return myScene;
	}

	private Pane setupMenuPane() {
		MenuManager menuManager = new MenuManager();
		myMenuAdder = (MenuAdder) menuManager;
		MenuPane menuPane = new MenuPane(myMenuAdder, menuManager.getMenuBar());
		return menuPane.getPane();
	}

	private Pane setupEditorPane() {
		// TODO Auto-generated method stub
		TitleScreenEditor tsE = new TitleScreenEditor(myMenuAdder);
//		GameEditor gE = new GameEditor(myMenuAdder);
//		return (new WaveEditor("Sample Editor");
		return tsE.getPane();
	}

	private Pane setupInventoryPane() {
		InventoryPane inventoryPane = new InventoryPane(myMenuAdder, myReceiver);
		return inventoryPane.getPane();
	}
	
	private Pane setupTimelinePane() {
		// TODO Auto-generated method stub
		return null;
	}

}
