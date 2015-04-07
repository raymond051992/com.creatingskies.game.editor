package com.creatingskies.game.map;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.creatingskies.game.classes.Util;
import com.creatingskies.game.classes.ViewController.Action;
import com.creatingskies.game.common.AlertDialog;
import com.creatingskies.game.common.MainLayout;
import com.creatingskies.game.core.Map;
import com.creatingskies.game.core.MapDao;
import com.creatingskies.game.core.Tile;
import com.creatingskies.game.core.TileImage;
import com.creatingskies.game.model.Constant;

public class MapDesignerController {
	
	@FXML private SplitPane mapDesignerContainer;
	@FXML private Label viewTitle;
	@FXML private GridPane mapTiles;
	@FXML private FlowPane tileImageSelections;
	@FXML private ImageView selectedTileImageView;
	@FXML private ImageView selectedMapTileImageView;
	@FXML private Label selectedTileLocX;
	@FXML private Label selectedTileLocY;
	@FXML private CheckBox selectedTileIsStartPoint;
	@FXML private CheckBox selectedTileIsEndPoint;
	@FXML private CheckBox selectedTileIsObstacle;
	@FXML private Button saveButton;
	@FXML private Button cancelButton;
	
	private Map map;
	private Stage stage;
	
	private Action currentAction;
	private Tile selectedTile;
	private TileImage selectedTileImage;
	
	public void init(){
		initMapTiles();
		initTileImageSelections();
		
		mapDesignerContainer.setDisable(getCurrentAction() == Action.VIEW);
		saveButton.setVisible(getCurrentAction() != Action.VIEW);
		if(getCurrentAction() == Action.VIEW){
			cancelButton.setText("Ok");
		}else{
			cancelButton.setText("Cancel");
		}
	}
	
	private void initMapTiles(){
		mapTiles.getChildren().clear();
		for(Tile tile : map.getTiles()){
			ImageView imageView = new ImageView();
			imageView.setFitHeight(Constant.TILE_HEIGHT);
			imageView.setFitWidth(Constant.tILE_WIDTH);
			imageView.setImage(Util.byteArrayToImage(tile.getImage()));
			Pane tilePane = new Pane(imageView);
			tilePane.getStyleClass().add("map-designer-tile");
			mapTiles.add(tilePane, tile.getColIndex(), tile.getRowIndex());
			
			tilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if(getCurrentAction() != null){
						if(selectedTileImage != null){
							imageView.setImage(selectedTileImageView.getImage());
							tile.setImage(selectedTileImage.getImage());
						}
						
						selectedTile = tile;
						selectedMapTileImageView = imageView;
						selectedTileLocX.setText(String.valueOf(tile.getRowIndex()));
						selectedTileLocY.setText(String.valueOf(tile.getColIndex()));
						selectedTileIsStartPoint.setSelected(tile.getStartPoint());
						selectedTileIsEndPoint.setSelected(tile.getEndPoint());
						selectedTileIsObstacle.setSelected(tile.getObstacle());
					}
				}
			});
		}
	}
	
	private void initTileImageSelections(){
		tileImageSelections.getChildren().clear();
		MapDao mapDao = new MapDao();
		List<TileImage> tileImages = mapDao.findAllTileImages();
		addTileImageSelection(new TileImage());
		if(tileImages != null && !tileImages.isEmpty()){
			for(TileImage tileImage : tileImages){
				addTileImageSelection(tileImage);
			}
		}
	}
	
	private void addTileImageSelection(TileImage tileImage){
		ImageView imageView = new ImageView(Util.byteArrayToImage(tileImage.getImage()));
		imageView.setFitHeight(Constant.TILE_HEIGHT);
		imageView.setFitWidth(Constant.tILE_WIDTH);
		Pane pane = new Pane(imageView);
		
		if(imageView.getImage() == null){
			pane.getStyleClass().add("map-blank-tile");
		}
		
		pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				selectedTileImageView.setImage(imageView.getImage());
				selectedTileImage = tileImage;
			}
		});
		
		tileImageSelections.getChildren().add(pane);
	}
	
	public void show(Action action, Map map){
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
        	controller.setViewTitle(map.getName());
	        
	        controller.setCurrentAction(action);
	        controller.init();
	        stage.showAndWait();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@FXML
	private void uploadTileImage(){
		FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Image files", "*.jpeg", "*.jpg", "*.png", "*.bmp", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(MainLayout.getPrimaryStage());
        
        if(file != null){
        	TileImage tileImage = new TileImage();
            tileImage.setFileName(file.getName());
            tileImage.setFileType(Util.getFileExtension(file.getName()));
            tileImage.setFileSize(file.length());
            tileImage.setImage(Util.fileToByteArray(file));
            
            new MapDao().save(tileImage);
            
            addTileImageSelection(tileImage);
        }
	}
	
	@FXML
	private void setSelectedTileTheStartPoint(){
		Tile tile = map.getStartPoint();
		if(tile != null){
			tile.setStartPoint(false);
		}else {
			if(selectedTileIsStartPoint.isSelected()){
				selectedTile.setStartPoint(true);
				
				Image image = new Image(Constant.PATH_TILE_IMAGE_START_POINT);
				selectedTile.setImage(Util.imageToByteArray(image, "png"));
				selectedMapTileImageView.setImage(image);
			}else{
				selectedTile.setStartPoint(false);
				selectedTile.setImage(null);
				selectedMapTileImageView.setImage(selectedTileImageView.getImage());
			}
		}
	}
	
	@FXML
	private void setSelectedTileTheEndPoint(){
		Tile tile = map.getEndPoint();
		if(tile != null){
			tile.setEndPoint(false);
		}else {
			if(selectedTileIsEndPoint.isSelected()){
				selectedTile.setEndPoint(true);
				Image image = new Image(Constant.PATH_TILE_IMAGE_END_POINT);
				selectedTile.setImage(Util.imageToByteArray(image, "png"));
				selectedMapTileImageView.setImage(image);
			}else{
				selectedTile.setEndPoint(false);
				selectedTile.setImage(null);
				selectedMapTileImageView.setImage(selectedTileImageView.getImage());
			}
		}
	}
	
	@FXML
	private void saveButtonClicked(){
		if(map.getStartPoint() == null || map.getEndPoint() == null){
			new AlertDialog(AlertType.ERROR, "Invalid Map", null, "Map should have 1 start point and 1 end point.",stage).showAndWait();
		}else{
			new MapPropertiesController().show(currentAction, map);
			stage.close();
		}
	}
	
	@FXML
	private void cancelButtonClicked(){
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
	
	public Action getCurrentAction() {
		return currentAction;
	}
	
	public void setCurrentAction(Action currentAction) {
		this.currentAction = currentAction;
	}
}
