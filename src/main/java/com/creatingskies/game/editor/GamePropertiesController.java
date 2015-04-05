package com.creatingskies.game.editor;

import com.creatingskies.game.classes.ViewController;
import com.creatingskies.game.core.Game;

public class GamePropertiesController extends ViewController{

	private Action currentAction;
	private Game game;
	
	@Override
	protected String getViewTitle() {
		if(currentAction != null){
			if(currentAction == Action.ADD){
				return "Create New Game";
			}else if (currentAction == Action.EDIT){
				return "Edit Game " + game.getTitle();
			}
		}
		return game.getTitle();
	}
	
	public void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}

}
