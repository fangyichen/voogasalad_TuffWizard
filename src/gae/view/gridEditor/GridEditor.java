package gae.view.gridEditor;

import gae.model.Receiver;
import gae.view.editorpane.editorComponents.EditorComponent;
import gae.view.editorpane.editorComponents.QueueEditor;
import interfaces.SpecialEditorAnnotation;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Grid Editor
 * Special editor that uses the editor components
 * Editor components used: 
 * 		single selector (for tiles -> path vs nature; for sprites)
 * 		text field (for width and height)
 * 		toggle button (for placing tiles vs sprites)
 * 		queue editor for waves
 * 
 * Currently, this will be it's own application until integrated with GAE pane		
 * Make the editor component nodes be added to the grid instead of the editor pane
 * 
 * @author ReyinaSenatus
 *
 */

//TODO: Make small editor things (except for queue)
//TODO: Make grid editor show up in inventory
//TODO: Figure out how to get created tiles and sprites
//TODO: Sprites used are base, port, tower, (tile)
//TODO: set up queue editor for waves
//TODO: Change content of each tab to sprite or tile

public class GridEditor extends EditorComponent{
	//private int myWidth;
	//private int myHeight;
	private IntegerProperty myWidth;
	private IntegerProperty myHeight;
	private GridPane mainPane;
	private ArrayList<Method> mySpecialMethods;
	private GridPane tileGrid;
	private GridPane spriteGrid;
	
	public GridEditor(Receiver receiver, Method method, String objectName) {
		super(receiver, method, objectName);
	}

	@Override
	public void setUpEditor() {
		myWidth = new SimpleIntegerProperty();
		myHeight = new SimpleIntegerProperty();
		Button b1 = new Button("Open Grid Editor");
		b1.setOnAction(e -> {
			Stage primaryStage = new Stage();
			primaryStage.setTitle("Grid Editor");
			Group root = new Group();
			Scene scene = new Scene(root, 400, 400);
			mainPane = new GridPane();
			mainPane.prefHeightProperty().bind(scene.heightProperty());
			mainPane.prefWidthProperty().bind(scene.widthProperty());
			
			//MAKING THE GRID
			gridSize();
			GridMaker myGrid = new GridMaker(myReceiver);
			
			//myGrid.grid(mainPane, myHeight, myWidth, myReceiver);
			
			Button gridDone = new Button("Create Grid");
			mainPane.add(gridDone, 1, 4);
			gridDone.setOnAction(
	                new EventHandler<ActionEvent>() {
	                    @Override
	                    public void handle(final ActionEvent e) {
	                    	Stage gridStage = new Stage();
	                    	gridStage.show();
	                    	gridStage.setTitle("Make your grid");
	                    	Group gridGroup = new Group();
	                    	Scene gridScene = new Scene(gridGroup, myHeight.getValue()*10, myWidth.getValue()*10);
	                    	GridPane grid = new GridPane();
	                    	//grid.prefWidthProperty().bind(myWidth);
	                    	//grid.prefHeightProperty().bind(myHeight);
	                    	System.out.println("My height1 " + myHeight.getValue());
	                    	System.out.println("My width1 " + myWidth.getValue());
	                    	//grid.setPrefHeight((int) myHeight.getValue());
	                    	//grid.setPrefWidth((int) myWidth.getValue());
	                    	grid.setPrefSize((int) myWidth.getValue(), (int) myHeight.getValue());
	                    	//grid.setPrefColumnSize(20);
	                    	System.out.println("Grid width"+grid.getPrefWidth());
	                    	System.out.println("My height2 " + myHeight.getValue());
	                    	System.out.println("My width2 " + myWidth.getValue());
	                    	
	                    	if(myHeight.getValue()<50){
	                    		gridStage.setHeight(500);
	                    	}
	                    	else if(myWidth.getValue()<50){
	                    		gridStage.setWidth(500);
	                    	}
	                    	
	                    	gridGroup.getChildren().add(myGrid.paneForGrid(gridScene, grid));
	                    	gridStage.setScene(gridScene);
	                    	
	                    	System.out.println("Grid Created");
	                    }
	                });
			
			tileGrid = myGrid.tiles();
			spriteGrid = myGrid.sprites(); //Only works when the user is done 
			for(int r=0; r<myHeight.getValue(); r++){
				for(int c=0; c<myWidth.getValue(); c++){
					Node tile = getNodeByRowColumnIndex(r, c, tileGrid);
					//String tileArray = 
					myReceiver.runOnObjectSwap(myObject, getMethod("setTiles"), tile.toString());
					//might not be a string
					Node sprite = getNodeByRowColumnIndex(r, c, spriteGrid);
					myReceiver.runOnObjectSwap(myObject, getMethod("setSprite"), sprite.toString());
				}
				
			}
			
			//MAKING THE QUEUE (Currently useless code) 
			Button waves = new Button("Make Wave Queue");
			waves.setOnAction(
	                new EventHandler<ActionEvent>() {
	                    @Override
	                    public void handle(final ActionEvent e) {
	                    	WaveMaker myWaves = new WaveMaker();
	                    	myWaves.setUp();
	                    }
	                });
			
			//MAKING THE QUEUE EDITOR -> the editor should be returning the value
			//TODO: I promise, this will probs not work

			//mainPane.add(waves, 1, 5);
			
			//ADDING EVERYTHING TO THE VIEW
			root.getChildren().add(mainPane);
			primaryStage.setScene(scene);
			primaryStage.show();
		});
		
		this.getChildren().add(b1);
        mySpecialMethods = new ArrayList<>(myReceiver.getSpecialEditorMethods(myObject));
	}
	
	
	private void gridSize(){
		Node width = width();
        mainPane.add(width, 1, 1);
		Node height = height();
		mainPane.add(height, 1, 2);
	}
	
	private Node height(){//TODO: Make sure the inputs are integers
    	GridTextField text = new GridTextField();
    	text.setName("height");
    	text.setUpEditor();
    	text.btn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	String val = text.textVal.getCharacters().toString();
                    	myHeight.setValue(Integer.parseInt(val)); 
                    	System.out.println(myHeight);
                    	myReceiver.runOnObject(myObject, getMethod("Set Height"), myHeight.getValue());
                    }
                });
    	return text.box();
    }
    
    private Node width(){//TODO: Make sure text field only accepts integers
    	GridTextField text = new GridTextField();
    	text.setName("width");
    	text.setUpEditor();
    	
    	text.btn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	String val = text.textVal.getCharacters().toString();
                    	myWidth.setValue(Integer.parseInt(val)); 
                    	myReceiver.runOnObject(myObject, getMethod("Set Width"), myWidth.getValue());
                    }
                });
    	return text.box();
    }
	
    private Method getMethod(String name){
		for(Method method : mySpecialMethods){
			SpecialEditorAnnotation specialAnnotation = method
					.getAnnotation(SpecialEditorAnnotation.class);
			if(specialAnnotation.name().equals(name)){
				return method;
			}
		}
		return null;
	}
    
    public Node getNodeByRowColumnIndex(int row, int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for(Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

}