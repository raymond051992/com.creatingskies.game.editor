package com.creatingskies.game.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import com.creatingskies.game.classes.PropertiesViewController;
import com.creatingskies.game.common.AlertDialog;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.core.Map;
import com.creatingskies.game.core.Tile;

public class MapPropertiesController extends PropertiesViewController{
	
	@FXML private TextField nameTextField;
	@FXML private TextArea descriptionTextField;
	@FXML private TextField widthTextField;
	@FXML private TextField heightTextField;
	
	@FXML private Button openDesignerButton;
	@FXML private Button saveButton;
	@FXML private Button cancelButton;
	
	@Override
	public void init() {
		super.init();
		
		widthTextField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (!t.getCharacter().matches("\\d")) {
                    t.consume();
                }
            }
        });
		widthTextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    Integer.parseInt(newValue);
                } catch (Exception e) {
                	widthTextField.setText(oldValue);
               }
            }
        });
		
		heightTextField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (!t.getCharacter().matches("\\d")) {
                    t.consume();
                }
            }
        });
		heightTextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    Integer.parseInt(newValue);
                } catch (Exception e) {
                	widthTextField.setText(oldValue);
               }
            }
        });
	}
	
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
	
	@FXML
	private void showMapDesigner(){
		if(isValid()){
			updateMapDetail();
			new MapDesignerController().show(getMap());
		}
	}
	
	private boolean isValid(){
		if(nameTextField.getText().isEmpty()){
			new AlertDialog(AlertType.ERROR, "Oops", "", "Name is required.").showAndWait();
			return false;
		}
		if(descriptionTextField.getText().isEmpty()){
			new AlertDialog(AlertType.ERROR, "Oops", "", "Description is required.").showAndWait();
			return false;
		}
		if(widthTextField.getText().isEmpty()){
			new AlertDialog(AlertType.ERROR, "Oops", "", "Width is required.").showAndWait();
			return false;
		}
		if(heightTextField.getText().isEmpty()){
			new AlertDialog(AlertType.ERROR, "Oops", "", "Height is required.").showAndWait();
			return false;
		}
			
		return true;
	}
	
	private void updateMapDetail(){
		getMap().setName(nameTextField.getText());
		getMap().setDescription(descriptionTextField.getText());
		Integer width = Integer.parseInt(widthTextField.getText());
		Integer height = Integer.parseInt(heightTextField.getText());
		
		List<Tile> tiles = new ArrayList<Tile>();
		for(int r = 0;r < height;r++){
			for(int c = 0;c < width;c++){
				Tile tile = new Tile();
				tile.setMap(getMap());
				tile.setColIndex(c);
				tile.setRowIndex(r);
				tile.setObstacle(false);
				tiles.add(tile);
			}
		}
		
		getMap().setTiles(tiles);
	}
}

