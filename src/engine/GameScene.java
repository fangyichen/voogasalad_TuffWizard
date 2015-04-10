package engine;

import javafx.animation.KeyFrame;
import javafx.util.Duration;

public abstract class GameScene {
	
	private GameScene myNext;
	protected boolean myHasCompleted;
	private KeyFrame myScene;
	protected boolean myGameLost;
	protected boolean myGameWon;
	public long myStartTime;
	
	public GameScene(){
		myGameLost = false;
	}

	public KeyFrame start(double frameRate) {
		myStartTime = System.nanoTime();
		myScene = new KeyFrame(Duration.millis(frameRate * 10), e -> update());
		return myScene;
	}

	public abstract void update();
	
	public abstract void checkComplete();
	
	public GameScene getNextScene(){
		return myNext;
	}
	
	public KeyFrame getCurScene(){
		return myScene;
	}
	
	public boolean isComplete(){
		checkComplete();
		return myHasCompleted;
	}
	
	public void setNextScene(GameScene gameScene){
		myNext = gameScene;
	}
}