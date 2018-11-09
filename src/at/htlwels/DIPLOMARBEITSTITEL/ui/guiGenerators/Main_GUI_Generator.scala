package at.htlwels.DIPLOMARBEITSTITEL.ui.guiGenerators

import at.htlwels.DIPLOMARBEITSTITEL.ui.fxmlFiles.MainFXMLController
import at.htlwels.DIPLOMARBEITSTITEL.ui.{GUI, Helpers}
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane

object Main_GUI_Generator extends GUI_Generator {
	override def generate = {

		val fxmlLoader = new FXMLLoader

		fxmlLoader.load(this.getClass.getResourceAsStream("../fxmlFiles/MainFXML.fxml"))
		val controller = fxmlLoader.getController.asInstanceOf[MainFXMLController]


		controller.interpreteButton.onClick {
			if (Helpers.symbolTable != null)
				new at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter(Helpers.symbolTable).startProgramFrom("main")
//				new Thread(()=>{new at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter(Helpers.symbolTable).startProgramFrom("main")})
		}

		controller.showAstButton.onClick {
			if (Helpers.symbolTable != null)
				AST_Viewer_Generator.generate
		}

		controller.parseButton.onClick {
			if (controller.fileField.getText != null && controller.fileField.getText != "")
				Helpers.parseSymboltable(controller.fileField.getText)
		}
		controller.closeButton.onClick {
			System.exit(0)
		}

		controller.stage = GUI.stage

		controller.fileField.setText(Helpers.filename)

		GUI.stage.setScene(new Scene(fxmlLoader.getRoot.asInstanceOf[Pane]))
	}
}
