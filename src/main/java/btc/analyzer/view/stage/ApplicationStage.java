package btc.analyzer.view.stage;

import btc.analyzer.view.View;
import btc.analyzer.view.MainApplicationView;
import btc.analyzer.view.SettingsView;
import btc.analyzer.controller.Controller;
import btc.analyzer.controller.MainApplicationController;
import btc.analyzer.controller.SettingsController;
import btc.analyzer.system.AnalyzerSystem;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;

public class ApplicationStage extends Stage {
	
	private MainApplicationView mainApplicationView;
	private SettingsView settingsView;
	
	private Scene mainApplication;
	
	public ApplicationStage() {
		
		super();
		
		setTitle("Bitcoin Analyzer");
		setFullScreen(false);
		setWidth(1536);
		setHeight(864);
		setResizable(false);
		
		//set View
		this.mainApplicationView = new MainApplicationView();
		this.settingsView = new SettingsView();
		
		//set ApplicationStage for Controllers
		setControllerApplicationStage(mainApplicationView);
		setControllerApplicationStage(settingsView);
		
		//set Scenes and Show
		mainApplication = new Scene(mainApplicationView.getGroup());
		
	}
	
	public void setControllerApplicationStage(View v){
		((Controller)v.getLoader().getController()).setApplicationStage(this);
	}
	
	public void setControllerSystem(AnalyzerSystem aS) {
		((MainApplicationController)mainApplicationView.getLoader().getController()).setAnalyzerSystem(aS);
		((SettingsController)settingsView.getLoader().getController()).setAnalyzerSystem(aS);
	}
	
	public void update() {
		// render every view
		mainApplicationView.update();
	}
	
	public void setSceneAndShow(String group) {	
		try{
		switch(group){
		case "mainApplication":
			setScene(mainApplication);
			setFullScreen(false);
			break;
		case "settings":
			getMainApplicationGroup().getChildren().add(getSettingsGroup());
			break;
		default:
			throw new IllegalArgumentException("Ungueltige Eingabe!");
		} 
		}catch (java.lang.IllegalArgumentException e){}
	}
	
	public Group getMainApplicationGroup() {
		return mainApplicationView.getGroup();
	}
	
	public Group getSettingsGroup() {
		return settingsView.getGroup();
	}
	
	public MainApplicationController getMainApplicationController() {
		return mainApplicationView.getLoader().getController();
	}
	

}
