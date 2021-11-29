package dad.codegen.ui.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

	private MainController controller;
	
	public static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		App.primaryStage = primaryStage;
		
		this.controller = new MainController();
		
		primaryStage.setTitle("FXCodeGenerator");
		primaryStage.setScene(new Scene(controller.getView()));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/fx-64x64.png")));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args); 
	}

}
