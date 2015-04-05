package com.creatingskies.game.map;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.core.Map;
import com.creatingskies.game.core.Tile;

public class MapDesignerController {
	
	@FXML private Label viewTitle;
	@FXML private GridPane mapTiles;
	
	private Map map;
	private Stage stage;
	private Image selectedImage = 
			new Image(MainLayout.class.getResourceAsStream("/images/tiles/stone_little_bricks.jpg"),48,48,true,true);
	
	public void init(){
		mapTiles.getChildren().clear();
		mapTiles.getChildren().removeAll(mapTiles.getChildren());
		for(Tile tile : map.getTiles()){
			ImageView image = new ImageView(
					new Image(MainLayout.class.getResourceAsStream("/images/tiles/grass.jpg"),48,48,true,true));
			Pane tilePane = new Pane(image);
			tilePane.setStyle("-fx-border-color:#375a7f;-fx-border-width:0.5;-fx-background-color:white;");
			
			mapTiles.add(tilePane, tile.getColIndex(), tile.getRowIndex());
			
			tilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					image.setImage(selectedImage);
					System.out.println("c = "+ tile.getColIndex() +" : "+" r = "+ tile.getRowIndex());
				}
			});
		}
	}
	
	public void show(Map map){
		try{
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("Designer.fxml"));
	        AnchorPane designer = (AnchorPane) loader.load();
	        
	        designer.getStylesheets().add("/css/style.css");
	        stage = new Stage();
	        stage.setTitle(map.getName());
	        stage.initModality(Modality.WINDOW_MODAL);
	        stage.initOwner(MainLayout.getPrimaryStage());
	        stage.initStyle(StageStyle.UNDECORATED);
	        Scene scene = new Scene(designer);
	        stage.setMaximized(true);
	        stage.setScene(scene);
	        
	        MapDesignerController controller = (MapDesignerController) loader.getController();
	        controller.setMap(map);
	        controller.setStage(stage);
	        controller.setViewTitle("Edit " + map.getName());
	        controller.init();
	        stage.showAndWait();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@FXML
	private void okButtonClicked(){
		stage.close();
	}
	
	public Map getMap() {
		return map;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setViewTitle(String title){
		viewTitle.setText(title);
	}
}
