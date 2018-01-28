package btc.analyzer;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import javafx.application.Application;
import javafx.stage.Stage;

import btc.analyzer.system.AnalyzerSystem;
import btc.analyzer.view.stage.ApplicationStage;

public class AnalyzerApp extends Application {

	public static void main(String[] args) throws IOException {
		launch(args);
	}
	
	@Override
	public void start(Stage pimaryStage) throws IOException, XMLStreamException {
		
		AnalyzerSystem analyzerSystem;
		ApplicationStage applicationStage;
		
		analyzerSystem = new AnalyzerSystem();
		applicationStage = new ApplicationStage();
		
		analyzerSystem.setApplicationStage(applicationStage);
		applicationStage.setControllerSystem(analyzerSystem);
		
		applicationStage.setSceneAndShow("mainApplication");
		applicationStage.show();
		
	}
	
}
