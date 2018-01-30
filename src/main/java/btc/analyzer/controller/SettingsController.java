package btc.analyzer.controller;

import btc.analyzer.system.AnalyzerSystem;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class SettingsController extends Controller{
	
	private AnalyzerSystem analyzerSystem;
	
	@FXML TextField blocktrailPath; 
	@FXML TextField blocktrailKey; 
	@FXML TextField blocktrailSecret;
	
	@FXML Text error;
	
	@FXML Button okayButton;
	@FXML Button cancelButton;
	
	@FXML
	public void confirm() {
		Boolean decision = true;
		
		if(blocktrailPath.getText().length() != 0) analyzerSystem.setBlocktrailPath(blocktrailPath.getText());
		else decision = false;
		
		if(blocktrailKey.getText().length() != 0) analyzerSystem.setBlocktrailKey(blocktrailKey.getText());
		else decision = false;
		
		if(blocktrailSecret.getText().length() != 0) analyzerSystem.setBlocktrailSecret(blocktrailSecret.getText());
		else decision = false;
		
		if(decision.equals(true)) {
			error.setText("             ");
			analyzerSystem.getApplicationStage().getMainApplicationController().getCheckAddressButton().setDisable(false);
			analyzerSystem.getApplicationStage().getMainApplicationGroup().getChildren().remove(analyzerSystem.getApplicationStage().getMainApplicationGroup().getChildren().size() - 1);
		}
		else {
			error.setText("Invalid input");
		}
	}
	
	@FXML
	public void cancel(){
		error.setText("             ");
		blocktrailPath.setText(analyzerSystem.getBlocktrailPath());
		blocktrailKey.setText(analyzerSystem.getBlocktrailKey());
		blocktrailSecret.setText(analyzerSystem.getBlocktrailSecret());
		analyzerSystem.getApplicationStage().getMainApplicationGroup().getChildren().remove(analyzerSystem.getApplicationStage().getMainApplicationGroup().getChildren().size() - 1);
	}
	
	@Override
	public void setAnalyzerSystem(AnalyzerSystem aS) {
		analyzerSystem = aS;
	}
	
	public void update() {
	}
	
}
