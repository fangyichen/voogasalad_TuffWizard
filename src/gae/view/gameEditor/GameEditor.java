package gae.view.gameEditor;

import engine.gameScreens.NodeButton;
import gae.model.Receiver;
import gae.view.editorpane.editorComponents.EditorComponent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import reflection.Reflection;

/**
 * 
 * @author sunjeevdevulapalli
 * These classes are experiments to see if visual scripting would be possible to show
 * Presumably this would extend GAEPane but as of right now it is free standing as its own application.
 *
 */
public class GameEditor extends EditorComponent{
	
	private static final int CHOICE_SPACING = 10;
	private Receiver myReceiver;
	private Pane myRoot;
	private ArrayList<GameNode> myNodes;
	private GameNode myHead;
	
	
	public GameEditor(Receiver receiver, Method method, String objectName){
		super(receiver, method, objectName);
		
		
	}

	/**
	 * The init of this class. It will draw all the components within the scene
	 * @return the Pane in the scene
	 */
	public Parent drawGameEditor(){
		myRoot = new Pane();
		
		HBox buttons = new HBox(25);
		buttons.setPadding(new Insets(10));
		Button addGameNode = addNodeButton();
		Button addAcceptButton = addAcceptButton();
		
		buttons.getChildren().addAll(addGameNode, addAcceptButton);
		myRoot.getChildren().add(buttons);
		return myRoot;
	}

	private Button addAcceptButton() {
		Button acceptButton = new Button("Accept");
		acceptButton.setOnAction(e -> {
			//export myNodes in whatever format
			//build two maps:
			// 1) String to Node [already done]
			// 2) Node to Map<Enum, Node>; [done]
			printMap();
		});
		return acceptButton;
	}

	private void printMap() {
		Map<String, Map<String, String>> nodeConditionMap = new HashMap<>();
		for(int i = 0; i < myNodes.size(); i++){
			Map<String, String> enumNodeMap = new HashMap<>();
			GameNode node = myNodes.get(i);
			//scene node
			if(i % 2 == 0){
				//latest thing is head.
				if(node.isHead()){
					myHead = node;
				}
				for(GameNode condition: node.getChildren()){
					if(!condition.isButton()){
						String e = condition.toString();
						String n = condition.getChildren() == null ? 
								null : condition.getChildren().get(0).toString();
						enumNodeMap.put(e, n);
					}
					else{
						//fetch button from titlescreen from receiver and update its next to be its next
						//i.e. go through all titlescenes and see if it contains button. If it does, update
						//button.
						fetchButton(condition).setTarget(condition.getChildren().get(0).getText());
					}
					
				}
				nodeConditionMap.put(node.toString(), enumNodeMap);
			}
		}
		for(String node: nodeConditionMap.keySet()){
			System.out.print(node + ": " + nodeConditionMap.get(node) + "\n");
		}
		
	}

	//TODO: implement this
	private NodeButton fetchButton(GameNode condition) {
		Set<String> titleScreens = myReceiver.getList("titleScene");
		for(String titleScene : titleScreens){
			//pseudo code
//			if(myReceiver.getButtonList(titleScene).contains(condition.getText()){
//				return button;
//			}
		}
		return null;
	}

	private Button addNodeButton() {
		//+ Button
		Button addGameNode = new Button("+ Node");
		addGameNode.setTextAlignment(TextAlignment.CENTER);
		addGameNode.setOnAction(e -> {
			nodeChooseDialog();
		});
		return addGameNode;
	}


	//TODO: extract to its own class?
	private void nodeChooseDialog(){
		
		Stage nodeDialog = new Stage();
		final ToggleGroup group = new ToggleGroup();
		
		RadioButton rb1 = new RadioButton("Scene Node");
		rb1.setToggleGroup(group);
		rb1.setSelected(true);

		RadioButton rb2 = new RadioButton("Condition Node");
		rb2.setToggleGroup(group);
		
		Button accept = new Button("Accept");
		accept.setOnAction(e -> {
			RadioButton r = (RadioButton) group.getSelectedToggle();
			
			GameNode gameNode = (GameNode) Reflection.createInstance("gae.view.gameEditor." + r.getText().replaceAll("\\s",""));
			gameNode.setReceiver(myReceiver);
			myNodes.add(gameNode);
			addSelectListener(gameNode);
			myRoot.getChildren().add(gameNode.getGroup());
			nodeDialog.close();
		});
		
		VBox choice = new VBox(CHOICE_SPACING);
		choice.setPadding(new Insets(CHOICE_SPACING));
		choice.getChildren().addAll(rb1, rb2, accept);
		
		
		nodeDialog.setScene(new Scene(choice));
		nodeDialog.show();
		 
	}
	
	/**
	 * adds a listener to the node to see if it was selected. This listener then looks for other selected of
	 * the opposite type 
	 */
	private void addSelectListener(GameNode node){
		node.getMyIn().isSelected().addListener(e -> {
			if(node.getMyIn().isSelected().getValue()){
				checkOutSelected(node);
			}
		});
	}
	
	/**
	 * goes through all of my nodes and checks for other selected outs. Ignores same node.
	 * @param node
	 */
	private void checkOutSelected(GameNode inNode) {
		for(GameNode outNode : myNodes){
			
			//if outnode is selected and outnode is not innode
			if(outNode.getMyOut().isSelected().getValue() && !outNode.equals(inNode) 
					&& outNode.draw() &&inNode.draw()){
				//draw line
				Line line = drawLine(inNode, outNode);
				
				outNode.addChild(inNode);
				
				line.setOnMouseEntered(new EventHandler<MouseEvent>() {
					
					@Override
					public void handle(MouseEvent event) {
						if(event.isShiftDown()){
							myRoot.getChildren().remove(line);
							outNode.removeChild(inNode);
						}
					}
					
				});
				
				//unselect the two selected nodes
				outNode.getMyOut().setSelected();
				inNode.getMyIn().setSelected();
			}
		}
		
	}

	/**
	 * creates properties that are bound so that the line will follow the Connectors whereever the node 
	 * moves.
	 * @param inNode
	 * @param outNode
	 * @return
	 */
	private Line drawLine(GameNode inNode, GameNode outNode) {
		Rectangle outNodeBody = outNode.getMyOut().getBody();
		Rectangle inNodeBody = inNode.getMyIn().getBody();
		DoubleProperty startX = new SimpleDoubleProperty();
		DoubleProperty startY = new SimpleDoubleProperty();
		DoubleProperty endX   = new SimpleDoubleProperty();
		DoubleProperty endY   = new SimpleDoubleProperty();
		startX.bind(outNodeBody.translateXProperty());
		startY.bind(outNodeBody.translateYProperty());
		endX.bind(inNodeBody.translateXProperty());
		endY.bind(inNodeBody.translateYProperty());
		Line line = new BoundLine(startX, startY, 
				endX, endY);
		myRoot.getChildren().add(line);
		return line;
	}

	@Override
	public void setUpEditor() {
		myNodes = new ArrayList<>();
		this.getChildren().add(drawGameEditor());
	}
	
	
	
}
