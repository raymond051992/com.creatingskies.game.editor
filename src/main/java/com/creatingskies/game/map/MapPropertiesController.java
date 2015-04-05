package com.creatingskies.game.map;

import com.creatingskies.game.classes.PropertiesViewController;
import com.creatingskies.game.classes.TableViewController.Action;
import com.creatingskies.game.core.Map;

public class MapPropertiesController extends PropertiesViewController{

	@Override
	protected String getViewTitle() {
		if(getCurrentAction() != null){
			if(getCurrentAction() == Action.ADD){
				return "Create New Map";
			}else if (getCurrentAction() == Action.EDIT){
				return "Edit Map "+ getMap().getName();
			}
		}
		return "Map " + getMap().getName();
	}
	
	private Map getMap(){
		return (Map) getMap();
	}

}
