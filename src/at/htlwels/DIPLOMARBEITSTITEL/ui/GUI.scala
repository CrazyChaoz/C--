package at.htlwels.DIPLOMARBEITSTITEL.ui

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME._
import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.{Parser, Scanner}
import at.htlwels.DIPLOMARBEITSTITEL.ui.fxmlFiles.{BooleanFXMLController, ConstantFXMLController, MainFXMLController, StatementFXMLController}
import javafx.application._
import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene._
import javafx.scene.control._
import javafx.scene.layout._
import javafx.stage._

object Helpers {
	var symbolTable: SymbolTable = _
	var filename = ""

	def getOrError[T](variable: T): T = if (variable equals null) throw new RuntimeException("Error, variable is null") else variable

	def parseSymboltable(filename: String): Unit = {
		val parser = new Parser(new Scanner("testfiles/" + filename))
		parser.Parse
		symbolTable = parser.symbolTable
		this.filename = filename
	}

}


trait GUI_Generator {
	def generate

	implicit class OnClickMakro(node: javafx.scene.Node) {
		def onClick(function: => Unit) = node.setOnMouseClicked(event => function)
	}

	implicit class GetChildrenMakro(pane: Pane) {
		def addChild(child: javafx.scene.Node) = pane.getChildren.add(child)
	}

}


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


//######################################################
//######################################################
//######################################################


