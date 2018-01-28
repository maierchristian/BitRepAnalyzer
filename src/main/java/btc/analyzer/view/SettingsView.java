package btc.analyzer.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;

public class SettingsView extends View{
	
	public SettingsView() {
		
		group = new Group();

		

		loader = new FXMLLoader(getClass().getResource("/fxml/Settings.fxml"));
		loader.setRoot(loader.getRoot());

		try {
			this.group.getChildren().add((Node)loader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void update() {
	}

}
