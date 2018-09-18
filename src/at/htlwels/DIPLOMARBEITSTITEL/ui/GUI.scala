package at.htlwels.DIPLOMARBEITSTITEL.ui

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME._
import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.{Parser, Scanner}
import javafx.application._
import javafx.geometry.Pos
import javafx.scene._
import javafx.scene.control._
import javafx.scene.layout._
import javafx.stage._

object Helpers {
  var symbolTable: SymbolTable = null
  var filename=""

  def getOrError[T](variable: T): T = if (variable equals null) throw new RuntimeException("Error, variable is null") else variable

  def parseSymboltable(filename: String): Unit = {
    val parser = new Parser(new Scanner("testfiles/" + filename))
    parser.Parse()
    symbolTable = parser.symbolTable
    this.filename=filename
  }
}


trait GUI_Generator {
  def generate(pane: Pane)

  implicit class OnClickMakro(node: javafx.scene.Node) {
    def onClick(function: => Unit) = node.setOnMouseClicked(event => function)
  }

  implicit class GetChildrenMakro(pane: Pane) {
    def addChild(child: javafx.scene.Node) = pane.getChildren.add(child)
  }

}


object GUI {
  def main(args: Array[String]): Unit = {
    Application.launch(new GUI().getClass)
  }
}

class GUI extends Application {
  override def start(myStage: Stage): Unit = {
    myStage.setTitle("AST Node Editor")

    val scrollPane = new ScrollPane
    val rootNode = new HBox

    Main_GUI_Generator.generate(rootNode)

    scrollPane.setContent(rootNode)

    val myScene = new Scene(scrollPane)
    myStage.setScene(myScene)
    myStage.show
  }
}

object Main_GUI_Generator extends GUI_Generator {
  override def generate(pane: Pane): Unit = {
    pane.getChildren.clear

    val filename_textfield = new TextField(Helpers.filename)
    val parse_button = new Button("Parse")
    val interprete_button = new Button("Interprete")
    val show_ast_button = new Button("Show AST")


    interprete_button.onClick {
      if (Helpers.symbolTable != null)
        new at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter(Helpers.symbolTable).startProgramFrom("main")
    }

    show_ast_button.onClick {
      if (Helpers.symbolTable != null)
        AST_Viewer_Generator.generate(pane)
    }

    parse_button.onClick {
      Helpers.parseSymboltable(filename_textfield.getText)
    }

    pane.addChild(interprete_button)
    pane.addChild(show_ast_button)

    pane.addChild(filename_textfield)
    pane.addChild(parse_button)
  }
}

object AST_Viewer_Generator extends GUI_Generator {
  def generate(parent: Pane) = {
    parent.getChildren.clear

    val backButton = new Button("<<")

    backButton.onClick {
      Main_GUI_Generator.generate(parent)
    }


    parent.addChild(backButton)
    parent.autosize
    var outermostNode = Helpers.symbolTable.curScope
    while (outermostNode.outer != null) outermostNode = outermostNode.outer
    var node = outermostNode.locals


    while (node != null) {
      if (node.kind == ObjKind.PROC) {
        parent.addChild(constructVisuals(node.ast, node.name))
      }
      node = node.next
    }

  }


  def constructVisuals(node: at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Node, name: String = null): Pane = {

    val rootBox = new VBox
    val superSelf = new VBox
    val self = new HBox
    var nodeLabel: Label = null


    node.kind match {
      case NodeKind.STATSEQ => nodeLabel = new Label(name)
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
        val stage = new Stage(StageStyle.DECORATED)
        val rootNode = new VBox
        val submitButton = new Button("Submit")


        rootNode.setAlignment(Pos.CENTER)

        node.kind match {
          case NodeKind.INTCON => {
            val textField = new TextField(node.`val` + "")


            rootNode.addChild(new Label("Integer"))
            rootNode.addChild(textField)


            submitButton.onClick {
              node.`val` = Integer.parseInt(textField.getText)
              nodeLabel.setText(node.`val` + "")
              stage.close()
            }

          }
          case NodeKind.CHARCON => {
            val textField = new TextField(node.`val`.toChar + "")


            rootNode.addChild(new Label("Character"))
            rootNode.addChild(textField)


            submitButton.onClick {
              node.`val` = textField.getText.charAt(0)
              nodeLabel.setText(node.`val`.toChar + "")
              stage.close()
            }

          }
          case NodeKind.FLOATCON => {
            val textField = new TextField(node.fVal + "")
            rootNode.addChild(new Label("Float"))
            rootNode.addChild(textField)
            submitButton.onClick {
              node.fVal = java.lang.Float.parseFloat(textField.getText)
              nodeLabel.setText(node.fVal + "")
              stage.close()
            }
          }

          case NodeKind.STRINGCON => {
            val textField = new TextField(node.strVal)
            rootNode.addChild(new Label("String"))
            rootNode.addChild(textField)
            submitButton.onClick {
              node.strVal = textField.getText
              nodeLabel.setText(node.strVal)
              stage.close()
            }
          }

          case _ => {
            if (NodeKind.isBoolean(node.kind)) {
              val menuButton = new MenuButton(node.kind.name())
              val nodeKinds = at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.NodeKind.values

              nodeKinds.foreach(nodeKind => {
                if (NodeKind.isBoolean(nodeKind)) {
                  val menuItem = new MenuItem(nodeKind.name)
                  menuItem.setOnAction(event => {
                    menuButton.setText(nodeKind.name)
                    //                    println(nodeKind.name + " is Selected")
                    node.kind = nodeKind
                    nodeLabel.setText(nodeKind.name())
                  })
                  menuButton.getItems.add(menuItem)
                }
              })
              rootNode.addChild(menuButton)
              submitButton.onClick {
                stage.close()
              }
            } else println("code not implemented yet")
          }
        }


        rootNode.addChild(submitButton)

        val scene = new Scene(rootNode)
        stage.setScene(scene)
        stage.show
      }
      )
    }


    superSelf.addChild(self)
    rootBox.addChild(superSelf)


    rootBox.setAlignment(Pos.TOP_CENTER)
    superSelf.setAlignment(Pos.TOP_CENTER)
    self.setAlignment(Pos.TOP_CENTER)
    //    superSelf.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
    //      + "-fx-border-width: 2;" + "-fx-border-insets: 2;"
    //      + "-fx-border-radius: 5;" + "-fx-border-color: yellow;")

    //Right Child
    if (node.left != null) {
      val leftChild = constructVisuals(node.left)

      if (leftChild != null) {
        leftChild.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
          + "-fx-border-width: 2;" + "-fx-border-insets: 2;"
          + "-fx-border-radius: 2;" + "-fx-border-color: red;")
        self.addChild(leftChild)
      }
    }

    //Left Child
    if (node.right != null) {
      val rightChild = constructVisuals(node.right)

      if (rightChild != null) {
        rightChild.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
          + "-fx-border-width: 2;" + "-fx-border-insets: 2;"
          + "-fx-border-radius: 2;" + "-fx-border-color: blue;")
        self.addChild(rightChild)
      }
    }


    if (node.next != null) {
      val magentaBox = new VBox
      magentaBox.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 2;"
        + "-fx-border-radius: 2;" + "-fx-border-color: magenta;")

      rootBox.addChild(magentaBox)
      rootBox.addChild(constructVisuals(node.next))
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