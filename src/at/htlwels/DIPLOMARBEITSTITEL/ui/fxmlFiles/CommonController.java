package at.htlwels.DIPLOMARBEITSTITEL.ui.fxmlFiles;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CommonController {

	private double xOffset;
	private double yOffset;
	public Stage stage;
	public Label title;
	public Button closeButton;


	public void initialize() {

		title.setOnMousePressed(event -> {
			xOffset = stage.getX() - event.getScreenX();
			yOffset = stage.getY() - event.getScreenY();
		});

		title.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() + xOffset);
			stage.setY(event.getScreenY() + yOffset);
		});

		closeButton.setOnMouseClicked(event->stage.close());

	}
}
