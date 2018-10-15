package at.htlwels.DIPLOMARBEITSTITEL.ui

import at.htlwels.DIPLOMARBEITSTITEL.ui.guiGenerators.Main_GUI_Generator
import javafx.application._
import javafx.stage._


object GUI {
	var stage: Stage = _

	def main(args: Array[String]): Unit = {
		Application.launch(new GUI getClass)
	}
}

class GUI extends Application {
	override def start(mainStage: Stage): Unit = {
		GUI.stage = mainStage
		mainStage.initStyle(StageStyle.UNDECORATED)
		mainStage.setTitle("AST Node Editor")
		Main_GUI_Generator.generate
		mainStage.show
	}
}




//def getDropdownButton = {
//  //
//  val menuButton = new MenuButton("Type of Node")
//
//  val nodeKinds = at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.NodeKind.values
//  nodeKinds.foreach(nodeKind => {
//  val menuItem = new MenuItem(nodeKind.name)
//  menuItem.setOnAction(event => {
//  menuButton.setText(nodeKind.name)
//  println(nodeKind.name + " is Selected")
//})
//  menuButton.getItems.add(menuItem)
//})
//
//  menuButton
//}