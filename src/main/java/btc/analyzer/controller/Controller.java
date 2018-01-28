package btc.analyzer.controller;

import btc.analyzer.system.AnalyzerSystem;
import btc.analyzer.view.stage.ApplicationStage;

public class Controller {
	
	protected ApplicationStage applicationStage;
	protected AnalyzerSystem analyzerSystem;
	
	
	public void init() {
		
	}
	
	
	public ApplicationStage getApplicationStage() {
		return applicationStage;
	}
	
	public AnalyzerSystem getAnalyzerSystem() {
		return analyzerSystem;
	}
	
	public void setApplicationStage(ApplicationStage as){
		applicationStage = as;
	}
	
	public void setAnalyzerSystem(AnalyzerSystem az) {
		analyzerSystem = az;
	}

}