object Main_GUI_Generator extends GUI_Generator {
	override def generate = {

		val fxmlLoader = new FXMLLoader

		fxmlLoader.load(this.getClass.getResourceAsStream("fxmlFiles/MainFXML.fxml"))
		val controller = fxmlLoader.getController.asInstanceOf[MainFXMLController]


		controller.interpreteButton.onClick {
			if (Helpers.symbolTable != null)
				new at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter(Helpers.symbolTable).startProgramFrom("main")
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


//######################################################
//######################################################
//######################################################


object AST_Viewer_Generator extends GUI_Generator {
	def generate = {
		val parent = new HBox
		val scrolly = new ScrollPane(parent)


		val backButton = new HBox

		backButton.onClick {
			Main_GUI_Generator.generate
		}

		backButton.setId("backButton")

		parent.addChild(backButton)

		var outermostNode = Helpers.symbolTable.curScope
		while (outermostNode.outer != null) outermostNode = outermostNode.outer
		var node = outermostNode.locals


		while (node != null) {
			if (node.kind == ObjKind.PROC) {
				parent.addChild(constructVisuals(node.ast, node.name))
			}
			node = node.next
		}

		GUI.stage.setScene(new Scene(scrolly))

		GUI.stage.getScene.getStylesheets.addAll(this.getClass.getResource("stylesheet.css").toExternalForm)
		GUI.stage.setFullScreen(true)
	}


	def initConstructVisuals(node: at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Node, name: String): Pane = {
		val nodePrev = new at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Node(0)
		nodePrev.next = node
		constructVisuals(node, name, nodePrev)
	}

	def constructVisuals(node: at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Node, name: String = null, nodePref: at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Node = null): Pane = {


		val rootBox = new VBox
		val superSelf = new VBox
		val self = new HBox
		var nodeLabel: Label = null


		node.kind match {
			case NodeKind.STATSEQ => {
				nodeLabel = new Label(name)
				rootBox.setId("procedure")
			}

			case NodeKind.IDENT => nodeLabel = new Label(node.obj.name)
			case NodeKind.INTCON => nodeLabel = new Label(node.`val` + "")
			case NodeKind.CHARCON => nodeLabel = new Label(node.`val`.toChar + "")
			case NodeKind.FLOATCON => nodeLabel = new Label(node.fVal + "")
			case NodeKind.STRINGCON => nodeLabel = new Label(node.strVal)
			case NodeKind.TRAP => return null
			case _ => nodeLabel = new Label(node.kind.name)
		}

		superSelf.addChild(nodeLabel)
		nodeLabel.onClick {
			Platform.runLater(() => {
				val stage: Stage = new Stage(StageStyle.UNDECORATED)
				val fxmlLoader = new FXMLLoader


				def constantPopup(title: String, value: String, onSave: (TextField) => Unit): Unit = {
					fxmlLoader.load(this.getClass.getResourceAsStream("fxmlFiles/ConstantFXML.fxml"))
					val controller: ConstantFXMLController = fxmlLoader.getController.asInstanceOf[ConstantFXMLController]

					controller.title.setText(title)
					controller.textField.setText(value)
					controller.stage = stage

					controller.saveButton.onClick {
						onSave(controller.textField)
					}

					stage.setScene(new Scene(fxmlLoader.getRoot.asInstanceOf[Pane]))
				}

				def booleanPopup: Unit = {
					fxmlLoader.load(this.getClass.getResourceAsStream("fxmlFiles/BooleanFXML.fxml"))
					val controller: BooleanFXMLController = fxmlLoader.getController.asInstanceOf[BooleanFXMLController]

					controller.menuButton.setText(node.kind.name)
					controller.stage = stage

					at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.NodeKind.values.foreach(nodeKind => {
						if (NodeKind.isBoolean(nodeKind)) {
							val menuItem = new MenuItem(nodeKind.name)
							menuItem.setOnAction(_ => {
								controller.menuButton.setText(nodeKind.name)
								node.kind = nodeKind
								nodeLabel.setText(nodeKind.name)
							})
							controller.menuButton.getItems.add(menuItem)
						}
					})
					stage.setScene(new Scene(fxmlLoader.getRoot.asInstanceOf[Pane]))
				}

				def statementPopup: Unit = {
					fxmlLoader.load(this.getClass.getResourceAsStream("fxmlFiles/StatementFXML.fxml"))
					val controller = fxmlLoader.getController.asInstanceOf[StatementFXMLController]

					controller.nameLabel.setText(node.kind.name)
					controller.stage = stage

					controller.deleteButton.onClick {
						if (node.next != null)
							nodePref.next = node.next
						stage.close

						AST_Viewer_Generator.generate
					}

					stage.setScene(new Scene(fxmlLoader.getRoot.asInstanceOf[Pane]))
				}

				node.kind match {
					case NodeKind.INTCON =>
						constantPopup("Integer", node.`val` + "", (textField: TextField) => {
							node.`val` = Integer.parseInt(textField.getText)
							nodeLabel.setText(node.`val` + "")
							stage.close
						})

					case NodeKind.CHARCON => {
						constantPopup("Character", node.`val`.toChar + "", (textField: TextField) => {
							node.`val` = textField.getText.charAt(0)
							nodeLabel.setText(node.`val`.toChar + "")
							stage.close
						})
					}


					case NodeKind.FLOATCON => {
						constantPopup("Float", node.fVal + "", (textField: TextField) => {
							node.fVal = java.lang.Float.parseFloat(textField.getText)
							nodeLabel.setText(node.fVal + "")
							stage.close
						})
					}

					case NodeKind.STRINGCON => {
						constantPopup("String", node.strVal + "", (textField: TextField) => {
							node.strVal = textField.getText
							nodeLabel.setText(node.strVal)
							stage.close
						})
					}

					case _ => {
						if (NodeKind.isBoolean(node.kind)) {
							booleanPopup
						} else if (NodeKind.isStatement(node.kind)) {
							statementPopup
						} else println("code not implemented yet")
					}
				}

				stage.show
			}
			)
		}


		superSelf.addChild(self)
		rootBox.addChild(superSelf)


		rootBox.setAlignment(Pos.TOP_CENTER)
		superSelf.setAlignment(Pos.TOP_CENTER)
		self.setAlignment(Pos.TOP_CENTER)

		//Right Child
		if (node.left != null) {
			val leftBox = constructVisuals(node.left)
			if (leftBox != null) {
				leftBox.setId("leftChild")
				self.addChild(leftBox)
			}
		}

		//Left Child
		if (node.right != null) {
			val rightBox = constructVisuals(node.right)
			if (rightBox != null) {
				rightBox.setId("rightChild")
				self.addChild(rightBox)
			}
		}


		if (node.next != null) {
			val box = new VBox
			box.setId("nextBox")
			rootBox.addChild(box)
			rootBox.addChild(constructVisuals(node.next, "", node))
		}

		rootBox
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