package com.creatingskies.game.map;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import com.creatingskies.game.classes.PropertiesViewController;
import com.creatingskies.game.classes.TableViewController.Action;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.core.Map;

public class MapPropertiesController extends PropertiesViewController{

	private Map getMap(){
		return (Map) getCurrentRecord();
	}
	
	@Override
	protected String getViewTitle() {
		System.out.println(getCurrentAction());
		if(getCurrentAction() == Action.ADD){
			return "Create New Map";
		}else if (getCurrentAction() == Action.EDIT){
			return "Edit Map "+ getMap().getName();
		}else {
			return "Map " + getMap().getName();
		}
	}
	
	public void show(Action action,Map map){
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MapProperties.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            MapPropertiesController controller = (MapPropertiesController)loader.getController();
            controller.setCurrentAction(action);
            controller.setCurrentRecord(map);
            controller.init();
            MainLayout.getRootLayout().setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
