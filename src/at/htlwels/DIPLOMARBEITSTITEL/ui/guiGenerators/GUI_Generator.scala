package at.htlwels.DIPLOMARBEITSTITEL.ui.guiGenerators

import javafx.scene.layout.Pane

trait GUI_Generator {
	def generate

	implicit class OnClickMakro(node: javafx.scene.Node) {
		def onClick(function: => Unit) = node.setOnMouseClicked(event => function)
	}

	implicit class GetChildrenMakro(pane: Pane) {
		def addChild(child: javafx.scene.Node) = pane.getChildren.add(child)
	}

}
